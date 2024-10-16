package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ReportController {
    @FXML
    public Label selezionaMeseAnno;
    @FXML
    private Button selectButton;
    @FXML
    private ComboBox<String> monthComboBox; // ComboBox per i mesi
    @FXML
    private ComboBox<Integer> yearComboBox; // ComboBox per gli anni
    @FXML
    private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante



    @FXML
    private void initialize() {
        // Popola i mesi nella ComboBox
        List<String> months = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");
        monthComboBox.getItems().addAll(months);

        // Popola gli anni nella ComboBox
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 20; i--) {
            yearComboBox.getItems().add(i);
        }

        // Imposta il mese e l'anno correnti nelle ComboBox
        monthComboBox.setValue(months.get(Calendar.getInstance().get(Calendar.MONTH))); // Imposta il mese corrente
        yearComboBox.setValue(currentYear); // Imposta l'anno corrente

        // Aggiorna il label con il valore corrente
        selezionaMeseAnno.setText(monthComboBox.getValue() + " " + yearComboBox.getValue());

        // Aggiungi listener per aggiornare il Label in tempo reale
        monthComboBox.setOnAction(e -> aggiornaLabel());
        yearComboBox.setOnAction(e -> aggiornaLabel());
    }

    // Metodo per aggiornare il Label quando il mese o l'anno vengono modificati
    private void aggiornaLabel() {
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth != null && selectedYear != null) {
            selezionaMeseAnno.setText(selectedMonth + " " + selectedYear);
        }
    }

    @FXML
    private void SelezionaMese(ActionEvent event) {
        monthComboBox.setVisible(true);
        yearComboBox.setVisible(true);
    }


    @FXML
    public void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer && event.getTarget() != selectButton) {
            
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);
        }
    }

}
