package smu.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import smu.DAO.TransazioneInPortafoglioDAO;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DAO_Implementation.TransazioneInPortafoglioDAOimp;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.DTO.TransazioneInPortafoglio;
import smu.DTO.Utente;
import smu.Sessione;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class PortafoglioController {

    @FXML
    private Label balanceLabel;
    @FXML
    private Label walletNameLabel;
    @FXML
    private TableView<Transazione> transactionsTableView;

    private List<Portafoglio> portafogliUtente; // Lista delle carte dell'utente
    private int currentWalletIndex;

    @FXML
    private TableColumn<Transazione, String> idColumn;
    @FXML
    private TableColumn<Transazione, String> tipoColumn;
    @FXML
    private TableColumn<Transazione, Double> importoColumn;
    @FXML
    private TableColumn<Transazione, String> dataColumn;
    @FXML
    private TableColumn<Transazione, String> causaleColumn;
    @FXML
    private TableColumn<Transazione, String> daAColumn;
    @FXML
    private TableColumn<Transazione, String> categoriaColumn;

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


    private void loadUserWallet() {
        try {
            portafogliUtente = Sessione.getInstance().getPortafogliUtente();
            if (portafogliUtente == null || portafogliUtente.isEmpty()) {
                System.out.println("Nessun portafoglio trovato per l'utente."); // Debug
            } else {
                /*if(portafogliUtente.size() > 1) {
                    nextWalletButton.setVisible(true); // Mostra il pulsante per la prossima carta
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

    /*
    @FXML
    private void addNewTransactionInWallet() {
        try {
            // Carica la nuova finestra per la selezione delle transazioni
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/smu/View/TransactionSelectionView.fxml"));
            Parent root = loader.load();

            // Ottieni il controller della nuova finestra
            TransactionSelectionController transactionController = loader.getController();

            // Passa l'utente corrente per recuperare le transazioni
            transactionController.setUtente(Sessione.getInstance().getUtenteLoggato());

            // Mostra la finestra
            Stage stage = new Stage();
            stage.setTitle("Seleziona Transazione");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL); // Blocca la finestra principale finché questa è aperta
            stage.showAndWait();

            // Recupera la transazione selezionata dall'utente
            Transazione selectedTransaction = transactionController.getSelectedTransaction();

            if (selectedTransaction != null) {
                // Ottieni il portafoglio corrente
                Portafoglio currentWallet = portafogliUtente.get(currentWalletIndex);

                // Crea l'associazione tra transazione e portafoglio
                TransazioneInPortafoglio association = new TransazioneInPortafoglio(
                        selectedTransaction.getIDTransazione(),
                        currentWallet.getIdPortafoglio()
                );

                // Inserisci l'associazione nel database
                TransazioneInPortafoglioDAO newTransactionInWallet = new TransazioneInPortafoglioDAOimp();
                boolean success = newTransactionInWallet.insert(association);

                if (success) {
                    System.out.println("Transazione aggiunta al portafoglio: " + selectedTransaction);

                    // Aggiorna la vista per includere la nuova transazione
                    loadTransactions(currentWallet.getIdPortafoglio());
                } else
                    System.out.println("Errore durante l'inserimento della transazione nel portafoglio.");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */


}
