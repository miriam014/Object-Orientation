package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import smu.DTO.Portafoglio;
import smu.DTO.Utente;
import smu.Sessione;

import java.util.List;

public class PortafoglioController {

    @FXML
    private Label balanceLabel;
    @FXML
    private Label walletNameLabel;

    private List<Portafoglio> portafogliUtente; // Lista delle carte dell'utente
    private int currentWalletIndex;

    @FXML
    public void initialize() {

        Utente utente = Sessione.getInstance().getUtenteLoggato();// Recupera l'utente loggato
        System.out.println("Utente loggato: " + utente); // Debug
        if (utente != null) {
            loadUserWallet();
            showCard(); // Mostra la carta attuale
        } else {
            System.out.println("Utente non trovato"); // Debug
        }
    }

    private void loadUserWallet() {
        try {
            portafogliUtente = Sessione.getInstance().getPortafogliUtente();
            if (portafogliUtente == null || portafogliUtente.isEmpty()) {
                System.out.println("Nessuna carta trovata per l'utente."); // Debug
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


    private void showCard() {
        if (portafogliUtente != null && !portafogliUtente.isEmpty()) {
            if (currentWalletIndex >= portafogliUtente.size()) {
                System.out.println("Indice della carta supera la dimensione della lista.");
                return;
            }

            Portafoglio portafoglio = portafogliUtente.get(currentWalletIndex);
            balanceLabel.setText(String.valueOf(portafoglio.getSaldo()));
            walletNameLabel.setText(portafoglio.getNomePortafoglio());
        }
        else {
            System.out.println("Nessuna carta da mostrare.");
        }
    }
}
