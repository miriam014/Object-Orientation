package smu.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO_Implementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Carta;
import smu.DTO.Famiglia;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.Sessione;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PortafoglioController extends Controller {
    @FXML public Button nextWalletButton;
    @FXML public Button previousWalletButton;
    @FXML protected Button addWalletButton;
    @FXML protected Button editWalletButton;
    @FXML protected Button deleteWalletButton;
    @FXML public Button addTransactionInWalletButton;
    @FXML private Label balanceLabel;
    @FXML protected Label walletNameLabel;
    @FXML private Label currentWalletID;
    @FXML private Label currentCardNumber;
    @FXML private TableView<Transazione> transactionsTableView;

    @FXML protected TextField nomePortafoglio;
    @FXML protected ComboBox<String> IdFamiglia;
    @FXML protected ComboBox<String> IdPortafoglio;
    @FXML protected ComboBox<String> NumeroCarta;
    @FXML protected Button Conferma;

    protected List<Portafoglio> personalWallets;
    private int currentWalletIndex;


    @FXML
    public void initialize() throws SQLException {
        initializeTableView();
        loadUserWallet();
        showWallet();
    }

    protected void loadUserWallet() {
        try {
            personalWallets = Sessione.getInstance().getPersonalWallets();
            if (personalWallets == null || personalWallets.isEmpty()) {
                System.out.println("Nessun portafoglio trovato per l'utente."); // Debug
            } else {
                List<String> walletsID = new ArrayList<>();
                System.out.println("Portafogli trovati: " + personalWallets.size());

                // Aggiungi gli ID delle famiglie alla lista
                for (Portafoglio portafoglio : personalWallets) {
                    walletsID.add(portafoglio.getIdPortafoglio());
                }
                if(IdPortafoglio != null)
                    IdPortafoglio.getItems().setAll(walletsID);
            }
            currentWalletIndex = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void loadUserCards(){
        try {
            List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
            if (carteUtente == null || carteUtente.isEmpty()) {
                System.out.println("Nessuna carta trovata per l'utente.");
            } else {
                List<String> cardsNumber = new ArrayList<>();
                for (Carta carta : carteUtente) {
                    cardsNumber.add(carta.getNumeroCarta());
                }
                NumeroCarta.getItems().setAll(cardsNumber);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void showWallet() throws SQLException {
        if (personalWallets != null && !personalWallets.isEmpty()) {
            if (currentWalletIndex >= personalWallets.size()) {
                System.out.println("Indice del portafoglio supera la dimensione della lista.");
                return;
            }

            Portafoglio portafoglio = personalWallets.get(currentWalletIndex);
            currentWalletID.setText(personalWallets.get(currentWalletIndex).getIdPortafoglio());
            balanceLabel.setText(portafoglio.getSaldo() != 0 ? String.valueOf(portafoglio.getSaldo()) : "0.00");
            walletNameLabel.setText(portafoglio.getNomePortafoglio());

            AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
            String numeroCarta = associazione.getCardNumberByID(personalWallets.get(currentWalletIndex).getIdPortafoglio());
            currentCardNumber.setText(numeroCarta);

            loadTransactions(portafoglio.getIdPortafoglio());
        }
        else {
            System.out.println("Nessun portafoglio da mostrare.");
        }
    }

    private void loadTransactions(String walletId) {
        try {
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            List<Transazione> transazioni = transazioneDAO.getByWalletId(walletId);

            ObservableList<Transazione> transactionDetails = FXCollections.observableArrayList(transazioni);
            transactionsTableView.setItems(transactionDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextWallet() throws SQLException {
        System.out.println("Next wallet button clicked!"); // Verifica che questo venga eseguito
        currentWalletIndex++; // Incrementa l'indice della carta
        if (currentWalletIndex >= personalWallets.size()) { // Se l'indice supera il numero di carte
            currentWalletIndex = 0; // Torna alla prima carta
        }
        showWallet();
    }

    @FXML
    private void handlePreviousWallet() throws SQLException {
        System.out.println("Previous wallet button clicked!");
        if (currentWalletIndex == 0)
            currentWalletIndex = personalWallets.size() -1;
        else
            currentWalletIndex--;

        currentWalletID.setText(personalWallets.get(currentWalletIndex).getIdPortafoglio());
        showWallet();
    }

    @FXML
    public void insertWallet() throws SQLException {
        showDialog("/interfaccia/addWallet.fxml", addWalletButton, "Nuovo Portafoglio");
        loadUserWallet();
        handlePreviousWallet();
        handleNextWallet();
    }

    @FXML
    public void updateWallet() throws SQLException {
        showDialog("/interfaccia/editWallet.fxml", editWalletButton, "Modifica Portafoglio");
        loadUserWallet();
        handlePreviousWallet();
        handleNextWallet();
    }

    @FXML
    public void deleteWallet() throws SQLException {
        showDialog("/interfaccia/deleteWallet.fxml", deleteWalletButton, "Elimina Portafoglio");
        loadUserWallet();
        handleNextWallet();
    }

    public void insertTransactionInWallet() throws SQLException {
        showDialog("/interfaccia/addTransaction.fxml", addTransactionInWalletButton, "Aggiungi Transazione nel portafoglio");
        loadTransactions(currentWalletID.getText());
    }
}
