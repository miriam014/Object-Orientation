package smu.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import smu.Main;

import java.io.IOException;
import java.util.Optional;

public class MenuController {

    @FXML private Button toggleButton;
    @FXML private Button homepageButton;
    @FXML private Button familyButton;
    @FXML private Button walletButton;
    @FXML private Button transactionsButton;
    @FXML private Button programmazioneButton;
    @FXML private Button logoutButton;
    @FXML private VBox sidePanel;

    private boolean isMenuVisible = false; // Stato del menu inizializzato a false

    @FXML
    public void initialize() {
        sidePanel.setTranslateX(-130);
        toggleButton.setText("☰");
    }

    @FXML
    public void HomepageButton() throws IOException {
        Main.setRoot("homepage");
    }

    @FXML
    public void ProgrammazioneButton() throws IOException {
        Main.setRoot("programmazione");
    }

    @FXML
    public void btnLogout() {
        // Mostra un messaggio di conferma
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler uscire?");

        Optional<ButtonType> result = alert.showAndWait();
        // Se l'utente preme OK, torna alla schermata di login
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Main.setRoot("login");
            } catch (IOException e) {
                e.printStackTrace(); // Gestisci l'eccezione in caso di errore nel caricamento
            }
        }
    }

    @FXML
    public void toggleMenu() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sidePanel);

        if (isMenuVisible) {
            slide.setToX(-sidePanel.getWidth()); // Imposta la larghezza a zero
            toggleButton.setText("☰");

        } else {
            slide.setToX(0); // Ripristina la larghezza
            toggleButton.setText("X");
        }

        slide.play();
        slide.setOnFinished(event -> {
            isMenuVisible = !isMenuVisible;
        });
    }
}
