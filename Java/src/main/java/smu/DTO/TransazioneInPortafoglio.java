package smu.DTO;

public class TransazioneInPortafoglio {
    private String IdTransazione;
    private String IdPortafoglio;

    public TransazioneInPortafoglio(String idTransazione, String idPortafoglio){
        this.IdTransazione = idTransazione;
        this.IdPortafoglio = idPortafoglio;
    }

    public String getIdTransazione() {
        return IdTransazione;
    }

    public String getIdPortafoglio() {
        return IdPortafoglio;
    }

    public void setIdTransazione(String idTransazione) {
        this.IdTransazione = idTransazione;
    }

    public void setIdPortafoglio(String idPortafoglio) {
        this.IdPortafoglio = idPortafoglio;
    }

    @Override
    public String toString() {
        return "TransazioniInPortafogli{" + "IdTransazione=" + IdTransazione + ", IdPortafoglio=" + IdPortafoglio + '}';
    }
}
