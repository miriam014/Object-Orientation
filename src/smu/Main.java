package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class Main extends Application {
    private static Stage primaryStage; // Riferimento allo stage principale

    public static void main(String[] args) {
        launch(args); // Lancia l'applicazione JavaFX
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Main.primaryStage = primaryStage; // Imposta il palco principale
        System.out.println("Avvio del metodo start...");

        //eseguiProve();

        // Configura lo stage principale
        primaryStage.setTitle("Applicazione login"); // Titolo della finestra
        primaryStage.setResizable(false); // Impedisce il ridimensionamento della finestra
        setRoot("login", 400, 350); // Imposta la root della scena di login
    }


    public static void setRoot(String fxml, double width, double height) throws IOException {
        // Carica il file FXML
        URL fxmlLocation = Main.class.getResource("/interfaccia/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlLocation);

        // Cambia la root della scena attuale
        primaryStage.setScene(new Scene(root, width, height)); // Imposta la scena con le dimensioni specificate
        primaryStage.show();
    }


    // Metodo per passare a un'altra interfaccia
    public static void showAnotherInterface(String fxml) throws IOException {
        setRoot(fxml, 800, 600); // Dimensioni per le altre interfacce
    }


    /**
     * Metodo per eseguire le prove iniziali.

     private void eseguiProve() {
     // Prove da cancellare poi
     Utente utente = new Utente("giulia28", "Giulia", "Gargiulo", "3664842648", "giulia@hotmail.it", "1234","1");
     System.out.println(utente.toString());

     Famiglia famiglia = new Famiglia("1", "Gargiulo");
     System.out.println(famiglia.toString());

     ContoCorrente conto = new ContoCorrente("1234", "IT2472", 273.5f, "BancoPosta", "74829");
     System.out.println(conto.toString());

     Carta carta = new Carta("4636810", "PostePay", LocalDate.of(2025,5,6), 100.0f, "Debito", 100.0f, "123");
     System.out.println(carta.toString());

     Categoria categoria = new Categoria("Alimentari", "Cibo");
     System.out.println(categoria.toString());
     }
     */
}


