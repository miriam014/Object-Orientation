package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.AssociazioneCartaPortafoglio;
import smu.DTO.Famiglia;
import smu.DTO.Portafoglio;
import smu.Sessione;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddWalletController extends PortafoglioController{

    private String selectedFamilyId;
    private String selectedCardNumber;

    @FXML
    public void initialize() {
        // Rimuovi il focus dalla TextField all'avvio
        nomePortafoglio.setFocusTraversable(false);
        loadFamilyID();
        loadUserCards();

        IdFamiglia.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFamilyId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Famiglia selezionata: " + selectedFamilyId); // Debugging
            }
        });

        NumeroCarta.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCardNumber = newValue; // Salva il numero della carta selezionata
                System.out.println("Carta selezionata: " + selectedCardNumber); // Debugging
            }
        });

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

        System.out.println("Creazione del portafoglio '" + walletName + " associato alla carta "+ selectedCardNumber + "' per la famiglia con ID: " + selectedFamilyId);

        PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
        AssociazioneCartaPortafoglioDAO associazioneDAO = new AssociazioneCartaPortafoglioDAOimp();
        Portafoglio wallet = new Portafoglio(walletName, selectedFamilyId);

        portafoglioDAO.insert(wallet);
        AssociazioneCartaPortafoglio associazione = new AssociazioneCartaPortafoglio(Integer.parseInt(wallet.getIdPortafoglio()), selectedCardNumber);
        associazioneDAO.insert(associazione);

        // Chiudi la finestra corrente
        Stage stage = (Stage) nomePortafoglio.getScene().getWindow();
        stage.close();
    }

}
