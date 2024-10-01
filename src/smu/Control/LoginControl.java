package smu.Control;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import smu.DAO.UtenteDAO;
import smu.DAO_Implementation.UtenteDAOimp;
import smu.DTO.Utente;

import smu.Main;
import java.sql.SQLException;
import java.io.IOException;


public class LoginControl {

    @FXML
    PasswordField passwordField;
    @FXML
    TextField emailField;

    @FXML
    public void login() {

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Campi email e/o password sono vuoti.");
        } else {
            // chiamata al DAO
            try {
                UtenteDAO userDao = new UtenteDAOimp();
                Utente user = userDao.checkCredentials(email, password);

                if (user == null) {
                    showAlert(AlertType.ERROR, "Errore", "Email e/o password errati.");
                } else {
                    Main.setRoot("Homepage");
                }
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Errore", "Si è verificato un errore durante il login.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
