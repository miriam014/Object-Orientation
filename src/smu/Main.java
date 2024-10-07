package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        // Carica il file FXML
        URL fxmlLocation = Main.class.getResource("/interfaccia/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlLocation);

        // Controlla se la pagina è di login per decidere il layout
        if (!fxml.equals("login")) {
            // Aggiungi layout solo se NON è la pagina di login
            Menu menu = new Menu(); // Crea un'istanza di Menu
            BorderPane layout = menu.creaLayout(); // Utilizziamo un BorderPane come layout principale
            layout.setCenter(root); // Imposta il contenuto principale della pagina
            primaryStage.setScene(new Scene(layout, 900, 700)); // Imposta la scena
        } else {
            // Se è la pagina di login, impostala direttamente senza layout
            primaryStage.setScene(new Scene(root, 400, 350)); // Imposta la scena con la pagina di login
        }
        primaryStage.show();
    }
}


