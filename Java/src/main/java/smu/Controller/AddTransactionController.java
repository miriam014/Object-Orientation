package smu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import smu.DAO_Implementation.CategoriaDAOimp;

import java.util.List;

public class AddTransactionController extends HomepageController{
    @FXML ComboBox TipoTransazione;
    @FXML TextField NuovoImporto;
    @FXML ComboBox Valuta;
    @FXML DatePicker NuovaData;
    @FXML ComboBox NuovaCategoria;
    @FXML TextField NuovaCausale;
    @FXML TextField NuovoDaA;

    @FXML
    public void initialize() {
        TipoTransazione.setItems(FXCollections.observableArrayList("Entrata", "Uscita"));
        Valuta.setItems(FXCollections.observableArrayList("EUR", "USD"));

        try {
            CategoriaDAOimp categoriaDAO = new CategoriaDAOimp();
            List<String> categorie = categoriaDAO.getAllCategorie();
            NuovaCategoria.setItems(FXCollections.observableArrayList(categorie));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
