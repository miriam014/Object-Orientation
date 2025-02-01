package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.util.Duration;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DAO_Implementation.UtentiInFamiglieDAOimp;
import smu.DTO.Famiglia;
import smu.DAO_Implementation.FamigliaDAOimp;
import smu.DTO.Transazione;
import smu.Sessione;
import smu.DTO.Carta;

import java.sql.SQLException;
import java.time.LocalDate;
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
    @FXML private Button deleteFamily;



    @FXML
    private void initialize() {
        popolaComboBox();

    }


    @FXML
    private void popolaComboBox(){
        List<Famiglia> famiglieUtente = Sessione.getInstance().getFamilyByUsername();
        familyComboBox.getItems().clear();
        for (Famiglia famiglia : famiglieUtente){
            familyComboBox.getItems().add(famiglia.getNomeFamiglia());
        }

    familyComboBox.setOnAction(event -> { //listner per aggiornare la combobox per gli utenti
        String nomeFamigliaSelezionata = familyComboBox.getValue();
        if(nomeFamigliaSelezionata != null){
            String idFamigliaSelezionata = null;
            for (Famiglia famiglia : famiglieUtente){
                if (famiglia.getNomeFamiglia().equals(nomeFamigliaSelezionata)){
                    idFamigliaSelezionata = famiglia.getIdFamiglia();
                    break;
                }
            }

            if (idFamigliaSelezionata != null){
                UtentiInFamiglieDAOimp utentiInFamigliaDAOimp = new UtentiInFamiglieDAOimp();
                List<String> utentiFamiglia = null;
                try {
                    utentiFamiglia = utentiInFamigliaDAOimp.getUsersByFamilyId(idFamigliaSelezionata);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // Controlla se la lista di utenti è null o vuota
                if (utentiFamiglia != null && !utentiFamiglia.isEmpty()) {
                    //Aggiungere "Tutti" come prima opzione
                    utentiFamiglia.add(0, "Tutti");
                    utenteComboBox.getItems().addAll(utentiFamiglia);
                } else {
                    // Puoi decidere di aggiungere un messaggio di default o gestire il caso
                    utenteComboBox.getItems().clear();
                    utenteComboBox.getItems().add("Nessun utente disponibile");
                }

            }
        }

    });
    }


    @FXML
    protected void initializeTableView() {
    }


    @FXML
    public void newFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/newFamiglia.fxml", newFamily, "Crea nuova Famiglia");
        initializeTableView();
    }

    @FXML
    public void changeFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/changeFamiglia.fxml", changeFamily, "Modifica famiglia");
        initializeTableView();
    }

    @FXML
    public void deleteFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/deleteFamiglia.fxml", deleteFamily, "Rimuovi Famiglia");
        initializeTableView();
    }

    @FXML
    private void SelezionaFamiglia(ActionEvent event) {
        boolean isVisible = familyComboBox.isVisible();
        familyComboBox.setVisible(!isVisible);
        utenteComboBox.setVisible(!isVisible);

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

