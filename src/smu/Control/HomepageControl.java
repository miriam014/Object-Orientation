package smu.Control;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class HomepageControl {

    public Button toggleButton;
    @FXML
    private VBox sidePanel;

    private boolean isMenuVisible = true;

    @FXML
    private void toggleMenu() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.millis(300));
        slide.setNode(sidePanel);

        if (isMenuVisible) {
            slide.setToX(-sidePanel.getWidth()); // Nasconde il pannello
        } else {
            slide.setToX(0); // Mostra il pannello
        }

        slide.play();
        isMenuVisible = !isMenuVisible; // Cambia lo stato
    }
}
