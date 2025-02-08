package smu.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import smu.DAO_Implementation.*;
import smu.DTO.*;
import smu.Sessione;

import java.sql.SQLException;
import java.util.*;

import static javax.swing.JColorChooser.showDialog;

public class FamigliaController extends Controller{

    @FXML private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante
    @FXML private Button selectFamilyButton;
    @FXML private ComboBox<String> familyComboBox;
    @FXML private ComboBox<String> utenteComboBox;
    @FXML private TableView<Transazione> TabellaFamiglia;
    @FXML private TableColumn<Transazione, String> tipoColumn;
    @FXML private TableColumn<Transazione, Float> importoColumn;
    @FXML private TableColumn<Transazione, String> daAColumn;
    @FXML private TableColumn<Transazione, String> utenteColumn;
    @FXML private TableColumn<Transazione, String> portafoglioColumn;

    @FXML private Button changeFamily;
    @FXML private Button newFamily;
    @FXML private Button logoutFamily;



    @FXML
    private void initialize() {
        popolaComboBox();
        familyComboBox.setVisible(false);
        utenteComboBox.setVisible(false);
    }


    @FXML
    private void popolaComboBox(){
        List<Famiglia> famiglieUtente = Sessione.getInstance().getFamilyByUsername();
        familyComboBox.getItems().clear();

        if (famiglieUtente == null || famiglieUtente.isEmpty()) {
            System.out.println("⚠️ Nessuna famiglia trovata per l'utente!");
            return;
        }

        System.out.println("✅ Famiglie trovate: " + famiglieUtente.size());

        for (Famiglia famiglia : famiglieUtente){
            familyComboBox.getItems().add(famiglia.getNomeFamiglia());
        }

        familyComboBox.setOnAction(event -> { //listner per aggiornare la combobox per gli utenti
            String nomeFamigliaSelezionata = familyComboBox.getValue();
            if(nomeFamigliaSelezionata != null) {
                Integer idFamigliaSelezionata = null;
                utenteComboBox.setDisable(false);

                for (Famiglia famiglia : famiglieUtente) {
                    if (famiglia.getNomeFamiglia().equals(nomeFamigliaSelezionata)) {
                        idFamigliaSelezionata = famiglia.getIdFamiglia();
                        break;
                    }
                }

                if (idFamigliaSelezionata != null) {
                    UtentiInFamiglieDAOimp utentiInFamigliaDAOimp = new UtentiInFamiglieDAOimp();
                    List<String> utentiFamiglia = null;
                    try {
                        utentiFamiglia = utentiInFamigliaDAOimp.getUsersByFamilyId(idFamigliaSelezionata);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    utenteComboBox.getItems().clear();


                    if (utentiFamiglia != null && !utentiFamiglia.isEmpty()) {
                        //Aggiungere "Tutti" come prima opzione
                        utenteComboBox.getItems().add("Tutti");
                        utenteComboBox.getItems().addAll(utentiFamiglia);
                    } else {
                        utenteComboBox.getItems().add("Nessun utente disponibile");
                    }
                }
                initializeTableView();
            }
        });
    }


    @FXML
    protected void initializeTableView() {
        tipoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoTransazione()));
        importoColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getImporto()));
        daAColumn.setCellValueFactory(cellData -> {
            Transazione transazione = cellData.getValue();
            String daAValue = (transazione.getDestinatario() != null && !transazione.getDestinatario().isEmpty())
                    ? transazione.getDestinatario()
                    : transazione.getMittente();
            return new SimpleStringProperty(daAValue);
        });
        utenteColumn.setCellValueFactory(cellData -> {
            Transazione transazione = cellData.getValue();
            String numeroCarta = transazione.getNumeroCarta();

            try {
                // 1. Ottieni il numero del conto corrente tramite il numero della carta
                CartaDAOimp cartaDAOimp = new CartaDAOimp();
                Carta carta = cartaDAOimp.getByNumeroCarta(numeroCarta);
                if (carta == null) {
                    return new SimpleStringProperty("Carta non trovata");
                }

                String numeroConto = carta.getNumeroConto();

                // 2. Ottieni l'utente associato al conto corrente
                ContoCorrenteDAOimp contoCorrenteDAOimp = new ContoCorrenteDAOimp();
                ContoCorrente contoCorrente = contoCorrenteDAOimp.getByAccountNumber(numeroConto);

                if (contoCorrente != null) {
                    // L'utente è collegato al conto corrente tramite 'Username'
                    String usernameUtente = contoCorrente.getUsername();

                    // Ottieni l'utente tramite il suo username
                    UtenteDAOimp utenteDAOimp = new UtenteDAOimp();
                    Utente utente = utenteDAOimp.getByUsername(usernameUtente);

                    if (utente != null) {
                        return new SimpleStringProperty(utente.getNome() + " " + utente.getCognome());
                    } else {
                        return new SimpleStringProperty("Utente non trovato");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Errore SQL");
            }

            return new SimpleStringProperty("Errore");
        });
        portafoglioColumn.setCellValueFactory(cellData -> {
            Transazione transazione = cellData.getValue();
            try {
                TransazioneInPortafoglioDAOimp transazioneInPortafoglio = new TransazioneInPortafoglioDAOimp();
                PortafoglioDAOimp portafoglioDAOimp = new PortafoglioDAOimp();

                Integer idPortafoglio = transazioneInPortafoglio.getPortafoglioByIdTransazione(cellData.getValue().getIDTransazione());

                if (idPortafoglio != null) {
                    Portafoglio portafoglio = portafoglioDAOimp.getByID(idPortafoglio.toString());
                    return new SimpleStringProperty(portafoglio.getNomePortafoglio());
                } else {
                    return new SimpleStringProperty(" ");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new SimpleStringProperty("Nessun portafoglio legato alla transazione");
            }
        });

        aggiornaTableView();
    }

    private void aggiornaTableView(){
        String nomeFamigliaSelezionata = familyComboBox.getValue();
        if(nomeFamigliaSelezionata == null)  {
            return;
        };

        System.out.println("Famiglia selezionata:" + nomeFamigliaSelezionata);

        // Recuperiamo l'ID della famiglia selezionata
        Integer idFamigliaSelezionata = null;
        for (Famiglia famiglia : Sessione.getInstance().getFamilyByUsername()) {
            if (famiglia.getNomeFamiglia().equals(nomeFamigliaSelezionata)) {
                idFamigliaSelezionata = famiglia.getIdFamiglia();
                break;
            }
        }

        try {
            PortafoglioDAOimp portafoglioDAOimp = new PortafoglioDAOimp();
            TransazioneDAOimp transazioneDAOimp = new TransazioneDAOimp();

            //Lista finale di tutte le transazioni
            List<Transazione> transazioni = new ArrayList<>();

            //otteniamo tutti i portafogli della famiglia selezionata
            List<Portafoglio> portafogli = portafoglioDAOimp.getByIdFamiglia(idFamigliaSelezionata);

            if (portafogli.isEmpty()) {
                System.out.println("Nessun portafoglio trovato per la famiglia selezionata!");
                return;
            }
            //per ogni portafoglio trovato, recuperiamo le sue transazioni
            for (Portafoglio portafoglio : portafogli){
                List<Transazione> transazioniPortafoglio = transazioneDAOimp.getByWalletId(portafoglio.getIdPortafoglio());
                if (transazioniPortafoglio.isEmpty()) {
                    System.out.println("Nessuna transazione trovata per il portafoglio: " + portafoglio.getNomePortafoglio());
                } else {
                    System.out.println("Transazioni trovate per " + portafoglio.getNomePortafoglio() + ": " + transazioniPortafoglio.size());
                    transazioni.addAll(transazioniPortafoglio);
                }
            }

            TabellaFamiglia.getItems().clear();
            TabellaFamiglia.getItems().addAll(transazioni);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void newFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/newFamiglia.fxml", newFamily, "Crea nuova Famiglia");
        initialize();
    }

    @FXML
    public void changeFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/changeFamiglia.fxml", changeFamily, "Modifica famiglia");
        initialize();
    }

    @FXML
    public void logoutFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/leaveFamiglia.fxml", logoutFamily, "Logout dalla famiglia");
        initialize();
    }

    @FXML
    private void SelezionaFamiglia(ActionEvent event) {
        boolean isVisible = familyComboBox.isVisible();
        familyComboBox.setVisible(!isVisible);
        utenteComboBox.setVisible(!isVisible);
        utenteComboBox.setDisable(true);
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer) {
            familyComboBox.setVisible(false);
            utenteComboBox.setVisible(false);
        }
    }
}

