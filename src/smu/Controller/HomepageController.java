package smu.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import smu.Sessione;
import smu.DTO.Utente;
import smu.Main;

import java.io.IOException;
import java.util.Optional;

public class HomepageController {

    public Button logoutButton;
    @FXML
    private Button toggleButton; // Il pulsante per mostrare/nascondere
    @FXML
    private VBox sidePanel; // Il pannello laterale
    @FXML
    private Label welcomeLabel; // Etichetta di benvenuto


    private boolean isMenuVisible = true; // Inizialmente il menù è nascosto

    @FXML
    public void initialize() {
        // Imposta il pannello laterale all'inizio fuori dallo schermo
        sidePanel.setTranslateX(0);
        toggleButton.setText("X");//il menu parte aperto

        Utente utente = Sessione.getInstance().getUtenteLoggato();
        if (utente != null) {
            setWelcomeLabel(utente);
        }
    }

    public void setWelcomeLabel(Utente nome) {
        welcomeLabel.setText("Lieti di rivederla, " + nome.getNome());
    }

    @FXML
    private void toggleMenu() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sidePanel); // Crea la transizione


        if (isMenuVisible) {
            slide.setToX(-sidePanel.getWidth()); // Nasconde il pannello
            transformToBurger();
        } else {
            slide.setToX(0); // Mostra il pannello
            transformToX();
        }

        slide.play();
        slide.setOnFinished(event -> {
            isMenuVisible = !isMenuVisible; // Cambia lo stato solo dopo che l'animazione è completata
        });
    }

    private void transformToX() {
        toggleButton.setText("X");
    }

    private void transformToBurger() {
        toggleButton.setText("☰");
    }

    @FXML
    private void btnLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma di Logout");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler uscire?");

        Optional<ButtonType> result = alert.showAndWait();
        // Se l'utente preme OK, torna alla schermata di login
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Torna alla schermata di login
                Main.setRoot("login", 400, 350); // Imposta la dimensione della schermata di login
            } catch (IOException e) {
                e.printStackTrace(); // Gestisci l'eccezione in caso di errore nel caricamento
            }
        }
    }
}
