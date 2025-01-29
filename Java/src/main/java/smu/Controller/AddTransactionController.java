package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import smu.DAO_Implementation.CategoriaDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Carta;
import smu.DTO.Transazione;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class AddTransactionController extends HomepageController {
    @FXML private Button confermaButton;
    @FXML private ComboBox<String> tipoTransazione;
    @FXML private TextField nuovoImporto;
    @FXML private ComboBox<String> valuta;
    @FXML private DatePicker nuovaData;
    @FXML private ComboBox<String> nuovaCategoria;
    @FXML private TextField nuovaCausale;
    @FXML private TextField nuovoDaA;
    @FXML private ComboBox<String> numeroCarta;

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


            Transazione nuovaTransazione = new Transazione(null, null, importo, java.sql.Date.valueOf(nuovaData.getValue()), java.sql.Time.valueOf(java.time.LocalTime.now()),
                    nuovaCausale.getText(), (String) tipoTransazione.getValue(), mittente, destinatario, (String) numeroCarta.getValue(), (String) nuovaCategoria.getValue()
            );
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            boolean success = transazioneDAO.insert(nuovaTransazione);
            if (success) {
                Stage stage = (Stage) confermaButton.getScene().getWindow();
                stage.close();
                System.out.println("inserimento riuscito: ");
            } else {
                showError("Errore durante l'inserimento della transazione.");
            }
        } catch (NumberFormatException e) {
            showError("Formato dell'importo non valido.");
        } catch (SQLException e) {
            showError("Errore: uno dei dati inseriti non è corretto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        if (tipoTransazione.getValue() == null || nuovoImporto.getText().isEmpty() || nuovaData.getValue() == null ||
                nuovaCausale.getText().isEmpty() || numeroCarta.getValue() == null) {
            showError("Tutti i campi obbligatori devono essere compilati.");
            return false;
        }

        //controlla che l'importo sia un numero valido
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

}
