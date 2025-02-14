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
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO_Implementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.Sessione;

import java.util.ArrayList;
import java.util.List;


public class AddTransactionInWalletController extends PortafoglioController {

    @FXML private TextField IdAggiugi;
    @FXML private ComboBox<Portafoglio> portafogli;
    @FXML private TableView<Transazione> transactionsTableView;

    @FXML private TableColumn<Transazione, String> idColumn;
    @FXML private TableColumn<Transazione, String> tipoColumn;
    @FXML private TableColumn<Transazione, Float> importoColumn;
    @FXML private TableColumn<Transazione, String> dataColumn;
    @FXML private TableColumn<Transazione, String> causaleColumn;
    @FXML private TableColumn<Transazione, String> daAColumn;
    @FXML private TableColumn<Transazione, String> categoriaColumn;

public void initialize(){
    popolaComboBox();
    initializeTableView();

    portafogli.setOnAction(this::portafoglioSelezionato);
}

    private void popolaComboBox() {
        try {
            // Recupera l'istanza della sessione e ottiene l'utente loggato
            Sessione sessione = Sessione.getInstance();
            List<Portafoglio> personalWallets = sessione.getPersonalWallets();

            // Popoliamo la ComboBox con i portafogli dell'utente
            ObservableList<Portafoglio> observableWallets = FXCollections.observableArrayList(personalWallets);
            portafogli.setItems(observableWallets);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeTableView() {
        transactionsTableView.setItems(FXCollections.observableArrayList());
        // Configura le colonne
        idColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIDTransazione()));
        tipoColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipoTransazione()));
        importoColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getImporto()));
        dataColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getData().toString()));
        causaleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCausale()));
        daAColumn.setCellValueFactory(cellData -> {
            Transazione t = cellData.getValue();
            String daA = t.getTipoTransazione().equalsIgnoreCase("tutte")
                    ? t.getMittente()
                    : t.getDestinatario();
            return new SimpleStringProperty(daA);
        });
        categoriaColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategoria()));

    }


    public void portafoglioSelezionato(ActionEvent actionEvent) {
        Portafoglio portafoglioSelezionato = portafogli.getValue();
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
}
