package smu.Controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.Portafoglio;

import java.sql.SQLException;

public class DeletePortafoglioController extends PortafoglioController {

    private String selectedWalletId;


    @FXML
    public void initialize() {
        loadUserWallet();

        nomePortafoglio.setDisable(true);
        Conferma.setDisable(true);

        IdPortafoglio.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedWalletId = newValue; // Salva l'ID della famiglia selezionata
                System.out.println("Portafoglio selezionato: " + selectedWalletId); // Debugging
                loadWalletInfo();

                nomePortafoglio.setDisable(false);
                Conferma.setDisable(false);
            }
        });
    }

    @FXML
    public void removeWallet() throws SQLException {

        if (selectedWalletId == null || selectedWalletId.trim().isEmpty()) {
            System.out.println("ERRORE: seleziona un ID portafoglio.");
            return;
        }
        System.out.println("Modifica del portafoglio con ID:" + selectedWalletId);

        PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp();
        portafoglioDAO.delete(selectedWalletId);

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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
