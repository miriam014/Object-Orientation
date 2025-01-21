package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import smu.Controller.MenuController;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage; // Riferimento allo stage principale
    private static double lastWidth = 800; // Memorizza la larghezza dell'ultima finestra
    private static double lastHeight = 600; // Memorizza l'altezza dell'ultima finestra
    private static boolean FirstPageAfterLogin = true; // Flag per controllare la prima pagina dopo login
    private static boolean isFullScreen = false; // Flag per verificare se la finestra è in modalità schermo intero

    public static void main(String[] args) {
        launch(args); // Lancia l'applicazione JavaFX
    }

    public static Window getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage; // Imposta il palco principale
        System.out.println("Avvio del metodo start...");

        // Configura lo stage principale
        primaryStage.setTitle("Applicazione login"); // Titolo della finestra
        setRoot("login"); // Imposta la root della scena di login
    }


    public static void setRoot(String fxml) throws IOException {
        URL fxmlLocation = Main.class.getResource("/interfaccia/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlLocation);

        if(fxml.equals("login")) {
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 400, 350));
            primaryStage.centerOnScreen();
            FirstPageAfterLogin = true; // la metto come vera così può essere modificata
        } else if (fxml.equals("homepage") && FirstPageAfterLogin) {
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(root, 800, 600));
            lastWidth = 800; // Memorizza la larghezza
            lastHeight = 600; // Memorizza l'altezza
            FirstPageAfterLogin = false;    // la metto come falsa così non può essere più modificata
        } else {
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(root, lastWidth, lastHeight));
        }

        // Gestisci modalità schermo intero
        primaryStage.setFullScreenExitHint(""); // Nasconde il hint per uscire dal fullscreen
        primaryStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Quando si entra in modalità schermo intero, memorizza la posizione e le dimensioni correnti
                isFullScreen = true;
                lastWidth = primaryStage.getWidth();
                lastHeight = primaryStage.getHeight();
            } else {
                // Quando si esce dal fullscreen, ripristina le dimensioni
                isFullScreen = false;
                primaryStage.setWidth(lastWidth);
                primaryStage.setHeight(lastHeight);
            }
        });

        // Dopo aver impostato la scena, aggiorniamo le dimensioni solo se la finestra non è in schermo intero
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if (!isFullScreen) {
                // Aggiorna le dimensioni solo quando la finestra non è a schermo intero
                lastWidth = newValue.doubleValue();
            }
        });

        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (!isFullScreen && (primaryStage.getHeight() != oldValue.doubleValue())) {
                // Aggiorna le dimensioni solo quando la finestra non è a schermo intero
                lastHeight = newValue.doubleValue();
            }
        });

        primaryStage.show();
    }

}





