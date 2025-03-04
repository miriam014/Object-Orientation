package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.Sessione;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAOImplementation.SpeseProgrammateDAOimp;
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

    private SpeseProgrammateDAO speseProgrammateDAO;
    @FXML private Button deleteProgram;
    @FXML private Button NewProgram;
    @FXML private Button ChangeProgram;


    @FXML
    public void initialize() {
        speseProgrammateDAO = new SpeseProgrammateDAOimp();

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
            List<SpeseProgrammate> ListaSpese = speseProgrammateDAO.getByUsername(username);

            TabellaProgrammazioni.getItems().clear();

            for (SpeseProgrammate spesa : ListaSpese) {
                //se la data di termine è passata, elimino la spesa programmata
                if( LocalDate.now().isAfter(spesa.getFineRinnovo().toLocalDate())){
                    speseProgrammateDAO.delete(spesa.getIdSpesa());
                    continue;
                }

                // Aggiunge la spesa alla tabella se non è stata eliminata prima
                TabellaProgrammazioni.getItems().add(spesa);


                // Configura l'evento per ogni bottone della colonna "Stato"
                Button bottone = spesa.getPaga();
                if (spesa.getStato()) { //se la spesa era già stata pagata la segna come tale
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
        // Ottieni la data di rinnovo dalla spesa
        LocalDate dataRinnovo = spesa.getDataScadenza().toLocalDate();
        LocalDate dataAttuale = LocalDate.now(); // Data di oggi

        if(dataAttuale.isAfter(dataRinnovo) || dataAttuale.isEqual(dataRinnovo)) {
            bottone.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            bottone.setText("Pagato");
            bottone.setDisable(true);   //in modo che non venga pagata di nuovo la spesa prima del dovuto

            // Aggiorna lo stato della spesa
            spesa.setStato(true);

            try {
                // Usa l'oggetto DAO per aggiornare il database
                boolean updated = speseProgrammateDAO.update(spesa);
                if (!updated) {
                    System.out.println("Errore durante l'update nel database.");
                } else {
                    System.out.println("Pagamento spesa programmata: " + spesa.getIdSpesa());
                }
                initializeTableView();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Se il pagamento non è consentito, mostra un alert all'utente
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Pagamento non consentito");
            alert.setHeaderText("Attenzione!");
            alert.setContentText("La data attuale è precedente alla data di inizio rinnovo.\n"
                    + "Non puoi effettuare il pagamento in questo momento:  attendere la data di inizio rinnovo.");
            alert.showAndWait();

            // Se la data attuale è maggiore o uguale alla data di rinnovo, non permettere il pagamento
            System.out.println("Pagamento non consentito. La data attuale è precedente alla data di inizio rinnovo.");
            bottone.setDisable(true);
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
