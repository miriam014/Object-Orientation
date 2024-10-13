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
        primaryStage.setResizable(false);
        setRoot("login"); // Imposta la root della scena di login
    }

    public static void setRoot(String fxml) throws IOException {
        URL fxmlLocation = Main.class.getResource("/interfaccia/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlLocation);

        if (!fxml.equals("login")) {
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(root, 800, 600));
        } else {
            primaryStage.setScene(new Scene(root, 400, 350));
        }
        primaryStage.show();
    }
}


