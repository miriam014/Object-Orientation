package smu.DTO;

import java.sql.Time;
import java.time.LocalDate;

public class Transazione {
    private String IDTransazione;
    private String CRO;
    private Float Importo;
    private LocalDate Data;
    private Time Ora;
    private String Causale;
    private String TipoTransazione;
    private String Mittente;
    private String Destinatario;

    public Transazione(String IDTransazione, String CRO, Float Importo, LocalDate Data, Time Ora, String Causale, String TipoTransazione, String Mittente, String Destinatario) {
        this.IDTransazione = IDTransazione;
        this.CRO = CRO;
        this.Importo = Importo;
        this.Data = Data;
        this.Ora = Ora;
        this.Causale = Causale;
        this.TipoTransazione = TipoTransazione;
        this.Mittente = Mittente;
        this.Destinatario = Destinatario;
    }

    public String getIDTransazione() {
        return IDTransazione;
    }

    public String getCRO() {
        return CRO;
    }

    public Float getImporto() {
        return Importo;
    }

    public LocalDate getData() {
        return Data;
    }

    public Time getOra() {
        return Ora;
    }

    public String getCausale() {
        return Causale;
    }

    public String getTipoTransazione() {
        return TipoTransazione;
    }

    public String getMittente() {
        return Mittente;
    }

    public String getDestinatario() {
        return Destinatario;
    }

    public void setIDTransazione(String IDTransazione) {
        this.IDTransazione = IDTransazione;
    }

    public void setCRO(String CRO) {
        this.CRO = CRO;
    }

    public void setImporto(Float Importo) {
        this.Importo = Importo;
    }

    public void setData(LocalDate Data) {
        this.Data = Data;
    }

    public void setOra(Time Ora) {
        this.Ora = Ora;
    }

    public void setCausale(String Causale) {
        this.Causale = Causale;
    }

    public void setTipoTransazione(String TipoTransazione) {
        this.TipoTransazione = TipoTransazione;
    }

    public void setMittente(String Mittente) {
        this.Mittente = Mittente;
    }

    public void setDestinatario(String Destinatario) {
        this.Destinatario = Destinatario;
    }

    @Override
    public String toString() {
        return "TRANSAZIONE: |IDTransazione = " + IDTransazione + "|\t" + "|CRO = " + CRO + "|\t" + "|Importo = " + Importo + "|\t"
                + "|Data = " + Data + "|\t" + "|Ora = " + Ora + "|\t" + "|Causale = " + Causale + "|\t" + "|TipoTransazione = "
                + TipoTransazione + "|\t" + "|Mittente = " + Mittente + "|\t" + "|Destinatario = " + Destinatario + "|\t";
    }
}
