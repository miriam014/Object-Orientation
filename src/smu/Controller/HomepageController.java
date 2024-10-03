package smu.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HomepageController {

    @FXML
    private Button toggleButton; // Il pulsante per mostrare/nascondere
    @FXML
    private VBox sidePanel; // Il pannello laterale

    private boolean isMenuVisible = true; // Inizialmente il menù è nascosto

    @FXML
    public void initialize() {
        // Imposta il pannello laterale all'inizio fuori dallo schermo
        sidePanel.setTranslateX(0);
        toggleButton.setText("X");//il menu parte aperto
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
}
