package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import smu.DAO.UtenteDAO;
import smu.DAOImplementation.UtenteDAOimp;
import smu.DTO.Utente;
import smu.Sessione;
import smu.Main;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController extends Controller {

    @FXML private PasswordField passwordField;
    @FXML private TextField usernameField;

    private UtenteDAO userDAO;

    @FXML
    public void initialize() {
        userDAO = new UtenteDAOimp(); // Inizializza il DAO
    }

    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Campi username e/o password sono vuoti.");
            return;
        }

        try {
            // Controlla se l'utente è autenticato
            Utente utente = authenticateUser(username, password);

            if (utente ==null) {
                showError("Username e/o password errati.");
            } else {
                Sessione.getInstance().setUtenteLoggato(utente);
                Main.setRoot("homepage");
            }
        } catch (SQLException e) {
            showError("Si è verificato un errore durante il login.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Utente authenticateUser(String username, String password) throws SQLException {
        return userDAO.checkCredentials(username, password);
    }

}
