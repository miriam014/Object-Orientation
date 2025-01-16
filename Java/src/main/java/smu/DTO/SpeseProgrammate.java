package smu.DTO;

import javafx.scene.control.Button;

import java.sql.Time;
import java.sql.Date;

public class SpeseProgrammate {

    private int IdSpesa;
    private String Periodicita;
    private Date DataScadenza;
    private float Importo;
    private String Destinatario;
    private Date FineRinnovo;
    private String Descrizione;
    private String NumeroCarta;
    private Button Stato;


    public SpeseProgrammate(int idSpesa, String periodicita, Date dataScadenza, float importo, String destinatario, Date fineRinnovo, String descrizione, String numeroCarta){
        this.IdSpesa = idSpesa;
        this.Periodicita = periodicita;
        this.DataScadenza = dataScadenza;
        this.Importo = importo;
        this.Destinatario = destinatario;
        this.FineRinnovo = fineRinnovo;
        this.Descrizione = descrizione;
        this.NumeroCarta = numeroCarta;
        this.Stato = new Button("Paga");
    }


    public int getIdSpesa(){ return IdSpesa; }

    public String getPeriodicita(){ return Periodicita; }

    public Date getDataScadenza(){
        return DataScadenza;
    }

    public float getImporto(){
        return Importo;
    }

    public String getDestinatario(){
        return Destinatario;
    }

    public Date getFineRinnovo(){
        return FineRinnovo;
    }

    public String getDescrizione(){
        return Descrizione;
    }

    public String getNumeroCarta(){
        return NumeroCarta;
    }

    public Button getStato(){ return Stato; }



    public void setIdSpesa(int idSpesa){
        this.IdSpesa = idSpesa;
    }

    public void setPeriodicita(String periodicita){
        this.Periodicita = periodicita;
    }

    public void setDataScadenza(Date dataScadenza){
        this.DataScadenza = dataScadenza;
    }

    public void setImporto(float importo){
        this.Importo = importo;
    }

    public void setDestinatario(String destinatario){
        this.Destinatario = destinatario;
    }

    public void setFineRinnovo(Date fineRinnovo){
        this.FineRinnovo = fineRinnovo;
    }

    public void setDescrizione(String descrizione){
        this.Descrizione = descrizione;
    }

    public void setNumeroCarta(String numeroCarta){
        this.NumeroCarta = numeroCarta;
    }

    public void setStato(Button stato){
        this.Stato = stato;
    }


    @Override
    public String toString(){
        return "SPESA PROGRAMMATA: |IdSpesa = "+ IdSpesa + "|\t" + "|Periodicità = " + Periodicita + "|\t" + "|DataScadenza = "
                + DataScadenza + "|\t" + "|Importo = " + Importo + "|\t" + "|Destinatario = " + Destinatario + "|\t" +
                "|FineRinnovo = " + FineRinnovo + "|\t" + "|Descrizione = " + Descrizione + "|";
    }
}
