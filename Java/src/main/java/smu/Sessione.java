package smu;

import smu.DAO.FamigliaDAO;
import smu.DAO.PortafoglioDAO;
import smu.DAO_Implementation.FamigliaDAOimp;
import smu.DAO_Implementation.PortafoglioDAOimp;
import smu.DTO.Famiglia;
import smu.DTO.Portafoglio;
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
    private final List<Carta> carte;// Lista delle carte dell'utente
    private List<Portafoglio> portafogli;
    private List<Famiglia> famiglie;
    private Carta cartaSelezionata; // Carta attualmente selezionata

    private Sessione() {
        carte = new ArrayList<>();// Inizializza la lista delle carte
        portafogli = new ArrayList<>();
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
        loadUserCards();
        loadUserWallets();
        loadUserFamily();
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

    private void loadUserWallets() {
        if (utenteLoggato != null) { // Verifica che l'utente sia loggato
            PortafoglioDAO portafoglioDAO = new PortafoglioDAOimp(); // Crea un'istanza del DAO per le carte
            try {
                List<Portafoglio> userWallets = portafoglioDAO.getByUsername(utenteLoggato.getUsername()); // Recupera le carte dell'utente
                portafogli.clear(); // Svuota la lista delle carte
                portafogli.addAll(userWallets); // Aggiunge le carte dell'utente alla lista
            } catch (SQLException e) {
                e.printStackTrace(); // Stampa lo stack trace in caso di errore
            }
        }
    }

    private void loadUserFamily() {
        if (utenteLoggato != null) {
            try {
                // Inizializza la lista delle famiglie, se necessario
                if (famiglie == null) {
                    famiglie = new ArrayList<>();
                }

                // Recupera le famiglie associate all'utente dal DAO
                FamigliaDAO famigliaDAO = new FamigliaDAOimp();
                List<Famiglia> userFamily = famigliaDAO.getByUsername(utenteLoggato.getUsername());

                // Controlla se ci sono famiglie associate e aggiorna la lista
                if (userFamily == null || userFamily.isEmpty()) {
                    System.out.println("Nessuna famiglia trovata per l'utente.");
                    famiglie.clear(); // Assicurati che la lista sia vuota
                } else {
                    famiglie.clear(); // Pulisci la lista esistente
                    famiglie.addAll(userFamily); // Aggiungi le nuove famiglie
                    System.out.println("Famiglie caricate: ");
                    for (Famiglia famiglia : famiglie) {
                        System.out.println("ID Famiglia: " + famiglia.getIdFamiglia());
                    }
                }
            } catch (SQLException e) {
                System.out.println("Errore durante il caricamento delle famiglie: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Restituisce le carte dell'utente loggato
    public List<Carta> getCarteUtente() {
        return carte; // Restituisce la lista delle carte
    }

    public List<Portafoglio> getPortafogliUtente(){
        loadUserWallets();
        return portafogli;
    }

    public List<Famiglia> getFamilyByUsername(){
        return famiglie;
    }

    // Imposta la carta selezionata dall'utente in quel momento
    public void setCartaSelezionata(Carta carta) {
        this.cartaSelezionata = carta;
    }

    // Recupera la carta attualmente in uso
    public Carta getCartaSelezionata() {
        return cartaSelezionata;
    }

}

