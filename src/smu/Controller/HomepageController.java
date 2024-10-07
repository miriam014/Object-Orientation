package smu.Controller;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.control.ListView;
import smu.Sessione;
import smu.DTO.Utente;
import smu.DTO.Carta;
import smu.Main;

import java.io.IOException;
import java.util.Optional;
import java.util.List;

public class HomepageController {

    @FXML
    public Button logoutButton; // Il pulsante per il logout
    @FXML
    private Button toggleButton; // Il pulsante per mostrare/nascondere
    @FXML
    private VBox sidePanel; // Il pannello laterale
    @FXML
    private Label welcomeLabel; // Etichetta di benvenuto
    @FXML
    private Label balanceLabel; // Etichetta del saldo
    @FXML
    private Label cardNameLabel; // Etichetta del numero della carta
    @FXML
    private Label cardNumberLabel; // Etichetta del nome della carta
    @FXML
    private Label cardTypeLabel; // Etichetta per il tipo di carta
    @FXML
    private Label expiryDateLabel; // Etichetta per la data di scadenza
    @FXML
    private Button nextCardButton; // Pulsante per la prossima carta
    @FXML
    private ListView<String> transactionsListView;


    private boolean isMenuVisible; // Inizialmente il menù è nascosto
    private List<Carta> carteUtente; // Lista delle carte dell'utente
    private int currentCardIndex; // Indice della carta corrente che si sta visualizzando

    @FXML
    public void initialize() {
        isMenuVisible = false; // Inizialmente il menù è nascosto
        //  listener per la larghezza del sidePanel e applica la traslazione dopo il rendering
        sidePanel.widthProperty().addListener((observable, oldValue, newValue) -> {
            sidePanel.setTranslateX(-newValue.doubleValue()); // Posiziona il pannello fuori schermo
        });

        toggleButton.setText("☰");

        Utente utente = Sessione.getInstance().getUtenteLoggato();// Recupera l'utente loggato
        System.out.println("Utente loggato: " + utente); // Debug
        if (utente != null) {
            setWelcomeLabel(utente);
            loadUserCards(); // Carica le carte dell'utente
            showCard(); // Mostra la carta attuale
        } else {
            System.out.println("Utente non trovato"); // Debug
    }   }

    public void setWelcomeLabel(Utente nome) {
        welcomeLabel.setText("Lieti di rivederla, " + nome.getNome());
    }

    private void loadUserCards() {
        try {
            carteUtente = Sessione.getInstance().getCarteUtente();
            if (carteUtente == null || carteUtente.isEmpty()) {
                System.out.println("Nessuna carta trovata per l'utente."); // Debug
            } else {
                System.out.println("Carte caricate: " + carteUtente); // Debug
            }
            currentCardIndex = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCard() {
        if (carteUtente != null && !carteUtente.isEmpty()) {
            if (currentCardIndex >= carteUtente.size()) {
                System.out.println("Indice della carta supera la dimensione della lista.");
                return;
            }
         // Verifica che la lista delle carte non sia vuota e che l'indice sia valido
            Carta carta = carteUtente.get(currentCardIndex); // Recupera la carta corrente
            balanceLabel.setText(String.valueOf(carta.getSaldo())); // Imposta il saldo
            cardNameLabel.setText(carta.getNomeCarta()); // Imposta il nome della carta
            cardNumberLabel.setText("Numero Carta: **** **** **** " + carta.getNumeroCarta().substring(carta.getNumeroCarta().length() - 4)); // Mostra solo gli ultimi 4 numeri
            expiryDateLabel.setText(carta.getScadenza().toString()); // Imposta la data di scadenza
            cardTypeLabel.setText(carta.getTipoCarta()); // Imposta il tipo di carta
        }
        else {
            System.out.println("Nessuna carta da mostrare.");
        }
    }
    @FXML
    private void toggleMenu() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sidePanel); // Crea la transizione


        if (isMenuVisible) {
            slide.setToX(0); // Nasconde il pannello
            transformToBurger();
        } else {
            slide.setToX(sidePanel.getWidth()); // Mostra il pannello
            transformToX();
        }

        slide.play();
        slide.setOnFinished(event -> { // Aggiungi un listener per l'evento di fine animazione
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

    @FXML
    private void handleNextCard() {
        currentCardIndex++; // Incrementa l'indice della carta
        if (currentCardIndex >= carteUtente.size()) { // Se l'indice supera il numero di carte
            currentCardIndex = 0; // Torna alla prima carta
        }
        showCard(); // Mostra la carta corrente
    }
}
