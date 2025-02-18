package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import smu.DAOImplementation.FamigliaDAOimp;
import smu.DAOImplementation.UtentiInFamiglieDAOimp;
import smu.DTO.Famiglia;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class EditFamigliaController extends AddFamigliaController{
    @FXML private TextField nuovoNome;
    @FXML private ComboBox<String> eliminaUtente;
    @FXML private TextField nuovoUtente;
    @FXML private Button Conferma;
    @FXML private ComboBox<String> nomeFamigliadaModificare;


    @FXML
    public void initialize() {
        caricaFamiglie();
        // Se seleziona una famiglia da modificare allora carica gli utenti di quella famiglia
        nomeFamigliadaModificare.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                caricaUtentiDellaFamiglia();
            }
        });
    }

    @FXML
    private void caricaFamiglie(){
        //inizializzo la combobox con i nomi delle famiglie
        List<Famiglia> famiglieUtente = Sessione.getInstance().getFamilyByUsername();
        nomeFamigliadaModificare.getItems().clear();

        for (Famiglia famiglia : famiglieUtente){
            nomeFamigliadaModificare.getItems().add(famiglia.getNomeFamiglia()+ " (ID: " + famiglia.getIdFamiglia() + ")");
        }

    }

    @FXML
    private void caricaUtentiDellaFamiglia(){
        UtentiInFamiglieDAOimp utentiInFamilyDAO = new UtentiInFamiglieDAOimp();
        eliminaUtente.getItems().clear();
        try {
            List<String> utenti = utentiInFamilyDAO.getUsersByFamilyId(getSelectedFamilyId());
            for (String utente : utenti){
                eliminaUtente.getItems().add(utente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modificaFamiglia(ActionEvent actionEvent) {
        Integer idFamiglia = getSelectedFamilyId();

        //controllo prima sia stata selezionata una famiglia
        if (nomeFamigliadaModificare.getValue().isEmpty()) {
            showError("Selezionare una famiglia");
            return;
        }

        if (nuovoNome.getText().isEmpty() && eliminaUtente.getValue() == null && nuovoUtente.getText().isEmpty()) {
            showError("Inserire almeno un campo da modificare");
            return;
        }

        UtentiInFamiglieDAOimp utentiInFamilyDAO = new UtentiInFamiglieDAOimp();
        FamigliaDAOimp famigliaDAO = new FamigliaDAOimp();

        try {
            Famiglia famiglia = famigliaDAO.getByID(idFamiglia);

            if(eliminaUtente.getValue() != null){
                try {
                    utentiInFamilyDAO.removeUserFromFamily(eliminaUtente.getValue(), idFamiglia);
                    System.out.println("Utente eliminato con successo");
                } catch (Exception e) {
                    showError("Errore nell'eliminazione dell'utente");
                }
            }

           if(!nuovoUtente.getText().isEmpty()){
                try {
                    utentiInFamilyDAO.addUserToFamily(idFamiglia, nuovoUtente.getText());
                    System.out.println("Utente aggiunto con successo");
                } catch (Exception e) {
                    showError("Errore nell'aggiunta dell'utente");
                }
           }

            if(!nuovoNome.getText().isEmpty()){
                try {
                    famiglia.setNomeFamiglia(nuovoNome.getText());
                    famigliaDAO.update(famiglia);
                    System.out.println("Nome famiglia modificato con successo");
                } catch (Exception e) {
                    showError("Errore nella modifica del nome della famiglia");
                }
            }

            Sessione.getInstance().loadUserFamily();

            Stage stage = (Stage) Conferma.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showError("Errore nel recupero della famiglia");
        }

    }

    //metodo per ottenere l'id della famiglia selezionata
    private Integer getSelectedFamilyId() {
        String selected = nomeFamigliadaModificare.getValue();
        if (selected == null || selected.isEmpty()) {
            return null;
        }
        String idPart = selected.substring(selected.lastIndexOf("ID: ") + 4).replace(")", "");
        return Integer.parseInt(idPart);
    }

}
