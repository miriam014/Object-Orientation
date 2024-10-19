package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import smu.Sessione;
import smu.DTO.Carta;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ReportController {
    @FXML
    public Label CartaMeseAnno;
    @FXML
    private Button selectButton;
    @FXML
    private ComboBox<String> cardComboBox;
    @FXML
    private ComboBox<String> monthComboBox; // ComboBox per i mesi
    @FXML
    private ComboBox<Integer> yearComboBox; // ComboBox per gli anni
    @FXML
    private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante



    @FXML
    private void initialize() {
        //Popola le carte dell'utente
        List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
        cardComboBox.getItems().clear();
        for (Carta carta : carteUtente){
            cardComboBox.getItems().add(carta.getNomeCarta());
        }

        // Popola i mesi nella ComboBox
        List<String> months = Arrays.asList("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre");
        monthComboBox.getItems().addAll(months);


        // Popola gli anni nella ComboBox
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 20; i--) {
            yearComboBox.getItems().add(i);
        }

        // Recupera la carta selezionata dall'utente e inizia ad impostare i valori nel label
        Carta cartaSelezionata = Sessione.getInstance().getCartaSelezionata();
        if (cartaSelezionata != null) {
            cardComboBox.setValue(cartaSelezionata.getNomeCarta());
        } else {
            cardComboBox.setValue(carteUtente.getFirst().getNomeCarta());
        }
        monthComboBox.setValue(months.get(Calendar.getInstance().get(Calendar.MONTH)));
        yearComboBox.setValue(currentYear);

        CartaMeseAnno.setText(cardComboBox.getValue() + "\n" + monthComboBox.getValue() + " " + yearComboBox.getValue());

        // listener per aggiornare il Label in tempo reale
        cardComboBox.setOnAction(e -> aggiornaLabel());
        monthComboBox.setOnAction(e -> aggiornaLabel());
        yearComboBox.setOnAction(e -> aggiornaLabel());
    }

    // Metodo per aggiornare il Label quando il mese o l'anno vengono modificati
    private void aggiornaLabel() {
        String selectedCard = cardComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth != null && selectedYear != null &&selectedCard != null) {
            CartaMeseAnno.setText(selectedCard + "\n" + selectedMonth + " " + selectedYear);
        }
    }

    @FXML
    private void SelezionaCarta(ActionEvent event) {
        cardComboBox.setVisible(true);
        monthComboBox.setVisible(true);
        yearComboBox.setVisible(true);
    }


    @FXML
    public void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer && event.getTarget() != selectButton) {

            cardComboBox.setVisible(false);
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);
        }
    }

}
