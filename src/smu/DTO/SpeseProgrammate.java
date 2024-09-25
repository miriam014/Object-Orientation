package smu.DTO;

import java.time.LocalDate;

public class SpeseProgrammate {

    private String IdSpesa;
    private String Periodicità;
    private LocalDate DataScadenza;
    private float Importo;
    private String Destinatario;
    private LocalDate FineRinnovo;
    private String Descrizione;

    public SpeseProgrammate(String idSpesa, String periodicità, LocalDate dataScadenza, float importo, String destinatario, LocalDate fineRinnovo, String descrizione){
        this.IdSpesa = idSpesa;
        this.Periodicità = periodicità;
        this.DataScadenza = dataScadenza;
        this.Importo = importo;
        this.Destinatario = destinatario;
        this.FineRinnovo = fineRinnovo;
        this.Descrizione = descrizione;
    }

    public String getIdSpesa(){
        return IdSpesa;
    }

    public String getPeriodicità(){
        return Periodicità;
    }

    public LocalDate getDataScadenza(){
        return DataScadenza;
    }

    public float getImporto(){
        return Importo;
    }

    public String getDestinatario(){
        return Destinatario;
    }

    public LocalDate getFineRinnovo(){
        return FineRinnovo;
    }

    public String getDescrizione(){
        return Descrizione;
    }

    public void setIdSpesa(String idSpesa){
        this.IdSpesa = idSpesa;
    }

    public void setPeriodicità(String periodicità){
        this.Periodicità = periodicità;
    }

    public void setDataScadenza(LocalDate dataScadenza){
        this.DataScadenza = dataScadenza;
    }

    public void setImporto(float importo){
        this.Importo = importo;
    }

    public void setDestinatario(String destinatario){
        this.Destinatario = destinatario;
    }

    public void setFineRinnovo(LocalDate fineRinnovo){
        this.FineRinnovo = fineRinnovo;
    }

    public void setDescrizione(String descrizione){
        this.Descrizione = descrizione;
    }

    @Override
    public String toString(){
        return "SPESA PROGRAMMATA: |IdSpesa = "+ IdSpesa + "|\t" + "|Periodicità = " + Periodicità + "|\t" + "|DataScadenza = "
                + DataScadenza + "|\t" + "|Importo = " + Importo + "|\t" + "|Destinatario = " + Destinatario + "|\t" +
                "|FineRinnovo = " + FineRinnovo + "|\t" + "|Descrizione = " + Descrizione + "|";
    }



}
