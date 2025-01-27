package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import javafx.util.Duration;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Famiglia;
import smu.DTO.Transazione;
import smu.Sessione;
import smu.DTO.Carta;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static javax.swing.JColorChooser.showDialog;

public class FamigliaController extends Controller{

    @FXML  private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante
    @FXML private Button selectFamilyButton;
    @FXML private ComboBox<String> familyComboBox;
    @FXML private ComboBox<String> portafoglioComboBox;

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
    }

    @FXML
    private void SelezionaFamiglia(ActionEvent actionEvent){

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
        showDialog("/interfaccia/changeFamiglia.fxml", changeFamily, "Modificafamiglia");
        initializeTableView();
    }

    @FXML
    public void deleteFamiglia(ActionEvent actionEvent) {
        showDialog("/interfaccia/deleteFamiglia.fxml", deleteFamily, "Elimina Famiglia");
        initializeTableView();
    }


    @FXML
    private void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer) {
            familyComboBox.setVisible(false);
            portafoglioComboBox.setVisible(false);
        }
    }
}

