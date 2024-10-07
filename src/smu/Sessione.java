package smu;

import smu.DTO.Utente;
import smu.DTO.Carta;
import smu.DAO.CartaDAO;
import smu.DAO_Implementation.CartaDAOimp;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Sessione {

    private static Sessione istanza;
    private Utente utenteLoggato;
    private List<Carta> carte; // Lista delle carte dell'utente

    private Sessione() {
        carte = new ArrayList<>(); // Inizializza la lista delle carte
    }

    // Metodo per ottenere l'istanza del Singleton
    public static Sessione getInstance() {
        if (istanza == null) {
            istanza = new Sessione();
        }
        return istanza;
    }

    // Imposta l'utente autenticato
    public void setUtenteLoggato(Utente utente) {

        this.utenteLoggato = utente;
        loadUserCards(); // Carica le carte dell'utente
    }

    // Recupera l'utente autenticato
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    //vengono recuperate le carte associate all'utente loggato

    private void loadUserCards() {
        if (utenteLoggato != null) { // Verifica che l'utente sia loggato
            CartaDAO cartaDAO = new CartaDAOimp(); // Crea un'istanza del DAO per le carte
            try {
                List<Carta> userCards = cartaDAO.getCardsByUsername(utenteLoggato.getUsername()); // Recupera le carte dell'utente
                carte.clear(); // Svuota la lista delle carte
                carte.addAll(userCards); // Aggiunge le carte dell'utente alla lista
            } catch (SQLException e) {
                e.printStackTrace(); // Stampa lo stack trace in caso di errore
            }
        }
    }

    // Restituisce le carte dell'utente loggato
    public List<Carta> getCarteUtente() {
        return carte; // Restituisce la lista delle carte
    }
}

