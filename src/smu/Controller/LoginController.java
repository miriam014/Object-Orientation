package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import smu.Control.LoginControl;
import smu.Main;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField emailField;

    private LoginControl model;

    public LoginController() {
        model = new LoginControl(); // Inizializza il modello
    }

    @FXML
    public void login() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Errore", "Campi email e/o password sono vuoti.");
            return;
        }

        try {
            boolean isAuthenticated = model.authenticateUser(email, password);

            if (!isAuthenticated) {
                showAlert(AlertType.ERROR, "Errore", "Email e/o password errati.");
            } else {
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
