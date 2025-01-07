package smu.Controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Controller {

    protected void showDialog(String fxmlPath, Button button, String title) {
        try {
            // Carica il file FXML
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Errore: file FXML non trovato: " + fxmlPath);
                return;
            }

            Parent root = FXMLLoader.load(resource);
            System.out.println("File FXML caricato correttamente!");

            // Recupera lo stage principale
            Stage primaryStage = (Stage) button.getScene().getWindow();

            // Configura la finestra modale
            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);
            modalStage.initOwner(primaryStage);
            modalStage.initModality(Modality.APPLICATION_MODAL);

            // Mostra la finestra modale
            modalStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento del file FXML!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Errore inatteso durante l'apertura della finestra.");
            e.printStackTrace();
        }

    }
}
