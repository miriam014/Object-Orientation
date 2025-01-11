package smu.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Carta;
import smu.DTO.Famiglia;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.Sessione;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PortafoglioController extends Controller {
    @FXML public Button nextWalletButton;
    @FXML protected Button addWalletButton;
    @FXML protected Button editWalletButton;
    @FXML protected Button deleteWalletButton;
    @FXML private Label balanceLabel;
    @FXML protected Label walletNameLabel;
    @FXML private Label currentWalletID;
    @FXML private TableView<Transazione> transactionsTableView;

    @FXML protected TextField nomePortafoglio;
    @FXML protected ComboBox<String> IdFamiglia;
    @FXML protected ComboBox<String> IdPortafoglio;
    @FXML protected ComboBox<String> NumeroCarta;
    @FXML protected Button Conferma;

    protected List<Portafoglio> portafogliUtente;
    private int currentWalletIndex;


    @FXML
    public void initialize() {
        initializeTableView();
        loadUserWallet();
        showWallet();
    }

    protected void loadUserWallet() {
        try {
            portafogliUtente = Sessione.getInstance().getPortafogliUtente();
            if (portafogliUtente == null || portafogliUtente.isEmpty()) {
                System.out.println("Nessun portafoglio trovato per l'utente."); // Debug
            } else {
                List<String> walletsID = new ArrayList<>();

                System.out.println("Portafogli trovati: " + portafogliUtente.size());

                // Aggiungi gli ID delle famiglie alla lista
                for (Portafoglio portafoglio : portafogliUtente) {
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

    @FXML
    protected void loadFamilyID() {
        try {
            // Recupera la lista delle famiglie
            List<Famiglia> famiglieUtente = Sessione.getInstance().getFamilyByUsername();

            if (famiglieUtente == null || famiglieUtente.isEmpty()) {
                System.out.println("Nessuna famiglia trovata per l'utente."); // Debug
            } else {
                // Crea una lista per contenere gli ID delle famiglie
                List<String> familyIds = new ArrayList<>();

                // Aggiungi gli ID delle famiglie alla lista
                for (Famiglia famiglia : famiglieUtente) {
                    familyIds.add(famiglia.getIdFamiglia());
                }

                // Imposta gli ID delle famiglie nella ComboBox
                IdFamiglia.getItems().setAll(familyIds);
            }
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

    protected void showWallet() {
        if (portafogliUtente != null && !portafogliUtente.isEmpty()) {
            if (currentWalletIndex >= portafogliUtente.size()) {
                System.out.println("Indice del portafoglio supera la dimensione della lista.");
                return;
            }

            Portafoglio portafoglio = portafogliUtente.get(currentWalletIndex);
            balanceLabel.setText(portafoglio.getSaldo() != 0 ? String.valueOf(portafoglio.getSaldo()) : "0.00");
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
            List<Transazione> transazioni = transazioneDAO.getByWalletId(walletId);

            ObservableList<Transazione> transactionDetails = FXCollections.observableArrayList(transazioni);
            transactionsTableView.setItems(transactionDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNextWallet() {
        System.out.println("Next wallet button clicked!"); // Verifica che questo venga eseguito
        currentWalletIndex++; // Incrementa l'indice della carta
        if (currentWalletIndex >= portafogliUtente.size()) { // Se l'indice supera il numero di carte
            currentWalletIndex = 0; // Torna alla prima carta
        }
        currentWalletID.setText(portafogliUtente.get(currentWalletIndex).getIdPortafoglio());
        showWallet();
    }

    @FXML
    private void handlePreviousWallet() {
        System.out.println("Previous wallet button clicked!");
        if (currentWalletIndex == 0)
            currentWalletIndex = portafogliUtente.size() -1;
        else
            currentWalletIndex--;

        currentWalletID.setText(portafogliUtente.get(currentWalletIndex).getIdPortafoglio());
        showWallet();
    }


    @FXML
    public void insertWallet() {
        showDialog("/interfaccia/addWallet.fxml", addWalletButton, "Nuovo Portafoglio");
        handlePreviousWallet();
        handleNextWallet();
        loadUserWallet();
    }

    @FXML
    public void updateWallet() {
        showDialog("/interfaccia/editWallet.fxml", editWalletButton, "Modifica Portafoglio");
        handlePreviousWallet();
        handleNextWallet();
        loadUserWallet();
    }

    @FXML
    public void deleteWallet() {
        showDialog("/interfaccia/deleteWallet.fxml", deleteWalletButton, "Elimina Portafoglio");
        loadUserWallet();
    }

}
