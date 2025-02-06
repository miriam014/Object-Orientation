package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage; // Riferimento allo stage principale
    private static double currentWidth; // Memorizza la larghezza dell'ultima finestra
    private static double currentHeight; // Memorizza l'altezza dell'ultima finestra
    private static boolean firstPageAfterLogin = true; // Flag per controllare la prima pagina dopo login

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
        if (fxmlLocation == null) {
            throw new IOException("File FXML non trovato");
        }
        Parent root = FXMLLoader.load(fxmlLocation);

        boolean wasFullScreen = primaryStage.isFullScreen(); // Controlla se la finestra era in fullscreen
        primaryStage.setResizable(true);

        if(fxml.equals("login")) {
            currentWidth = 400;
            currentHeight = 350;
            primaryStage.setResizable(false);
            primaryStage.setScene(new Scene(root, 400, 350));
            primaryStage.centerOnScreen();
            firstPageAfterLogin = true; // la metto come vera così può essere modificata
        } else if (fxml.equals("homepage") && firstPageAfterLogin) {
            primaryStage.setScene(new Scene(root, 800, 600));
            currentWidth = 800;
            currentHeight = 600;
            firstPageAfterLogin = false;    // la metto come falsa così non può essere più modificata
        } else {
            primaryStage.setScene(new Scene(root, currentWidth, currentHeight));
            primaryStage.setWidth(currentWidth);
            primaryStage.setHeight(currentHeight);
        }
        // Nasconde il messaggio di uscita dallo schermo intero
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(wasFullScreen);
        if (wasFullScreen) {
            primaryStage.setFullScreen(true);
        }


        boolean inFullScreen = primaryStage.isFullScreen();
        if (!inFullScreen) {
            // Solo se non è in fullscreen, aggiorna le dimensioni
            primaryStage.widthProperty().addListener((_, __, newValue) -> {
                currentWidth = newValue.doubleValue();
            });

            primaryStage.heightProperty().addListener((_, __, newValue) -> {
                currentHeight = newValue.doubleValue();
            });
        }

        // Salva le dimensioni finali della finestra, evitando ridimensionamenti se fullscreen
        currentHeight = primaryStage.getHeight();
        currentWidth = primaryStage.getWidth();

        primaryStage.show();
    }

}





