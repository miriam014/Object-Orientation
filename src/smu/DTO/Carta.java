package smu.DTO;
import java.sql.Date;
import java.time.LocalDate;

public class Carta {
    private String NumeroCarta;
    private String NomeCarta;
    private LocalDate Scadenza;
    private float Saldo;
    private String TipoCarta;
    private float Plafond;
    private String CVV;

    public Carta(String numeroCarta, String nomeCarta, LocalDate scadenza, float saldo, String tipoCarta, float plafond, String cvv){
        this.NumeroCarta = numeroCarta;
        this.NomeCarta = nomeCarta;
        this.Scadenza = scadenza;
        this.TipoCarta = tipoCarta;
        this.Saldo = saldo;
        this.Plafond = plafond;
        this.CVV = cvv;
    }

    public String getNumeroCarta(){
        return NumeroCarta;
    }

    public String getNomeCarta(){
        return NomeCarta;
    }

    public LocalDate getScadenza(){
        return Scadenza;
    }

    public float getSaldo(){
        return Saldo;
    }

    public String getTipoCarta(){
        return TipoCarta;
    }

    public float getPlafond(){
        return Plafond;
    }

    public String getCVV(){
        return CVV;
    }

    public void setNumeroCarta(String numeroCarta){
        this.NumeroCarta = numeroCarta;
    }

    public void setNomeCarta(String nomeCarta){
        this.NomeCarta = nomeCarta;
    }

    public void setScadenza(LocalDate scadenza){
        this.Scadenza = scadenza;
    }

    public void setSaldo(float saldo){
        this.Saldo = saldo;
    }

    public void setTipoCarta(String tipoCarta){
        this.TipoCarta = tipoCarta;
    }

    public void setPlafond(float plafond){
        this.Plafond = plafond;
    }

    public void setCVV(String cvv){
        this.CVV = cvv;
    }

    @Override
    public String toString(){
        return "CARTA: |NumeroCarta = "+ NumeroCarta + "|\t" + "|NomeCarta = " + NomeCarta + "|\t" + "|Scadenza = " + Scadenza +"|\t" + "|Saldo = "+
                Saldo + "|\t" + "|TipoCarta = " + TipoCarta + "|\t" + "|Plafond = " + Plafond + "|\t" + "|CVV = " + CVV + "|\t";
    }

}
