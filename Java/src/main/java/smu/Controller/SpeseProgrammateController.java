package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.Sessione;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAO_Implementation.SpeseProgrammateDAOimp;
import smu.DTO.SpeseProgrammate;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;

import static smu.Database.getConnection;

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
                TabellaProgrammazioni.getItems().add(spesa);

                // Configura l'evento per ogni bottone della colonna "Stato"
                Button bottone = spesa.getPaga();
                if (spesa.getStato()) {
                    bottone.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    bottone.setText("Pagato");
                    bottone.setDisable(true); // Disabilita il bottone se già pagato
                } else {
                    bottone.setOnAction(event -> BottonePaga(spesa, bottone));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void BottonePaga(SpeseProgrammate spesa, Button bottone) {
        System.out.println("Pagamento spesa programmata: " + spesa.getIdSpesa());
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
            }
        } catch (SQLException e) {
                e.printStackTrace();
        }
    }


    @FXML
    public void newProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/newProgrammazione.fxml", NewProgram, "Crea nuova Spesa programmata");
        initializeTableView();
    }

    @FXML
    public void changeProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/changeProgrammazione.fxml", ChangeProgram, "Modifica Spesa programmata");
        initializeTableView();
    }

    @FXML
    public void deleteProgrammazione(ActionEvent actionEvent) {
        showDialog("/interfaccia/deleteProgrammazione.fxml", deleteProgram, "Elimina Spesa programmata");
        initializeTableView();
    }
}
