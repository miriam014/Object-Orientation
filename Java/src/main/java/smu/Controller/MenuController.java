package smu.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Duration;
import smu.DTO.Carta;
import smu.Main;
import smu.Sessione;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class MenuController {


    @FXML private Button toggleButton;
    @FXML private Button scadenzeButton;
    @FXML private Button homepageButton;
    @FXML private Button portafoglioButton;
    @FXML private Button famigliaButton;
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

        // Aggiungi un listener alla scena principale
        sidePanel.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnMouseClicked(event -> {
                    // Chiudi il menu solo se il clic NON è su sidePanel o toggleButton
                    if (!sidePanel.isHover() && !toggleButton.isHover()) {
                        if (isMenuVisible) {
                            toggleMenu();
                        }
                    }
                });
            }
        });
    }

    @FXML
    public void HomepageButton() throws IOException {
        Main.setRoot("homepage");
    }
    
    @FXML
    public void ReportButton() throws IOException {
        List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
        if(!carteUtente.isEmpty()) {
            Main.setRoot("report");
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Non hai carte associate al tuo account.");
            alert.showAndWait();
        }
    }

    @FXML
    public void ScadenzeButton() throws IOException {
        Main.setRoot("speseProgrammate");
    }

   @FXML void portafoglioButton() throws IOException {
        Main.setRoot("portafoglio");
    }

    @FXML void famigliaButton() throws IOException {
        Main.setRoot("famiglia");
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
            slide.setToX(20); // Ripristina la larghezza
            toggleButton.setText("X");
        }

        slide.play();
        slide.setOnFinished(event -> {
            isMenuVisible = !isMenuVisible;
        });
    }


    @FXML
    public void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori della VBox, chiudi il menu richiama il metodo del toggle
        if (event.getTarget() != sidePanel) {
            toggleMenu();
        }
    }

}
