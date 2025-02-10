package smu.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DAO_Implementation.AssociazioneCartaPortafoglioDAOimp;
import smu.DAO_Implementation.TransazioneDAOimp;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;

import java.util.List;


public class AddTransactionInWalletController extends PortafoglioController {

    @FXML private TableView<Transazione> transactionsTableView;

public void initialize(){
    initializeTableView();
    loadTransactions(selectedWallet.getIdPortafoglio());

}

    private void loadTransactions(String walletId) {
        try {
            TransazioneDAOimp transazioneDAO = new TransazioneDAOimp();
            AssociazioneCartaPortafoglioDAO associazione = new AssociazioneCartaPortafoglioDAOimp();
            String numeroCarta = associazione.getCardNumberByID(walletId);
            List<Transazione> transazioni = transazioneDAO.getByCardNumber(numeroCarta,"Entrata");
            transazioneDAO.getByCardNumber(numeroCarta,"Uscita");

            ObservableList<Transazione> transactionDetails = FXCollections.observableArrayList(transazioni);
            transactionsTableView.setItems(transactionDetails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
