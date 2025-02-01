package smu.Controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.Portafoglio;

import java.sql.SQLException;



public class EditPortafoglioController extends PortafoglioController {

    private String selectedWalletId;
    private String selectedCardNumber;

    @FXML
    public void initialize() {
        nomePortafoglio.setFocusTraversable(false);
        loadUserWallet();

        nomePortafoglio.setDisable(true);
        NumeroCarta.setDisable(true);
        Conferma.setDisable(true);


        IdPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWalletId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Portafoglio selezionato: " + selectedWalletId); // Debugging
                loadWalletInfo();
                loadUserCards();

                nomePortafoglio.setDisable(false);
                NumeroCarta.setDisable(false);
                Conferma.setDisable(false);
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

        if ((walletName == null || walletName.trim().isEmpty())) {
            System.out.println("Modifica il Nome del portafoglio.");
            return;
        }

        System.out.println("Modifica del portafoglio con ID:" + selectedWalletId);

        PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
        Portafoglio wallet = new Portafoglio(selectedWalletId, walletName, null);
        portafoglioDAO.update(wallet);

        // Chiudi la finestra corrente
        Stage stage = (Stage) nomePortafoglio.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loadWalletInfo() {

        if (selectedWalletId == null || selectedWalletId.trim().isEmpty()) {
            System.out.println("Nessun portafoglio selezionato per il caricamento delle informazioni.");
            return;
        }

        try {
            PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
            Portafoglio wallet = portafoglioDAO.getByID(selectedWalletId);

            if (wallet != null) {
                nomePortafoglio.setText(wallet.getNomePortafoglio());
                selectedCardNumber = portafoglioDAO.getCardNumberByWalletID(selectedWalletId);

                NumeroCarta.getSelectionModel().select(selectedCardNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
