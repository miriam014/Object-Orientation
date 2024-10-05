package smu;
import smu.DTO.Utente;

public class Sessione {

    private static Sessione istanza;
    private Utente utenteLoggato;

    private Sessione() {}

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
    }

    // Recupera l'utente autenticato
    public Utente getUtenteLoggato() {
        return utenteLoggato;
    }

    // Aggiungi altri metodi per gestire dati associati (es: transazioni, etc.)
}
