// Starter.java
package smu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import smu.DTO.Famiglia;
import smu.DTO.Utente;
import smu.DTO.Carta;
import smu.DTO.ContoCorrente;
import smu.DTO.Categoria;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

/**
 * Classe Starter dell'applicazione JavaFX.
 * Estende javafx.application.Application e configura lo stage principale.
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args); // Lancia l'applicazione JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Codice di debug
            System.out.println("Avvio del metodo start...");

            // Esecuzione del codice di Main.java
            eseguiProve();

            // Carica il file FXML della schermata di login
            URL fxmlLocation = getClass().getResource("/interfaccia/login.fxml");
            if (fxmlLocation == null) {
                System.out.println("login.fxml non trovato!");
                return;
            } else {
                System.out.println("login.fxml trovato: " + fxmlLocation);
            }

            Parent root = FXMLLoader.load(fxmlLocation);

            // Crea una nuova scena con il layout caricato
            Scene scene = new Scene(root, 448, 399); // Dimensioni della finestra

            // Configura lo stage principale
            primaryStage.setTitle("Applicazione Login"); // Titolo della finestra
            primaryStage.setScene(scene); // Imposta la scena
            primaryStage.setResizable(false); // Impedisce il ridimensionamento della finestra
            primaryStage.show(); // Mostra la finestra

            // Codice di debug
            System.out.println("Stage mostrato correttamente.");

        } catch (IOException e) {
            e.printStackTrace(); // Stampa l'errore
        }
    }

    /**
     * Metodo per eseguire le prove iniziali.
     */
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
}

