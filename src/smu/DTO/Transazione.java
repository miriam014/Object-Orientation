package smu.DTO;

import java.sql.Date;
import java.sql.Time;

public class Transazione {
    private String IDTransazione;
    private String CRO;
    private Float Importo;
    private Date Data;
    private Time Ora;
    private String Causale;
    private String TipoTransazione;
    private String Mittente;
    private String Destinatario;
    private String NumeroCarta;
    private String Categoria;

    public Transazione(String idTransazione, String cro, Float importo, Date data, Time ora, String causale, String tipoTransazione, String mittente, String destinatario, String numeroCarta, String categoria) {
        this.IDTransazione = idTransazione;
        this.CRO = cro;
        this.Importo = importo;
        this.Data = data;
        this.Ora = ora;
        this.Causale = causale;
        this.TipoTransazione = tipoTransazione;
        this.Mittente = mittente;
        this.Destinatario = destinatario;
        this.NumeroCarta = numeroCarta;
        this.Categoria = categoria;
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

    public Date getData() {
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

    public String getCategoria(){ return Categoria; }

    public void setIDTransazione(String IDTransazione) {
        this.IDTransazione = IDTransazione;
    }

    public void setCRO(String CRO) {
        this.CRO = CRO;
    }

    public void setImporto(Float Importo) {
        this.Importo = Importo;
    }

    public void setData(Date Data) {
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

    public void setCategoria(String Categoria){ this.Categoria = Categoria; }

    public String getNumeroCarta() {
        return NumeroCarta;
    }

    public void setNumeroCarta(String NumeroCarta) {
        this.NumeroCarta = NumeroCarta;
    }

    @Override
    public String toString() {
        return "TRANSAZIONE: |IDTransazione = " + IDTransazione + "|\t" + "|CRO = " + CRO + "|\t" + "|Importo = " + Importo + "|\t"
                + "|Data = " + Data + "|\t" + "|Ora = " + Ora + "|\t" + "|Causale = " + Causale + "|\t" + "|TipoTransazione = "
                + TipoTransazione + "|\t" + "|Mittente = " + Mittente + "|\t" + "|Destinatario = " + Destinatario + "|\t";
    }
}
