package smu;

import javafx.animation.TranslateTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Optional;

public class Menu {

    private VBox sidePanel; // Pannello laterale
    private boolean isMenuVisible; // Stato di visibilità del menu
    private Button toggleButton; // Pulsante per mostrare/nascondere il menu

    // Funzione per creare il layout completo
    public BorderPane creaLayout() {
        BorderPane layout = new BorderPane(); // Layout principale

        layout.setTop(creaHBoxSuperiore());
        sidePanel = creaVBoxLaterale();
        layout.setLeft(sidePanel);

        isMenuVisible = false; // Inizialmente il menù è nascosto
        sidePanel.setTranslateX(-sidePanel.getWidth()); // Sposta il menu fuori dallo schermo
        return layout;
    }

    // Funzione per creare l'HBox superiore
    private HBox creaHBoxSuperiore() {
        HBox hbox = new HBox();
        hbox.setPrefHeight(30);
        hbox.setPrefWidth(800);
        hbox.setStyle("-fx-background-color: #008080;");

        // Creazione del pulsante toggle
        toggleButton = new Button("☰");
        toggleButton.setStyle("-fx-background-color: #008080; -fx-text-fill: white;");
        toggleButton.setOnAction(event -> toggleMenu());

        hbox.getChildren().add(toggleButton);

        return hbox;
    }


    // Funzione per creare il VBox laterale
    private VBox creaVBoxLaterale() {
        VBox sidePanel = new VBox();
        sidePanel.setPrefHeight(600); // Imposta l'altezza desiderata
        sidePanel.setPrefWidth(130);   // Imposta la larghezza desiderata
        sidePanel.setStyle("-fx-background-color: #339999;");

        // Creazione dei bottoni nel menù laterale
        Button homepageButton = new Button("Homepage");
        homepageButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #f8f6f6;");
        sidePanel.getChildren().add(homepageButton);

        Button familyButton = new Button("Famiglia");
        familyButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #f8f6f6;");
        sidePanel.getChildren().add(familyButton);

        Button walletButton = new Button("Portafoglio");
        walletButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #f8f6f6;");
        sidePanel.getChildren().add(walletButton);

        Button transactionsButton = new Button("Transazioni");
        transactionsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #f8f6f6;");
        sidePanel.getChildren().add(transactionsButton);

        Button programmingButton = new Button("Programmazione");
        programmingButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #f8f6f6;");
        sidePanel.getChildren().add(programmingButton);

        // Creazione della linea di separazione
        Line separator = new Line();
        separator.setEndX(130);
        separator.setStroke(Color.WHITE);
        sidePanel.getChildren().add(separator);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #38A6A6; -fx-text-fill: #f8f3f3;");
        logoutButton.setOnAction(event -> btnLogout()); // Aggiungi gestore evento
        sidePanel.getChildren().add(logoutButton);

        return sidePanel;
    }

    // Funzione per gestire il toggle del menu
    private void toggleMenu() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sidePanel); // Crea la transizione

        if (isMenuVisible) {
            slide.setToX(-sidePanel.getWidth()); // Nasconde il pannello
            toggleButton.setText("☰"); // Cambia l'icona del toggle
        } else {
            slide.setToX(0); // Mostra il pannello
            toggleButton.setText("X"); // Cambia l'icona del toggle
        }

        slide.play();
        slide.setOnFinished(event -> { // Aggiungi un listener per l'evento di fine animazione
            isMenuVisible = !isMenuVisible; // Cambia lo stato solo dopo che l'animazione è completata
        });
    }

    // Funzione per gestire il logout
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
                Main.setRoot("login"); // Imposta la dimensione della schermata di login
            } catch (IOException e) {
                e.printStackTrace(); // Gestisci l'eccezione in caso di errore nel caricamento
            }
        }
    }
}
