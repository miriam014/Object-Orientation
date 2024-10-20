package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Transazione;
import smu.Sessione;
import smu.DTO.Carta;

import java.sql.SQLException;
import java.util.*;

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
    private BarChart<String, Number> statisticheEntrate;
    @FXML
    private BarChart<String, Number> statisticheUscite;



    @FXML
    private void initialize() throws SQLException {
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


        popolabarChartEntrate();
        popolabarChartUscite();
    }

    private void popolabarChartEntrate() {
        popolaBarChart("Entrata", statisticheEntrate, "Entrate");
    }

    private void popolabarChartUscite() {
        popolaBarChart("Uscita", statisticheUscite, "Uscite");
    }

    private void popolaBarChart (String tipoTransazione, XYChart<String, Number> grafico, String nomeSerie) {
        grafico.getData().clear(); // Pulisce i dati precedenti
        Carta cartaSelezionata = Sessione.getInstance().getCartaSelezionata();
        TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
        List<Transazione> transazioni = new ArrayList<>();

        try {
            transazioni = transazioneDAO.getByCardNumber(cartaSelezionata.getNumeroCarta(), tipoTransazione);
            System.out.println ("Transazioni: " + transazioni);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(transazioni.isEmpty()){
            System.out.println("Nessuna transazione in " + tipoTransazione);
            return;
        }

        List<Float> importi = new ArrayList<>();
        for (Transazione transazione : transazioni) {
            importi.add(transazione.getImporto());  // Supponendo che getImporto restituisca un valore di tipo Double
        }

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(nomeSerie);

        float massimo = calcolaMassimo(importi);
        float medio = calcolaMedio(importi);
        float minimo = calcolaMinimo(importi);

        serie.getData().add(new XYChart.Data<>("Massima " + tipoTransazione, massimo));
        serie.getData().add(new XYChart.Data<>("Media " + tipoTransazione, medio));
        serie.getData().add(new XYChart.Data<>("Minima " + tipoTransazione, minimo));

        grafico.getData().add(serie);
    }

    private float calcolaMassimo(List<Float> importi) {
        if (importi.isEmpty()) return 0; // Aggiungi un controllo per la lista vuota
        float massimo = Float.MIN_VALUE; // Inizializza con il valore più basso possibile
        for (Float importo : importi) {
            if (importo > massimo) {
                massimo = importo;
            }
        }
        System.out.println("Massimo: " + massimo);
        return massimo;
    }

    private float calcolaMinimo(List<Float> importi) {
        if (importi.isEmpty()) return 0; // Aggiungi un controllo per la lista vuota
        float minimo = Float.MAX_VALUE; // Inizializza con il valore più alto possibile
        for (Float importo : importi) {
            if (importo < minimo) {
                minimo = importo;
            }
        }
        System.out.println("Minimo: " + minimo);
        return minimo;
    }

    private float calcolaMedio(List<Float> importi) {
        if (importi.isEmpty()) return 0; // Gestisci il caso di lista vuota

        float somma = 0;
        for (Float importo : importi) {
            somma += importo;
        }
        System.out.println("Somma: " + somma);
        return somma / importi.size();
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
        boolean isVisible = cardComboBox.isVisible();
        cardComboBox.setVisible(!isVisible);
        monthComboBox.setVisible(!isVisible);
        yearComboBox.setVisible(!isVisible);
    }


    @FXML
    public void handleMouseClick(MouseEvent event) {
        // Se si clicca al di fuori dell'HBox, chiudi le ComboBox
        if (event.getTarget() != comboBoxContainer) {

            cardComboBox.setVisible(false);
            monthComboBox.setVisible(false);
            yearComboBox.setVisible(false);
        }
    }

}
