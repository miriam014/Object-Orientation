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
    @FXML public Label LabelDati;
    @FXML private Label saldoIniziale;
    @FXML private Label saldoFinale;
    @FXML private Button selectButton;
    @FXML private ComboBox<String> cardComboBox;
    @FXML private ComboBox<String> monthComboBox; // ComboBox per i mesi
    @FXML private ComboBox<Integer> yearComboBox; // ComboBox per gli anni
    @FXML private HBox comboBoxContainer; // Contenitore delle ComboBox e del pulsante
    @FXML private BarChart<String, Number> statisticheEntrate;
    @FXML private BarChart<String, Number> statisticheUscite;



    @FXML
    private void initialize() throws SQLException {
        popolaComboBox();
        addComboBoxListeners();
        aggiornaGrafici();
    }

    private void popolaComboBox() {
        //Popola le carte dell'utente
        List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
        cardComboBox.getItems().clear();
        for (Carta carta : carteUtente) {
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
    }

    private void addComboBoxListeners() {
        // listener per aggiornare il Label e i grafici in tempo reale
        monthComboBox.setOnAction(e -> aggiornaGrafici());
        yearComboBox.setOnAction(e -> aggiornaGrafici());
        cardComboBox.setOnAction(e -> {
            List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
            for (Carta carta : carteUtente) {    // Aggiorna la carta selezionata nella sessione
                if (carta.getNomeCarta().equals(cardComboBox.getValue())) {
                    Sessione.getInstance().setCartaSelezionata(carta);
                }
            }
            aggiornaGrafici();
        });
    }

    private void aggiornaGrafici() {
        String mese = monthComboBox.getValue();
        Integer anno = yearComboBox.getValue();

        if ((mese != null) && (anno != null)) {
            popolaBarChart(statisticheEntrate, "Entrata",mese, anno);
            popolaBarChart(statisticheUscite, "Uscita", mese, anno);
            aggiornaLabel();
            aggiornaSaldi(monthComboBox.getValue(), yearComboBox.getValue());
        }
    }

    private void popolaBarChart ( BarChart<String, Number> grafico, String tipoTransazione, String mese, Integer anno) {
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
        float massimo = importi.isEmpty() ? 0 : calcolaMassimo(importi);
        float minimo = importi.isEmpty() ? 0 : calcolaMinimo(importi);
        float medio = importi.isEmpty() ? 0 : calcolaMedio(importi);

        float valoreMinimoColonne = 0.15F;

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName(tipoTransazione);

        // Crea i dati per il grafico con i tooltip
        XYChart.Data<String, Number> dataMassimo = new XYChart.Data<>("Massima " + tipoTransazione, massimo == 0 ? valoreMinimoColonne : massimo);
        XYChart.Data<String, Number> dataMedio = new XYChart.Data<>("Media " + tipoTransazione, medio == 0 ? valoreMinimoColonne : medio);
        XYChart.Data<String, Number> dataMinimo = new XYChart.Data<>("Minima " + tipoTransazione, minimo == 0 ? valoreMinimoColonne : minimo);

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
        //forza il layout per forzare l'aggiornamneto del grafico
        grafico.layout();
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

    private void aggiornaSaldi(String mese, int anno) {
        try {
            Carta cartaSelezionata = Sessione.getInstance().getCartaSelezionata();
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();

            List<Transazione> tutteTransazioni = transazioneDAO.getByCardNumber(cartaSelezionata.getNumeroCarta(), "Tutte");

            // Ottieni la data di inizio mese (primo giorno del mese)
            int meseSelezionato = convertiMeseInNumero(mese);
            LocalDate inizioMese = LocalDate.of(anno, meseSelezionato, 1);

            // Calcolare il saldo iniziale (sottraendo tutte le transazioni effettuate dopo l'inizio del mese)
            float saldoInizialeVal = cartaSelezionata.getSaldo(); // Saldo iniziale della carta
            float saldoFinaleVal = cartaSelezionata.getSaldo();

            for (Transazione transazione : tutteTransazioni) {
                LocalDate dataTransazione = transazione.getData().toLocalDate();
                float importo = transazione.getImporto();
                String tipoTransazione = transazione.getTipoTransazione(); // Usa getTipo() se il tipo è nel campo Tipo

                if (dataTransazione.isAfter(inizioMese)) {
                    saldoInizialeVal += tipoTransazione.equals("Entrata") ? -importo : importo;
                    saldoFinaleVal += tipoTransazione.equals("Entrata") ? -importo : importo;
                }
                if (dataTransazione.getMonthValue() == meseSelezionato && dataTransazione.getYear() == anno) {
                    //se la transazione è stata effettuata nel mese ed annp selezionato, aggiorna il saldo finale
                    saldoFinaleVal += tipoTransazione.equals("Entrata") ? importo : -importo;
                }
            }

            // Aggiorna i label per il saldo iniziale e finale
            saldoIniziale.setText(String.format("Saldo Iniziale: %.2f €", saldoInizialeVal));
            saldoFinale.setText(String.format("Saldo Finale: %.2f €", saldoFinaleVal));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Metodo per aggiornare i Label quando il mese o l'anno vengono modificati
    //non mi serve fare il controllo per vedere se sono nulli perchè mai lo possono essere
    private void aggiornaLabel() {
        LabelDati.setText(cardComboBox.getValue() + "\n" + monthComboBox.getValue() + " " + yearComboBox.getValue());
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
        return List.of("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre").indexOf(mese) + 1;
    }
}
