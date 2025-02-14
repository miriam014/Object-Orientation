package smu.Controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO.TransazioneDAO;
import smu.DAO.TransazioneInPortafoglioDAO;
import smu.DAO_Implementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DAO_Implementation.TransazioneInPortafoglioDAOimp;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.DTO.TransazioneInPortafoglio;
import smu.Sessione;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AddTransactionInWalletController extends PortafoglioController {

    @FXML private ComboBox<Portafoglio> idPortafoglio;
    @FXML private ComboBox<String> idTransazione;
    @FXML private TableView<Transazione> transactionsTableView;

    @FXML private TableColumn<Transazione, String> idColumn;
    @FXML private TableColumn<Transazione, String> tipoColumn;
    @FXML private TableColumn<Transazione, Float> importoColumn;
    @FXML private TableColumn<Transazione, String> dataColumn;
    @FXML private TableColumn<Transazione, String> causaleColumn;
    @FXML private TableColumn<Transazione, String> daAColumn;
    @FXML private TableColumn<Transazione, String> categoriaColumn;

    private String selectedWalletID;
    private String transactionId;

    public void initialize(){
        popolaComboBoxPortafogli();
        initializeTableView();
        Conferma.setDisable(true);

        idPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWalletID = newValue.getIdPortafoglio();
                System.out.println("ID Portafoglio selezionato: " + selectedWalletID);
                try {
                    popolaComboBoxTransazioni();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                checkFormValidity();
            }
        });

        idPortafoglio.setOnAction(this::portafoglioSelezionato);

        idTransazione.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            transactionId= newValue;
            System.out.println("ID Transazione selezionato: " + transactionId);
            checkFormValidity();
        }
        });
    }

    private void popolaComboBoxPortafogli() {
        try {
            // Recupera l'istanza della sessione e ottiene l'utente loggato
            Sessione sessione = Sessione.getInstance();
            List<Portafoglio> personalWallets = sessione.getPersonalWallets();

            // Popoliamo la ComboBox con i portafogli dell'utente
            ObservableList<Portafoglio> observableWallets = FXCollections.observableArrayList(personalWallets);
            idPortafoglio.setItems(observableWallets);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> recuperaIdTransazioni() throws SQLException {
        TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
        AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
        String numeroCarta = associazione.getCardNumberByID(selectedWalletID);

        // Recupera le transazioni in base al numero della carta
        List<Transazione> transazioni = transazioneDAO.getByCardNumber(numeroCarta, "Tutte");
        System.out.println("Transazioni: " + transazioni);

        List<String> IdTransazioni = new ArrayList<>();

        for(Transazione t : transazioni){
            IdTransazioni.add(t.getIDTransazione());
        }

        TransazioneInPortafoglioDAO transazioniInPortafoglio = new TransazioneInPortafoglioDAOimp();
        List<String> transazioniInPortafoglioID = transazioniInPortafoglio.getTransazioniInPortafoglio(selectedWalletID);

        IdTransazioni.removeAll(transazioniInPortafoglioID);

        return IdTransazioni;
    }

    private void popolaComboBoxTransazioni() throws SQLException {
        if (selectedWalletID != null) {
             try {
                 TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
                 AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
                 String numeroCarta = associazione.getCardNumberByID(selectedWalletID);

                 // Recupera le transazioni in base al numero della carta
                 List<Transazione> transazioni = transazioneDAO.getByCardNumber(numeroCarta, "Tutte");
                 List<String> IdTransazioni = new ArrayList<>();

                 for(Transazione t : transazioni){
                     IdTransazioni.add(t.getIDTransazione());
                 }

                // Popoliamo la ComboBox con le transazioni dell'utente
                ObservableList<String> observableTransactions = FXCollections.observableArrayList(IdTransazioni);
                idTransazione.setItems(observableTransactions);

             } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }


    private void checkFormValidity() {
        // Controlla se il nome del portafoglio è stato inserito e se è stata selezionata una carta
        if (selectedWalletID != null && !selectedWalletID.trim().isEmpty() &&
                transactionId != null && !transactionId.trim().isEmpty()) {
            Conferma.setDisable(false); // Abilita il bottone se entrambi i campi sono validi
        }
    }

    protected void initializeTableView() {
        transactionsTableView.setItems(FXCollections.observableArrayList());

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
    }


    public void portafoglioSelezionato(ActionEvent actionEvent) {
        Portafoglio portafoglioSelezionato = idPortafoglio.getValue();
        if (portafoglioSelezionato != null) {
            try {
                // Carica le transazioni del portafoglio selezionato
                TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
                AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
                String numeroCarta = associazione.getCardNumberByID(portafoglioSelezionato.getIdPortafoglio());

                // Recupera le transazioni in base al numero della carta
                List<Transazione> transazioni = transazioneDAO.getByCardNumber(numeroCarta, "Tutte");

                // Popola la TableView con le transazioni
                ObservableList<Transazione> transactionDetails = FXCollections.observableArrayList(transazioni);
                transactionsTableView.setItems(transactionDetails);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void inserisciTransazione() throws SQLException {

        if (selectedWalletID == null || selectedWalletID.trim().isEmpty()) {
            System.out.println("Inserisci un ID portafoglio.");
            return;
        }

        if(transactionId == null || transactionId.trim().isEmpty()) {
            System.out.println("Seleziona una transazione da aggiungere al portafoglio.");
            return;
        }

        try {
            System.out.println("Inserimento della transazione '" + transactionId + " al portafoglio " + selectedWalletID);

            TransazioneInPortafoglioDAO transazioneInPortafoglio = new TransazioneInPortafoglioDAOimp();
            TransazioneInPortafoglio aggiungiTransazione = new TransazioneInPortafoglio(transactionId, selectedWalletID);
            transazioneInPortafoglio.insert(aggiungiTransazione);

        }
        catch(SQLException e) {
            System.out.println("Inserimento della transazione non riuscito.");
            e.printStackTrace();
        }


        // Chiudi la finestra corrente
        Stage stage = (Stage) Conferma.getScene().getWindow();
        stage.close();
    }
}
