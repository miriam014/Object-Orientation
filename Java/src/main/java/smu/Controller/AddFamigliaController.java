package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.DTO.Utente;
import smu.Sessione;

import java.sql.SQLException;

public class AddFamigliaController extends Controller{
    @FXML public TextField nomeFamiglia;
    @FXML public Button ConfermaNew;
    @FXML public TextField usernameNuovoUtente;


    public void createFamily(ActionEvent actionEvent) {
        if (nomeFamiglia.getText().trim().isEmpty()) {
            showError("Inserire un nome per la famiglia.");
            return;
        }

        try {
            // Creazione dell'oggetto Famiglia
            smu.DTO.Famiglia famiglia = new smu.DTO.Famiglia(null, nomeFamiglia.getText());

            smu.DAOImplementation.FamigliaDAOimp famigliaDAOimp = new smu.DAOImplementation.FamigliaDAOimp();
            boolean success = famigliaDAOimp.insert(famiglia);

            if (success) {
                Utente utente = Sessione.getInstance().getUtenteLoggato();
                System.out.println("Famiglia creata con successo");

                addUserToFamily(famiglia.getIdFamiglia(), utente.getUsername());
                if(!usernameNuovoUtente.getText().trim().isEmpty()) {
                    addUserToFamily(famiglia.getIdFamiglia(), usernameNuovoUtente.getText());
                }

                Stage stage = (Stage) ConfermaNew.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("Errore nella creazione della famiglia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void addUserToFamily(Integer idFamiglia, String username) {
        smu.DAOImplementation.UtentiInFamiglieDAOimp utentiInFamiglieDAOimp = new smu.DAOImplementation.UtentiInFamiglieDAOimp();

        try {
            utentiInFamiglieDAOimp.addUserToFamily(idFamiglia, username);
            Sessione.getInstance().loadUserFamily();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
