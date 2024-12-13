package smu.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.DTO.Utente;
import smu.Sessione;

import java.util.List;

public class PortafoglioController {

    @FXML
    private Label balanceLabel;
    @FXML
    private Label walletNameLabel;
    @FXML
    private ListView<String> transactionsListView;

    private List<Portafoglio> portafogliUtente; // Lista delle carte dell'utente
    private int currentWalletIndex;


    @FXML
    public void initialize() {

        Utente utente = Sessione.getInstance().getUtenteLoggato();// Recupera l'utente loggato
        System.out.println("Utente loggato: " + utente); // Debug
        if (utente != null) {
            loadUserWallet();
            showWallet(); // Mostra la carta attuale
        } else {
            System.out.println("Utente non trovato"); // Debug
        }
    }

    private void loadUserWallet() {
        try {
            portafogliUtente = Sessione.getInstance().getPortafogliUtente();
            if (portafogliUtente == null || portafogliUtente.isEmpty()) {
                System.out.println("Nessun portafoglio trovato per l'utente."); // Debug
            } else {
                /*if(portafogliUtente.size() > 1) {
                    nextCardButton.setVisible(true); // Mostra il pulsante per la prossima carta
                    statisticaButton.setVisible(true); // Mostra il pulsante per le statistiche
                }*/
            }
            currentWalletIndex = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWallet() {
        if (portafogliUtente != null && !portafogliUtente.isEmpty()) {
            if (currentWalletIndex >= portafogliUtente.size()) {
                System.out.println("Indice del portafoglio supera la dimensione della lista.");
                return;
            }

            Portafoglio portafoglio = portafogliUtente.get(currentWalletIndex);
            balanceLabel.setText(String.valueOf(portafoglio.getSaldo()));
            walletNameLabel.setText(portafoglio.getNomePortafoglio());
            loadTransactions(portafoglio.getIdPortafoglio());
        }
        else {
            System.out.println("Nessun portafoglio da mostrare.");
        }
    }
    
    private void loadTransactions(String walletId) {
        try {
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            List<Transazione> transazioni = transazioneDAO.getByWalletId(walletId); // Recupera le transazioni per il portafoglio

            ObservableList<String> transactionDetails = FXCollections.observableArrayList(); // Crea una lista osservabile per le transazioni

            for (Transazione transazione : transazioni) {
                String directionArrow = transazione.getTipoTransazione().equals("entrata") ? "🟢 →" : "🔴 ←";
                String transactionInfo = directionArrow + transazione.getImporto() + " € - "
                        + transazione.getCausale() + " - "
                        + (transazione.getTipoTransazione().equals("Entrata") ? "Da: " + transazione.getMittente() : "A: " + transazione.getDestinatario())
                        + " "+ transazione.getNumeroCarta() + " "+ transazione.getCategoria();

                transactionDetails.add(transactionInfo);
            }
            transactionsListView.setItems(transactionDetails); // Imposta le transazioni nella ListView

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
