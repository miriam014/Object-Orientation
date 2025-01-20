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
import smu.DTO.Transazione;
import smu.Sessione;
import smu.DTO.Carta;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class FamigliaController {
    @FXML  private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante
    @FXML private Button selectFamilyButton;
    @FXML private ComboBox<String> familyComboBox;
    @FXML private ComboBox<String> portafoglioComboBox;




    @FXML
    private void initialize() throws SQLException {

    }



    @FXML
    public void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer) {
            familyComboBox.setVisible(false);
            portafoglioComboBox.setVisible(false);
        }
    }
}

