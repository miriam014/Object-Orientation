package smu.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO.TransazioneInPortafoglioDAO;
import smu.DAOImplementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAOImplementation.TransazioneDAOimp;
import smu.DAOImplementation.TransazioneInPortafoglioDAOimp;
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
    private List<Transazione> transazioniList = new ArrayList<>();


    public void initialize(){
        popolaComboBoxPortafogli();
        initializeTableView();
        Conferma.setDisable(true);

        idPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedWalletID = newValue.getIdPortafoglio();
            System.out.println("ID Portafoglio selezionato: " + selectedWalletID);
            try {
                popolaComboBoxTransazioni();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

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

            // Popolo la ComboBox con i portafogli dell'utente
            ObservableList<Portafoglio> observableWallets = FXCollections.observableArrayList(personalWallets);
            idPortafoglio.setItems(observableWallets);

            // Usa il setCellFactory per visualizzare solo il nome del portafoglio
            idPortafoglio.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Portafoglio portafoglio, boolean empty) {
                    super.updateItem(portafoglio, empty);
                    if (empty || portafoglio == null) {
                        setText(null);
                    } else {
                        setText(portafoglio.getNomePortafoglio()); // Mostra solo il nome
                    }
                }
            });

            // Impostiamo anche il comportamento quando un elemento viene selezionato
            idPortafoglio.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Portafoglio portafoglio, boolean empty) {
                    super.updateItem(portafoglio, empty);
                    if (empty || portafoglio == null) {
                        setText(null);
                    } else {
                        setText(portafoglio.getNomePortafoglio()); // Mostra solo il nome
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popolaComboBoxTransazioni() throws SQLException {
        try {
            if (transazioniList != null && !transazioniList.isEmpty()) {
                List<String> idTransazioni = new ArrayList<>();
                for (Transazione t : transazioniList) {
                    idTransazioni.add(t.getIDTransazione());
                }

                // Popola la ComboBox con gli ID delle transazioni
                ObservableList<String> observableTransactions = FXCollections.observableArrayList(idTransazioni);
                idTransazione.setItems(observableTransactions);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                // Carica le transazioni che possono essere associate al portafoglio selezionato
                TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
                AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
                String numeroCarta = associazione.getCardNumberByID(portafoglioSelezionato.getIdPortafoglio());

                List<Transazione> transazioniGiaPresenti = transazioneDAO.getByWalletId(portafoglioSelezionato.getIdPortafoglio());
                // Recupera le transazioni in base al numero della carta
                List<Transazione> transazioni = transazioneDAO.getByCardNumber(numeroCarta, "Tutte");
                // Crea una lista per le transazioni che non sono già nel portafoglio
                List<Transazione> transazioniFiltrate = new ArrayList<>();

                for (Transazione t : transazioni) {
                    boolean transazionePresente = false;
                    for (Transazione t2 : transazioniGiaPresenti) {
                        if (t.getIDTransazione().equals(t2.getIDTransazione())) {
                            transazionePresente = true;
                            break;
                        }
                    }
                    if (!transazionePresente) {
                        transazioniFiltrate.add(t);
                    }
                }

                // Popola la TableView con le transazioni
                ObservableList<Transazione> transactionDetails = FXCollections.observableArrayList(transazioniFiltrate);
                transactionsTableView.setItems(transactionDetails);

                // Salva le transazioni nella lista per il futuro utilizzo
                transazioniList = new ArrayList<>(transazioniFiltrate);

                // Popola la ComboBox degli ID delle transazioni
                popolaComboBoxTransazioni();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void inserisciTransazione() throws SQLException {
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
