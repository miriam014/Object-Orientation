package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import smu.Controller.MenuController;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage; // Riferimento allo stage principale

    public static void main(String[] args) {
        launch(args); // Lancia l'applicazione JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage; // Imposta il palco principale
        System.out.println("Avvio del metodo start...");

        // Configura lo stage principale
        primaryStage.setTitle("Applicazione login"); // Titolo della finestra
        primaryStage.setResizable(false); // Impedisce il ridimensionamento della finestra
        setRoot("login"); // Imposta la root della scena di login
    }

    public static void setRoot(String fxml) throws IOException {
        URL fxmlLocation = Main.class.getResource("/interfaccia/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlLocation);

        if (!fxml.equals("login")) {
            // Carica il menu (menu.fxml) e il suo controller
            FXMLLoader menuLoader = new FXMLLoader(Main.class.getResource("/interfaccia/menu.fxml"));
            Parent menu = menuLoader.load();
            MenuController menuController = menuLoader.getController(); // Ottieni il controller del menu

            // Crea un layout principale (StackPane)
            StackPane layout = new StackPane();
            layout.getChildren().addAll(menu, root); // Aggiungi il contenuto principale e il menu

            primaryStage.setResizable(true); // Impedisce il ridimensionamento della finestra
            primaryStage.setScene(new Scene(layout, 800, 600)); // Imposta la scena
        } else {
            primaryStage.setScene(new Scene(root, 400, 350)); // Imposta la scena con la pagina di login
        }
        primaryStage.show();
    }
}


