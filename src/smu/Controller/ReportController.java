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

public class ReportController {
    @FXML
    public Label LabelDati;
    @FXML
    private Label saldoIniziale;
    @FXML
    private Label saldoFinale;
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
            Sessione.getInstance().setCartaSelezionata(carteUtente.getFirst());
        }
        monthComboBox.setValue(months.get(Calendar.getInstance().get(Calendar.MONTH)));
        yearComboBox.setValue(currentYear);

        LabelDati.setText(cardComboBox.getValue() + "\n" + monthComboBox.getValue() + " " + yearComboBox.getValue());

        // listener per aggiornare il Label e i grafici in tempo reale
        monthComboBox.setOnAction(e -> aggiornaGrafici());
        yearComboBox.setOnAction(e -> aggiornaGrafici());
        cardComboBox.setOnAction(e -> {
            for (Carta carta : carteUtente) {    // Aggiorna la carta selezionata nella sessione
                if (carta.getNomeCarta().equals(cardComboBox.getValue())) {
                    Sessione.getInstance().setCartaSelezionata(carta);
                }
            }
            aggiornaGrafici();
        });

        aggiornaGrafici();
    }

    private void aggiornaGrafici() {
        String mese = monthComboBox.getValue();
        Integer anno = yearComboBox.getValue();

        if ((mese != null) && (anno != null)) {
            popolabarChartEntrate(mese, anno);
            popolabarChartUscite(mese, anno);
            aggiornaLabel();
        }
    }

    private void popolabarChartEntrate(String mese, Integer anno) {
        popolaBarChart("Entrata", statisticheEntrate, "Entrate", mese, anno);
    }

    private void popolabarChartUscite(String mese, Integer anno) {
        popolaBarChart("Uscita", statisticheUscite, "Uscite", mese, anno);
    }

    private void popolaBarChart (String tipoTransazione, XYChart<String, Number> grafico, String nomeSerie, String mese, Integer anno) {
        grafico.getData().clear(); // Pulisce i dati precedeti
        Carta cartaSelezionata = Sessione.getInstance().getCartaSelezionata();
        TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
        List<Transazione> transazioni = new ArrayList<>();

        try {
            transazioni = transazioneDAO.getByCardNumber(cartaSelezionata.getNumeroCarta(), tipoTransazione);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Float> importi = new ArrayList<>();
        int meseSelezionato = convertiMeseInNumero(mese);
        System.out.println("Mese selezionato: " + meseSelezionato);

        for (Transazione transazione : transazioni) {
            LocalDate dataTransazione = transazione.getData().toLocalDate();
            int meseTransazione = dataTransazione.getMonthValue();
            int annoTransazione = dataTransazione.getYear();

            System.out.println("Transazione: " + transazione + " - Mese: " + meseTransazione + ", Anno: " + annoTransazione);

            if(meseTransazione == meseSelezionato && annoTransazione == anno){
                importi.add(transazione.getImporto());
            }
        }

        // Se non ci sono importi, imposta tutti i valori (massimo, minimo, medio) a 0
        float massimo = importi.isEmpty() ? 0.1F : calcolaMassimo(importi);
        float minimo = importi.isEmpty() ? 0.1F : calcolaMinimo(importi);
        float medio = importi.isEmpty() ? 0.1F : calcolaMedio(importi);

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(nomeSerie);

        // Crea i dati per il grafico con i tooltip
        XYChart.Data<String, Number> dataMassimo = new XYChart.Data<>("Massima " + tipoTransazione, massimo);
        XYChart.Data<String, Number> dataMedio = new XYChart.Data<>("Media " + tipoTransazione, medio);
        XYChart.Data<String, Number> dataMinimo = new XYChart.Data<>("Minima " + tipoTransazione, minimo);

        serie.getData().add(dataMassimo);
        serie.getData().add(dataMedio);
        serie.getData().add(dataMinimo);
        grafico.getData().add(serie);

        aggiungiTooltip(dataMassimo, massimo);
        aggiungiTooltip(dataMedio, medio);
        aggiungiTooltip(dataMinimo, minimo);

        NumberAxis asseY = (NumberAxis) grafico.getYAxis();
        if (importi.size() <= 1) {
            asseY.setAutoRanging(false);
            asseY.setLowerBound(0);
            asseY.setUpperBound(Math.max(10, massimo)); // imposta un limite massimo ragionevole
        } else {
            asseY.setAutoRanging(true);
        }
    }

    private float calcolaMassimo(List<Float> importi) {
        float massimo = Float.MIN_VALUE;
        for (Float importo : importi) {
            if (importo > massimo) {
                massimo = importo;
            }
        }
        System.out.println("Massimo: " + massimo);
        return massimo;
    }


    private float calcolaMinimo(List<Float> importi) {
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
        float somma = 0;
        for (Float importo : importi) {
            somma += importo;
        }
        System.out.println("Somma: " + somma);
        return somma / importi.size();
    }

    private void aggiungiTooltip(XYChart.Data<String, Number> data, float valore) {
        Tooltip tooltip = new Tooltip("Importo: " + valore);
        Tooltip.install(data.getNode(), tooltip);

        tooltip.setShowDelay(Duration.millis(0.1)); // Ritardo di visualizzazione
        tooltip.setHideDelay(Duration.millis(100)); // Ritardo di scomparsa
    }

    // Metodo per aggiornare i Label quando il mese o l'anno vengono modificati
    private void aggiornaLabel() {
        String selectedCard = cardComboBox.getValue();
        String selectedMonth = monthComboBox.getValue();
        Integer selectedYear = yearComboBox.getValue();

        if (selectedMonth != null && selectedYear != null &&selectedCard != null) {
            LabelDati.setText(selectedCard + "\n" + selectedMonth + " " + selectedYear);
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


    private int convertiMeseInNumero (String mese) {
        return switch (mese) {
            case "Gennaio" -> 1;
            case "Febbraio" -> 2;
            case "Marzo" -> 3;
            case "Aprile" -> 4;
            case "Maggio" -> 5;
            case "Giugno" -> 6;
            case "Luglio" -> 7;
            case "Agosto" -> 8;
            case "Settembre" -> 9;
            case "Ottobre" -> 10;
            case "Novembre" -> 11;
            case "Dicembre" -> 12;
            default -> 0;
        };
    }
}
