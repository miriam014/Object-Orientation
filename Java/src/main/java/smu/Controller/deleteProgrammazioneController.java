package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAO_Implementation.SpeseProgrammateDAOimp;
import smu.DTO.Carta;
import smu.DTO.SpeseProgrammate;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class deleteProgrammazioneController extends Controller {
    @FXML private Button Conferma;
    @FXML private ComboBox<String> nomeProgrammazione;
    private SpeseProgrammateDAO speseProgrammateDAO;


    @FXML
    public void initialize() throws SQLException {
        speseProgrammateDAO = new SpeseProgrammateDAOimp();
        String username = Sessione.getInstance().getUtenteLoggato().getUsername();
        List<SpeseProgrammate> ListaSpese = speseProgrammateDAO.getByUsername(username);

        for (SpeseProgrammate spesa : ListaSpese) {
            nomeProgrammazione.getItems().add(spesa.getDescrizione());
        }
    }

    public void deleteProgrammazione(ActionEvent actionEvent) {
        String nomeProgrammazioneValue = (String) nomeProgrammazione.getValue();
        SpeseProgrammate spesaSelezionata = null;

        try {
            String username = Sessione.getInstance().getUtenteLoggato().getUsername();
            List<SpeseProgrammate> ListaSpese = speseProgrammateDAO.getByUsername(username);

            for (SpeseProgrammate spesa : ListaSpese) {
                if (spesa.getDescrizione().equals(nomeProgrammazioneValue)) {
                    spesaSelezionata = spesa;
                    break;
                }
            }

            if (spesaSelezionata != null) {
                Integer idSpesa = Integer.valueOf(spesaSelezionata.getIdSpesa());
                speseProgrammateDAO.delete(idSpesa);
                Stage stage = (Stage) Conferma.getScene().getWindow();
                stage.close();
            }  else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Spesa programmata non trovata.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
