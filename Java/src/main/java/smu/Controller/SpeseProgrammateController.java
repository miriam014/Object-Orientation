package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.Sessione;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAO_Implementation.SpeseProgrammateDAOimp;
import smu.DTO.SpeseProgrammate;

import java.sql.SQLException;
import java.util.List;

public class SpeseProgrammateController extends Controller {

    private List<SpeseProgrammate> programmazioni;

    @FXML public TableColumn<SpeseProgrammate, String> destinatarioColumn;
    @FXML private TableView<SpeseProgrammate> TabellaProgrammazioni;
    @FXML private TableColumn<SpeseProgrammate, Boolean> statoColumn;
    @FXML private TableColumn<SpeseProgrammate, String> nomeColumn;
    @FXML private TableColumn<SpeseProgrammate, String> cartaColumn;
    @FXML private TableColumn<SpeseProgrammate, Float> importoColumn;
    @FXML private TableColumn<SpeseProgrammate, String> dataColumn;
    @FXML private TableColumn<SpeseProgrammate, String> causaleColumn;
    @FXML private TableColumn<SpeseProgrammate, String> frequenzaColumn;

    private SpeseProgrammateDAO speseProgrammateDAO;
    @FXML private Button NewProgram;
    @FXML private Button ChangeProgram;


    @FXML
    public void initialize() {
        speseProgrammateDAO = new SpeseProgrammateDAOimp();

        // Imposta i valori per le colonne della tabella
        nomeColumn.setCellValueFactory(new PropertyValueFactory<>("Destinatario"));
        cartaColumn.setCellValueFactory(new PropertyValueFactory<>("NumeroCarta"));
        importoColumn.setCellValueFactory(new PropertyValueFactory<>("Importo"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("DataScadenza"));
        causaleColumn.setCellValueFactory(new PropertyValueFactory<>("Descrizione"));
        frequenzaColumn.setCellValueFactory(new PropertyValueFactory<>("Periodicita"));
        statoColumn.setCellValueFactory(new PropertyValueFactory<>("Stato"));

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
                Button bottone = spesa.getStato();
                bottone.setOnAction(event -> {
                    BottonePaga(spesa, bottone);
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void BottonePaga(SpeseProgrammate spesa, Button bottone) {
        System.out.println("Pagamento spesa programmata: " + spesa.getIdSpesa());
        bottone.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        bottone.setText("Pagato");
        bottone.setDisable(true);   //in modo che non venga pagata di nuovo la spesa prima del dovuto

        //riattivo il bottone una volta passato il tempo necessario in base alla data di scadenza

    }

}
