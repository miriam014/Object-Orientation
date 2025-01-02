package smu.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.Portafoglio;

import java.sql.SQLException;


public class EditWalletController extends PortafoglioController {

    private String selectedWalletId;
    private String selectedFamilyId;

    @FXML
    public void initialize() {
        // Rimuovi il focus dalla TextField all'avvio
        nomePortafoglio.setFocusTraversable(false);
        loadFamilyID();
        loadUserWallet();

        IdPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWalletId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Portafoglio selezionato: " + selectedWalletId); // Debugging
            }
        });

        IdFamiglia.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFamilyId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Famiglia selezionata: " + selectedFamilyId); // Debugging
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

}
