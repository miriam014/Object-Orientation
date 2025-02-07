package smu.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import smu.DAO_Implementation.UtentiInFamiglieDAOimp;
import smu.DTO.Famiglia;
import smu.DTO.Transazione;
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
        initializeTableView();
        familyComboBox.setVisible(false);
        utenteComboBox.setVisible(false);
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
                Integer idFamigliaSelezionata = null;
                utenteComboBox.setDisable(false);

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
                    utenteComboBox.getItems().clear();


                    if (utentiFamiglia != null && !utentiFamiglia.isEmpty()) {
                        //Aggiungere "Tutti" come prima opzione
                        utenteComboBox.getItems().add("Tutti");
                        utenteComboBox.getItems().addAll(utentiFamiglia);
                    } else {
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

