package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import smu.DAO.FamigliaDAO;
import smu.DAO_Implementation.FamigliaDAOimp;
import smu.DAO_Implementation.UtentiInFamiglieDAOimp;
import smu.DTO.Famiglia;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class LeaveFamigliaController extends FamigliaController{
    @FXML public ComboBox<String> nomeFamiglia;
    @FXML private Button Conferma;

    private FamigliaDAO famigliaDAO;


    @FXML
    public void initialize() {
        famigliaDAO = new FamigliaDAOimp();
        try{
            String username = Sessione.getInstance().getUtenteLoggato().getUsername();
            List<Famiglia> ListaFamiglie = famigliaDAO.getByUsername(username);

            for (Famiglia spesa : ListaFamiglie) {
                nomeFamiglia.getItems().add(spesa.getNomeFamiglia());
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void leaveFamiglia(ActionEvent actionEvent) {
        String famigliaValue = nomeFamiglia.getValue();
        Famiglia famigliaSelezionata = null;
        String username = Sessione.getInstance().getUtenteLoggato().getUsername();

        try {
            //trova la famiglia selezionata
            List<Famiglia> ListaFamiglia = famigliaDAO.getByUsername(username);
            for (Famiglia spesa : ListaFamiglia) {
                if (spesa.getNomeFamiglia().equals(famigliaValue)) {
                    famigliaSelezionata = spesa;
                    break;
                }
            }

            if (famigliaSelezionata != null) {
                Integer idFamiglia = famigliaSelezionata.getIdFamiglia();
                UtentiInFamiglieDAOimp utentiInFamilyDAO = new UtentiInFamiglieDAOimp();

                // 1. Rimuovi l'utente dalla famiglia
                utentiInFamilyDAO.removeUserFromFamily(username, idFamiglia);
                // 2. Controlla se la famiglia ha ancora utenti associati
                List<String> utentiFamiglia = utentiInFamilyDAO.getUsersByFamilyId(idFamiglia);
                if (utentiFamiglia.isEmpty()) {
                    // Elimina la famiglia se non ha più utenti
                    famigliaDAO.delete(idFamiglia);
                }

                // 2. Aggiornare la sessione
                Sessione.getInstance().loadUserFamily(); // Metodo da implementare in Sessione

                Stage stage = (Stage) Conferma.getScene().getWindow();
                stage.close();
            }  else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Errore");
                alert.setHeaderText(null);
                alert.setContentText("Famiglia non selezionata.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
