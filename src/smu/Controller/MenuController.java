package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import smu.Main;

import java.io.IOException;
import java.util.Optional;

public class MenuController {

    @FXML private Button toggleButton;
    @FXML private Button homepageButton;
    @FXML private Button familyButton;
    @FXML private Button walletButton;
    @FXML private Button transactionsButton;
    @FXML private Button programmingButton;
    @FXML private Button logoutButton;
    @FXML private VBox sidePanel; // Riferimento al pannello laterale

    private boolean isMenuVisible = true; // Stato del menu

    @FXML
    public void initialize() {
        isMenuVisible = false; // Inizialmente il menù è nascosto
        //  listener per la larghezza del sidePanel e applica la traslazione dopo il rendering
        sidePanel.widthProperty().addListener((observable, oldValue, newValue) -> {
            sidePanel.setTranslateX(-newValue.doubleValue()); // Posiziona il pannello fuori schermo
        });

        toggleButton.setText("☰");
    }

    @FXML
    public void handleHomepageButton() throws IOException {
        Main.setRoot("homepage");
    }


    @FXML
    public void btnLogout() {
        // Mostra un messaggio di conferma
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler uscire?");
        Optional<ButtonType> result = alert.showAndWait();
        // Se l'utente preme OK, torna alla schermata di login
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Torna alla schermata di login
                Main.setRoot("login"); // Imposta la dimensione della schermata di login
            } catch (IOException e) {
                e.printStackTrace(); // Gestisci l'eccezione in caso di errore nel caricamento
            }
        }
    }

    @FXML
    public void toggleMenu() {
        // Logica per mostrare/nascondere il menu
        if (isMenuVisible) {
            sidePanel.setVisible(false); // Nasconde il menu
            sidePanel.setPrefWidth(0); // Imposta la larghezza a zero
            toggleButton.setText("☰");

        } else {
            sidePanel.setVisible(true); // Mostra il menu
            sidePanel.setPrefWidth(131); // Ripristina la larghezza        toggleButton.setText("X");
            toggleButton.setText("X");
        }
        isMenuVisible = !isMenuVisible; // Inverte lo stato del menu
    }
}
