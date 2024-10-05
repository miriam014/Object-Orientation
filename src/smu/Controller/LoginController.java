package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import smu.Control.LoginControl;
import smu.DTO.Utente; // Importa la classe Utente
import smu.Sessione; // Importa la classe Sessione
import smu.Main;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private LoginControl model;

    // Metodo di inizializzazione chiamato automaticamente da JavaFX
    @FXML
    public void initialize() {
        // Inizializza l'oggetto model
        model = new LoginControl();
    }

    @FXML
    public void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Errore", "Campi username e/o password sono vuoti.");
            return;
        }

        try {
            // Controlla se l'utente è autenticato
            Utente utente = model.authenticateUser(username, password);

            if (utente ==null) {
                showAlert(AlertType.ERROR, "Errore", "Username e/o password errati.");
            } else {
                Sessione.getInstance().setUtenteLoggato(utente);
                Main.showAnotherInterface("homepage");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore durante il login.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
