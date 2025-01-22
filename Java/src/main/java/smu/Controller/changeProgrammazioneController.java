package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAO_Implementation.SpeseProgrammateDAOimp;
import smu.DTO.Carta;
import smu.DTO.SpeseProgrammate;
import smu.Sessione;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class changeProgrammazioneController extends Controller {

    @FXML private ComboBox<String> nomeProgrammazione;
    @FXML private ComboBox<String> CartaUtilizzata;
    @FXML private TextField Destinatario;
    @FXML private TextField Importo;
    @FXML private ComboBox<String> Frequenza;
    @FXML private DatePicker DataScadenza;
    @FXML private DatePicker DataTermine;
    @FXML private Button Conferma;
    private SpeseProgrammateDAO speseProgrammateDAO;

    private List<SpeseProgrammate> ListaSpese;

    @FXML
    public void initialize() throws SQLException {
        speseProgrammateDAO = new SpeseProgrammateDAOimp();
        String username = Sessione.getInstance().getUtenteLoggato().getUsername();
        ListaSpese = speseProgrammateDAO.getByUsername(username);

        rimepiComboBox(ListaSpese);
    }


    @FXML
    private void rimepiComboBox(List<SpeseProgrammate> ListaSpese) {
        //riempio la combobox con le spese programmate
        for (SpeseProgrammate speseProgrammate : ListaSpese) {
            nomeProgrammazione.getItems().add(speseProgrammate.getDescrizione());
        }

        //una volta scelta la spesa programmata riempi tutte le restanti celle con i valoiri corrispondenti
        // e popolale anche degli altri valori possibili
        nomeProgrammazione.setOnAction(event -> {
            String selectedDescrizione = nomeProgrammazione.getValue();
            SpeseProgrammate sp = ListaSpese.stream()
                    .filter(s -> s.getDescrizione().equals(selectedDescrizione))
                    .findFirst()
                    .orElse(null);
            if (sp != null) {   //trovata la spesa selezionata recupero i dati
                CartaUtilizzata.getItems().clear();
                List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
                for (Carta carta : carteUtente) {
                    CartaUtilizzata.getItems().add(carta.getNumeroCarta()); // Aggiungi il numero della carta alla ComboBox
                }
                CartaUtilizzata.setValue(sp.getNumeroCarta());

                Destinatario.setText(sp.getDestinatario());
                Importo.setText(String.valueOf(sp.getImporto()));
                if (DataScadenza != null){ DataScadenza.setValue(sp.getDataScadenza().toLocalDate());}
                if (DataTermine != null){ DataTermine.setValue(sp.getFineRinnovo().toLocalDate());}

                Frequenza.setValue(sp.getPeriodicita());
                Frequenza.getItems().addAll( "7 giorni", "15 giorni", "1 mese", "3 mesi", "6 mesi", "1 anno");
            }
        });
    }


    @FXML
    public void changeProgrammazione(ActionEvent actionEvent) {
        //recupero le modifiche dell'utente
        String nomeSpesa = nomeProgrammazione.getValue();
        if (nomeSpesa == null) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Nessuna spesa selezionata", "Per favore seleziona una spesa programmata prima di premere 'Conferma'.");
            return;
        }
        String cartaUtilizzata = CartaUtilizzata.getValue();
        String destinatario = Destinatario.getText();
        String frequenza = Frequenza.getValue();
        java.sql.Date dataScadenza = java.sql.Date.valueOf(DataScadenza.getValue());
        java.sql.Date dataTermine = java.sql.Date.valueOf(DataTermine.getValue());
        String importoString = Importo.getText();

        String importo = importoString.replace(",", ".");
        float importofloat;
        try {
            importofloat = Float.parseFloat(importo);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Importo non valido", "L'importo inserito non è valido. Inserisci un valore numerico.");
            return;
        }

        // Controllo che le date non siano precedenti alla data attuale
        LocalDate oggi = LocalDate.now();
        if (dataScadenza.toLocalDate().isBefore(oggi)) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Data di rinnovo non valida", "La data di scadenza non può essere precedente alla data attuale.");
            return;
        }
        if (dataTermine.toLocalDate().isBefore(oggi)) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Data di termine rinnovo non valida", "La data di termine rinnovo non può essere precedente alla data attuale.");
            return;
        }

        // Trovo la spesa programmata selezionata dalla lista
        SpeseProgrammate spesaModificata = ListaSpese.stream()
                .filter(sp -> sp.getDescrizione().equals(nomeSpesa)) // Trovo la spesa con la stessa descrizione
                .findFirst()
                .orElse(null);

        // modifico l'oggetto SpeseProgrammate con i dati modificati
        if (spesaModificata != null) {
            spesaModificata.setDescrizione(nomeSpesa);
            spesaModificata.setNumeroCarta(cartaUtilizzata);
            spesaModificata.setDestinatario(destinatario);
            spesaModificata.setImporto(importofloat);
            spesaModificata.setPeriodicita(frequenza);
            spesaModificata.setDataScadenza(dataScadenza);
            spesaModificata.setFineRinnovo(dataTermine);
        }

        // Salva o aggiorna la spesa nel database (da implementare nel DAO)
        try {
            boolean successo = speseProgrammateDAO.update(spesaModificata);
            if (successo) {
                //chiudo direttamente la scheda
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore", "Aggiornamento fallito", "Non è stato possibile aggiornare la spesa programmata.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Funzione generica per mostrare Alert
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
