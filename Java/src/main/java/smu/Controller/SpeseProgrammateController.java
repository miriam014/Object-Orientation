package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.Control.SpeseProgrammateControl;
import smu.Sessione;
import smu.DTO.SpeseProgrammate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SpeseProgrammateController extends Controller {

    @FXML private TableView<SpeseProgrammate> TabellaProgrammazioni;
    @FXML private TableColumn<SpeseProgrammate, String> destinatarioColumn;
    @FXML private TableColumn<SpeseProgrammate, Boolean> statoColumn;
    @FXML private TableColumn<SpeseProgrammate, String> cartaColumn;
    @FXML private TableColumn<SpeseProgrammate, Float> importoColumn;
    @FXML private TableColumn<SpeseProgrammate, String> dataColumn;
    @FXML private TableColumn<SpeseProgrammate, String> dataTermineColumn;
    @FXML private TableColumn<SpeseProgrammate, String> nomeColumn;
    @FXML private TableColumn<SpeseProgrammate, String> frequenzaColumn;

    private SpeseProgrammateControl speseProgrammateControl; // Il servizio che contiene la logica di business

    @FXML private Button deleteProgram;
    @FXML private Button NewProgram;
    @FXML private Button ChangeProgram;

    @FXML
    public void initialize() {
        speseProgrammateControl = new SpeseProgrammateControl(); // Inizializza il servizio

        // Imposta i valori per le colonne della tabella
        destinatarioColumn.setCellValueFactory(new PropertyValueFactory<>("Destinatario"));
        cartaColumn.setCellValueFactory(new PropertyValueFactory<>("NumeroCarta"));
        importoColumn.setCellValueFactory(new PropertyValueFactory<>("Importo"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("DataScadenza"));
        dataTermineColumn.setCellValueFactory(new PropertyValueFactory<>("FineRinnovo"));
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("Descrizione"));
        frequenzaColumn.setCellValueFactory(new PropertyValueFactory<>("Periodicita"));
        statoColumn.setCellValueFactory(new PropertyValueFactory<>("Paga"));

        initializeTableView();
    }

    @FXML
    protected void initializeTableView() {
        try {
            String username = Sessione.getInstance().getUtenteLoggato().getUsername();
            List<SpeseProgrammate> ListaSpese = speseProgrammateControl.getSpeseProgrammateByUsername(username);

            TabellaProgrammazioni.getItems().clear();

            for (SpeseProgrammate spesa : ListaSpese) {
                // Se la data di termine è passata, elimina la spesa programmata
                if(LocalDate.now().isAfter(spesa.getFineRinnovo().toLocalDate())) {
                    speseProgrammateControl.deleteSpesa(spesa.getIdSpesa());
                    continue;
                }

                // Aggiungi la spesa alla tabella
                TabellaProgrammazioni.getItems().add(spesa);

                // Configura l'evento per ogni bottone della colonna "Stato"
                Button bottone = spesa.getPaga();
                if (spesa.getStato()) { // Se la spesa è già stata pagata
                    bottone.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    bottone.setText("Pagato");
                    bottone.setDisable(true); // Disabilita il bottone se già pagato
                } else {
                    if (LocalDate.now().isAfter(spesa.getDataScadenza().toLocalDate())) {
                        bottone.setStyle("-fx-background-color: #C85C5C; -fx-text-fill: white;");
                        bottone.setText("Paga");
                    }
                    bottone.setOnAction(event -> BottonePaga(spesa, bottone));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void BottonePaga(SpeseProgrammate spesa, Button bottone) {
        try {
            boolean pagamentoRiuscito = speseProgrammateControl.pagaSpesa(spesa);

            if (pagamentoRiuscito) {
                bottone.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                bottone.setText("Pagato");
                bottone.setDisable(true); // Disabilita il bottone dopo il pagamento
                initializeTableView();
            } else {
                // Mostra l'alert se il pagamento non è consentito
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Pagamento non consentito");
                alert.setHeaderText("Attenzione!");
                alert.setContentText("Non puoi effettuare il pagamento prima della data di rinnovo.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void newProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/addProgrammazione.fxml", NewProgram, "Crea nuova Spesa programmata");
        initializeTableView();
    }

    @FXML
    public void changeProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/editProgrammazione.fxml", ChangeProgram, "Modifica Spesa programmata");
        initializeTableView();
    }

    @FXML
    public void deleteProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/deleteProgrammazione.fxml", deleteProgram, "Elimina Spesa programmata");
        initializeTableView();
    }
}
