package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.DAO_Implementation.SpeseProgrammateDAOimp; // Corretto l'import
import smu.DTO.Carta;
import smu.DTO.SpeseProgrammate;
import smu.Sessione;

import java.sql.Date;
import java.awt.*;
import java.util.List;

public class newProgrammazioneController extends Controller {

    @FXML private ComboBox<String> CartaUtilizzata;
    @FXML private ComboBox<String> Frequenza;
    @FXML private DatePicker DataScadenza;
    @FXML private DatePicker DataTermine;
    @FXML private Button Conferma;
    @FXML private TextField nomeProgrammazione;
    @FXML private TextField Destinatario;
    @FXML private TextField Importo;


    @FXML
    public void initialize() {
        Frequenza.getItems().addAll( "7 giorni", "15 giorni", "1 mese", "3 mesi", "6 mesi", "1 anno");
        List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
        for (Carta carta : carteUtente) {
            CartaUtilizzata.getItems().add(carta.getNumeroCarta()); // Aggiungi il numero della carta alla ComboBox
        }
    }


    @FXML
    public void addProgrammazione(ActionEvent actionEvent) {
        String nome = nomeProgrammazione.getText().trim();      //.trim() serve a rimuovere gli spazi bianchi prima e dopo il testo
        String carta = CartaUtilizzata.getValue();
        String destinatario = Destinatario.getText().trim();
        String importo = Importo.getText().trim();
        String frequenza = Frequenza.getValue();
        Date dataScadenza = null;
        Date dataTermine = null;

        // Controllo se tutti i campi sono stati compilati
        if (nome.isEmpty() || carta == null || destinatario.isEmpty() || importo.isEmpty() ||
                frequenza == null || DataScadenza.getValue() == null || DataTermine.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Compilare tutti i campi");
            return;
        }
        dataScadenza = Date.valueOf(DataScadenza.getValue());
        dataTermine = Date.valueOf(DataTermine.getValue());

        float importoFloat;
        try {
            importo = importo.replace(",", "."); // Gestione della virgola
            importoFloat = Float.parseFloat(importo); // Prova a convertire l'importo in float
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Importo non valido. Inserisci un valore numerico.");
            return; // Se l'importo non è valido, mostra il messaggio di errore e ferma l'operazione
        }

        SpeseProgrammate nuovaSpesa = new SpeseProgrammate(0, frequenza, dataScadenza, Float.parseFloat(importo), destinatario, dataTermine, nome, carta, false);
        SpeseProgrammateDAOimp speseProgrammateDAO = new SpeseProgrammateDAOimp();
        try {
            boolean success = speseProgrammateDAO.insert(nuovaSpesa);
            if (success) {
                Stage stage = (Stage) Conferma.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore", "Errore durante l'aggiunta della spesa programmata");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
