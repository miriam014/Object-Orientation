package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import smu.DAO.SpeseProgrammateDAO;
import smu.DAO_Implementation.SpeseProgrammateDAOimp;
import smu.DTO.SpeseProgrammate;
import smu.Sessione;

import java.sql.SQLException;
import java.util.List;

public class changeProgrammazioneController extends Controller {

    @FXML private ComboBox<String> nomeProgrammazione;
    @FXML private ComboBox<String> CartaUtilizzata;
    @FXML private ComboBox<String> Destinatario;
    @FXML private TextField Importo;
    @FXML private ComboBox<String> Frequenza;
    @FXML private DatePicker DataScadenza;
    @FXML private DatePicker DataTermine;
    @FXML private Button Conferma;
    private SpeseProgrammateDAO speseProgrammateDAO;



    @FXML
    public void initialize() throws SQLException {
        speseProgrammateDAO = new SpeseProgrammateDAOimp();
        String username = Sessione.getInstance().getUtenteLoggato().getUsername();
        List<SpeseProgrammate> ListaSpese = speseProgrammateDAO.getByUsername(username);

        rimepiComboBox(ListaSpese);
    }


    @FXML
    private void rimepiComboBox(List<SpeseProgrammate> ListaSpese) {
        for (SpeseProgrammate speseProgrammate : ListaSpese) {
            nomeProgrammazione.getItems().add(speseProgrammate.getDescrizione());
        }
        //una volta scelto il nome riempi tutte le restanti celle e dai la possibiità di cambiare

        nomeProgrammazione.setOnAction(event -> {
            String selectedDescrizione = nomeProgrammazione.getValue();
            SpeseProgrammate sp = ListaSpese.stream()
                    .filter(s -> s.getDescrizione().equals(selectedDescrizione))
                    .findFirst()
                    .orElse(null);
            if (sp != null) {   //trovata la spesa selezionata recupero i dati
                CartaUtilizzata.setValue(sp.getNumeroCarta());
                Destinatario.setValue(sp.getDestinatario());
                Importo.setText(String.valueOf(sp.getImporto()));
                Frequenza.setValue(sp.getPeriodicita());
                if (DataScadenza != null){ DataScadenza.setValue(sp.getDataScadenza().toLocalDate());}
                if(DataTermine != null) { DataTermine.setValue(sp.getFineRinnovo().toLocalDate());}
            }
        });
    }


    @FXML
    public void changeProgrammazione(ActionEvent actionEvent) {
        /*String nomeSpesa = nomeProgrammazione.getValue();
        String cartaUtilizzata = CartaUtilizzata.getValue();
        String destinatario = Destinatario.getValue();
        double importo = Double.parseDouble(Importo.getText());
        String frequenza = Frequenza.getValue();
        java.sql.Date dataScadenza = java.sql.Date.valueOf(DataScadenza.getValue());
        java.sql.Date dataTermine = java.sql.Date.valueOf(DataTermine.getValue());

        // Crea un nuovo oggetto SpeseProgrammate con i dati modificati
        SpeseProgrammate spesaModificata = new SpeseProgrammate();
        spesaModificata.setDescrizione(nomeSpesa);
        spesaModificata.setNumeroCarta(cartaUtilizzata);
        spesaModificata.setDestinatario(destinatario);
        spesaModificata.setImporto((float) importo);
        spesaModificata.setPeriodicita(frequenza);
        spesaModificata.setDataScadenza(dataScadenza);
        spesaModificata.setFineRinnovo(dataTermine);

        // Salva o aggiorna la spesa nel database (da implementare nel DAO)
        try {
            speseProgrammateDAO.update(spesaModificata);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

}
