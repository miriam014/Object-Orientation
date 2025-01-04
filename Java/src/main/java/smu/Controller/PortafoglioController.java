package smu.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import smu.DAO_Implementation.TransazioneDAOimp;
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
    @FXML public Button addWalletButton;
    @FXML public Button editWalletButton;
    @FXML private Label balanceLabel;
    @FXML protected Label walletNameLabel;
    @FXML private TableView<Transazione> transactionsTableView;

    @FXML protected TextField nomePortafoglio;
    @FXML protected ComboBox<String> IdFamiglia;
    @FXML protected ComboBox<String> IdPortafoglio;
    @FXML protected Button Conferma;

    protected List<Portafoglio> portafogliUtente; // Lista delle carte dell'utente
    private int currentWalletIndex;

    @FXML private TableColumn<Transazione, String> idColumn;
    @FXML private TableColumn<Transazione, String> tipoColumn;
    @FXML private TableColumn<Transazione, Double> importoColumn;
    @FXML private TableColumn<Transazione, String> dataColumn;
    @FXML private TableColumn<Transazione, String> causaleColumn;
    @FXML private TableColumn<Transazione, String> daAColumn;
    @FXML private TableColumn<Transazione, String> categoriaColumn;

    @FXML
    public void initialize() {
        // Configura le colonne
        idColumn.setCellValueFactory(new PropertyValueFactory<>("IDTransazione"));
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("TipoTransazione"));
        importoColumn.setCellValueFactory(new PropertyValueFactory<>("Importo"));
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("Data"));
        causaleColumn.setCellValueFactory(new PropertyValueFactory<>("Causale"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("Categoria"));

        daAColumn.setCellValueFactory(cellData -> {
            Transazione transazione = cellData.getValue();
            if ("Entrata".equals(transazione.getTipoTransazione())) {
                return new SimpleStringProperty(transazione.getMittente());
            } else {
                return new SimpleStringProperty(transazione.getDestinatario());
            }
        });

        // Carica i portafogli dell'utente
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
        showWallet(); // Mostra la carta corrente
    }


    @FXML
    public void insertWallet() {
        showDialog("/interfaccia/addWallet.fxml", addWalletButton, "Nuovo Portafoglio");
        loadUserWallet();
    }

    @FXML
    public void updateWallet() {
        showDialog("/interfaccia/editWallet.fxml", editWalletButton, "Modifica Portafoglio");
        loadUserWallet();
    }
/*
    @FXML
    public void deleteWallet() {
        showDialog("/interfaccia/deleteWallet.fxml", deleteWalletButton, "Elimina Portafoglio");
    }*/


}
