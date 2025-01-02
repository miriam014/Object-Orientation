package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import smu.Main;
import smu.Sessione;
import smu.DTO.Utente;
import smu.DTO.Carta;
import smu.DTO.Transazione;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DAO_Implementation.CartaDAOimp;


import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.List;

public class HomepageController extends Controller {

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
    private Button statisticaButton; // Pulsante per passare al report
    @FXML
    private ListView<String> transactionsListView;


    private List<Carta> carteUtente; // Lista delle carte dell'utente
    private int currentCardIndex; // Indice della carta corrente che si sta visualizzando

    @FXML
    public void initialize() {

        Utente utente = Sessione.getInstance().getUtenteLoggato();// Recupera l'utente loggato
        System.out.println("Utente loggato: " + utente); // Debug
        if (utente != null) {
            loadUserCards(); // Carica le carte dell'utente
            showCard(); // Mostra la carta attuale
        } else {
            System.out.println("Utente non trovato"); // Debug
        }
    }

    private void loadUserCards() {
        try {
            carteUtente = Sessione.getInstance().getCarteUtente();
            if (carteUtente == null || carteUtente.isEmpty()) {
                System.out.println("Nessuna carta trovata per l'utente."); // Debug
            } else {
                if(carteUtente.size() > 1) {
                    nextCardButton.setVisible(true); // Mostra il pulsante per la prossima carta
                    statisticaButton.setVisible(true); // Mostra il pulsante per le statistiche
                }
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
            loadTransactions(carta.getNumeroCarta()); // Carica le transazioni per la carta corrente
        }
        else {
            System.out.println("Nessuna carta da mostrare.");
        }
    }

    private void loadTransactions(String cardNumber) {
        try {
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            List<Transazione> transazioni = transazioneDAO.getByCardNumber(cardNumber, "tutte"); // Recupera le transazioni per la carta

            ObservableList<String> transactionDetails = FXCollections.observableArrayList(); // Crea una lista osservabile per le transazioni

            for (Transazione transazione : transazioni) {
                String directionArrow = transazione.getTipoTransazione().equals("entrata") ? "🟢 →" : "🔴 ←";
                String transactionInfo = directionArrow + transazione.getImporto() + " € - "
                        + transazione.getCausale() + " - "
                        + (transazione.getTipoTransazione().equals("Entrata") ? "Da: " + transazione.getMittente() : "A: " + transazione.getDestinatario());

                transactionDetails.add(transactionInfo);
            }
            transactionsListView.setItems(transactionDetails); // Imposta le transazioni nella ListView

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextCard() {
        System.out.println("Next card button clicked!"); // Verifica che questo venga eseguito
        currentCardIndex++; // Incrementa l'indice della carta
        if (currentCardIndex >= carteUtente.size()) { // Se l'indice supera il numero di carte
            currentCardIndex = 0; // Torna alla prima carta
        }
        showCard(); // Mostra la carta corrente
    }

    @FXML
    private void scenaReport() {
        try {
            Carta cartaSelezionata = carteUtente.get(currentCardIndex); // Recupera la carta selezionata
            Sessione.getInstance().setCartaSelezionata(cartaSelezionata); // Imposta la carta selezionata
            Main.setRoot("report"); // Imposta la root della scena del report
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
