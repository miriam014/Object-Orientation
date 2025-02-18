package smu.Controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO.PortafoglioDAO;
import smu.DAOImplementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAOImplementation.PortafoglioDAOimp;
import smu.DTO.AssociazioneCartaPortafoglio;
import smu.DTO.Portafoglio;

import java.sql.SQLException;

public class AddPortafoglioController extends PortafoglioController{

    private String selectedCardNumber;

    @FXML
    public void initialize() {
        // Rimuovi il focus dalla TextField all'avvio
        nomePortafoglio.setFocusTraversable(false);
        loadUserCards();
        Conferma.setDisable(true);

        nomePortafoglio.textProperty().addListener((observable, oldValue, newValue) -> {
            checkFormValidity(); // Verifica la validità del modulo ogni volta che il nome cambia
        });


        NumeroCarta.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCardNumber = newValue; // Salva il numero della carta selezionata
                System.out.println("Carta selezionata: " + selectedCardNumber); // Debugging
                checkFormValidity();
            }
        });
    }

    private void checkFormValidity() {
        // Controlla se il nome del portafoglio è stato inserito e se è stata selezionata una carta
        if (nomePortafoglio.getText() != null && !nomePortafoglio.getText().trim().isEmpty() &&
                selectedCardNumber != null && !selectedCardNumber.trim().isEmpty()) {
            Conferma.setDisable(false); // Abilita il bottone se entrambi i campi sono validi
        }
    }

    @FXML
    public void addWallet() throws SQLException {
        String walletName = nomePortafoglio.getText();

        if (walletName == null || walletName.trim().isEmpty()) {
            System.out.println("Inserisci un nome per il portafoglio.");
            return;
        }

        if(selectedCardNumber == null || selectedCardNumber.trim().isEmpty()) {
            System.out.println("Seleziona una carta per il portafoglio.");
            return;
        }

        System.out.println("Creazione del portafoglio '" + walletName + " associato alla carta "+ selectedCardNumber);

        PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
        AssociazioneCartaPortafoglioDAO associazioneDAO = new AssociazioneCartaPortafoglioDAOimp();
        Portafoglio wallet = new Portafoglio(walletName, null);

        portafoglioDAO.insert(wallet);
        AssociazioneCartaPortafoglio associazione = new AssociazioneCartaPortafoglio(Integer.parseInt(wallet.getIdPortafoglio()), selectedCardNumber);
        associazioneDAO.insert(associazione);

        // Chiudi la finestra corrente
        Stage stage = (Stage) nomePortafoglio.getScene().getWindow();
        stage.close();
    }

}
