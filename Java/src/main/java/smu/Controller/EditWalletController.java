package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.AssociazioneCartaPortafoglio;
import smu.DTO.Portafoglio;

import java.sql.SQLException;



public class EditWalletController extends PortafoglioController {

    private String selectedWalletId;
    private String selectedFamilyId;
    private String selectedCardNumber;

    @FXML
    public void initialize() {
        //TODO: quando modifico un portafoglio devo visualizzare i dati attuali
        //TODO: uso il metodo getbyID, faccio una chiamata al dao per avere i dati, e li inserisco nelle textfield

        nomePortafoglio.setFocusTraversable(false);
        loadUserWallet();

        nomePortafoglio.setDisable(true);
        IdFamiglia.setDisable(true);
        NumeroCarta.setDisable(true);
        Conferma.setDisable(true);


        IdPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWalletId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Portafoglio selezionato: " + selectedWalletId); // Debugging
                loadWalletInfo();
                loadFamilyID();
                loadUserCards();

                nomePortafoglio.setDisable(false);
                IdFamiglia.setDisable(false);
                NumeroCarta.setDisable(false);
                Conferma.setDisable(false);
            }

        });

        IdFamiglia.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFamilyId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Famiglia selezionata: " + selectedFamilyId); // Debugging
            }
        });

        NumeroCarta.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedCardNumber = newValue; // Salva il numero della carta selezionata
                System.out.println("Carta selezionata: " + selectedCardNumber);// Debugging
            }
        });
    }


    @FXML
    public void editWallet() throws SQLException {

        if (selectedWalletId == null || selectedWalletId.trim().isEmpty()) {
            System.out.println("ERRORE: seleziona un ID portafoglio.");
            return;
        }

        String walletName = nomePortafoglio.getText();

        if ((walletName == null || walletName.trim().isEmpty()) && (selectedFamilyId == null || selectedFamilyId.trim().isEmpty())) {
            System.out.println("Modifica il Nome o l'ID Famiglia del portafoglio.");
            return;
        }

        System.out.println("Modifica del portafoglio con ID:" + selectedWalletId);

        PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
        Portafoglio wallet = new Portafoglio(selectedWalletId, walletName, selectedFamilyId);
        portafoglioDAO.update(wallet);

        loadUserWallet();

        // Chiudi la finestra corrente
        Stage stage = (Stage) nomePortafoglio.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void loadWalletInfo() {

        if (selectedWalletId == null || selectedWalletId.trim().isEmpty()) {
            System.out.println("Nessun portafoglio selezionato per il caricamento delle informazioni.");
            return;
        }

        try {
            PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
            Portafoglio wallet = portafoglioDAO.getByID(selectedWalletId);

            if (wallet != null) {
                nomePortafoglio.setText(wallet.getNomePortafoglio());
                selectedFamilyId = wallet.getIdFamiglia();
                selectedCardNumber = portafoglioDAO.getCardNumberByWalletID(selectedWalletId);

                IdFamiglia.getSelectionModel().select(selectedFamilyId);
                NumeroCarta.getSelectionModel().select(selectedCardNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
