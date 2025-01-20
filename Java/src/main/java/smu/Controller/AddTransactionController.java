package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import smu.DAO_Implementation.CategoriaDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Carta;
import smu.DTO.Transazione;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class AddTransactionController extends HomepageController{
    @FXML ComboBox<String> tipoTransazione;
    @FXML TextField nuovoImporto;
    @FXML ComboBox<String> valuta;
    @FXML DatePicker nuovaData;
    @FXML ComboBox<String> nuovaCategoria;
    @FXML TextField nuovaCausale;
    @FXML TextField nuovoDaA;
    @FXML ComboBox<String> numeroCarta;

    @FXML
    public void initialize() {

        tipoTransazione.setItems(FXCollections.observableArrayList("Entrata", "Uscita"));
        valuta.setItems(FXCollections.observableArrayList("EUR", "USD"));

        try {
            CategoriaDAOimp categoriaDAO = new CategoriaDAOimp();
            List<String> categorie = categoriaDAO.getAllCategorie();
            nuovaCategoria.setItems(FXCollections.observableArrayList(categorie));
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Carta> carteUtente = Sessione.getInstance().getCarteUtente();
        ObservableList<String> numeriCarte = FXCollections.observableArrayList();
        for (Carta carta : carteUtente) {
            numeriCarte.add(carta.getNumeroCarta());
        }
        numeroCarta.setItems(numeriCarte);
    }

    @FXML
    private void saveTransaction(ActionEvent event) {
        String mittente  = null;
        String destinatario = null;

        // Validazione dei campi
        if (!validateInputs()) {
            return; // Blocca l'esecuzione in caso di errori
        }

        if("Uscita".equals((String) tipoTransazione.getValue())){
            destinatario = nuovoDaA.getText(); //Solo per le transazioni in uscita
            if (destinatario.isEmpty()){
                showError("Il destinatario non può essere nullo per una transazione di uscita.");
                return;  // Blocca l'inserimento se il mittente è vuoto
            }
        } else if ("Entrata".equals((String) tipoTransazione.getValue())){
            mittente= nuovoDaA.getText();
            if (mittente.isEmpty()){
                showError("Il mittente non può essere nullo per una transazione in entrata");
                return;
            }
        }
        try {
            float amount = Float.parseFloat(nuovoImporto.getText());
            String selectedValuta = (String)valuta.getValue();

            float importo = convertToEuro(amount, selectedValuta);
            // Crea l'oggetto Transazione utilizzando il costruttore completo
            Transazione nuovaTransazione = new Transazione(
                    null, // IDTransazione (generato dal database)
                    null, // CRO (generato dal database)
                    importo, // Importo
                    java.sql.Date.valueOf(nuovaData.getValue()), // Data
                    java.sql.Time.valueOf(java.time.LocalTime.now()), // Ora corrente
                    nuovaCausale.getText(), // Causale
                    (String) tipoTransazione.getValue(), // Tipo Transazione
                    mittente, //mittente (solo per entrate)
                    destinatario, //destinatario (solo per uscite)
                    (String) numeroCarta.getValue(), // Numero Carta
                    (String) nuovaCategoria.getValue() // Categoria
            );

            // Inserimento nel database
            System.out.println("tentativo di inserire la transazione: " +nuovaTransazione);
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            boolean success = transazioneDAO.insert(nuovaTransazione);
            System.out.println("inserimento riuscito: " + success);

            if (success) {
                closePopup(event); // Chiudi il popup
                if(transactionsTableView != null) {
                ObservableList<Transazione> currentItems = transactionsTableView.getItems();
                currentItems.add(nuovaTransazione);
                transactionsTableView.setItems(currentItems);// Aggiorna la tabella nella homepage
                } else {
                    System.out.println("transactionsTableView non è stato inizializzato correttamente");
                }
            } else {
                showError("Errore durante l'inserimento della transazione.");
            }
        } catch (NumberFormatException e) {
            showError("Formato dell'importo non valido.");
        } catch (SQLException e) {
            showError("Errore: uno dei dati inseriti non è corretto.");
        } catch (Exception e) {
            showError("Errore sconosciuto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        if (tipoTransazione.getValue() == null ||
                nuovoImporto.getText().isEmpty() ||
                nuovaData.getValue() == null ||
                nuovaCausale.getText().isEmpty() ||
                numeroCarta.getValue() == null ||
                nuovaCategoria.getValue() == null) {
            showError("Tutti i campi obbligatori devono essere compilati.");
            return false;
        }

        //controlla che l'importo sia un numero validot

        try {
        Float.parseFloat(nuovoImporto.getText());
    } catch (NumberFormatException e) {
        showError("L'importo deve essere un numero valido.");
        return false;
    }

    return true;
}

private float convertToEuro (float importoInEuro, String valutaCorrente) {
        if("USD".equals(valutaCorrente)){
            float result = importoInEuro /1.06f;
            return Math.round(result * 100.0f) / 100.0f; // Arrotondamento a 2 cifre decimali
        }
        return importoInEuro;
}

private void closePopup(ActionEvent event) {
    ((Button) event.getSource()).getScene().getWindow().hide();
}

}
