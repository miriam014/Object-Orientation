package smu.DTO;
import java.sql.Date;

public class Carta {
    private String NumeroCarta;
    private String NomeCarta;
    private Date Scadenza;
    private float Saldo;
    private String TipoCarta;
    private float Plafond;
    private String CVV;
    private String NumeroConto;

    public Carta(String numeroCarta, String nomeCarta, Date scadenza, float saldo, String tipoCarta, float plafond, String cvv, String numeroConto){
        this.NumeroCarta = numeroCarta;
        this.NomeCarta = nomeCarta;
        this.Scadenza = scadenza;
        this.TipoCarta = tipoCarta;
        this.Saldo = saldo;
        this.Plafond = plafond;
        this.CVV = cvv;
        this.NumeroConto = numeroConto;
    }

    public String getNumeroCarta(){
        return NumeroCarta;
    }

    public String getNomeCarta(){
        return NomeCarta;
    }

    public Date getScadenza(){
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

    public String getNumeroConto(){
        return NumeroConto;
    }

    public void setNumeroCarta(String numeroCarta){
        this.NumeroCarta = numeroCarta;
    }

    public void setNomeCarta(String nomeCarta){
        this.NomeCarta = nomeCarta;
    }

    public void setScadenza(Date scadenza){
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

    public void setNumeroConto(String numeroConto){
        this.NumeroConto = numeroConto;
    }

    @Override
    public String toString(){
        return "CARTA: |NumeroCarta = "+ NumeroCarta + "|\t" + "|NomeCarta = " + NomeCarta + "|\t" + "|Scadenza = " + Scadenza +"|\t" + "|Saldo = "+
                Saldo + "|\t" + "|TipoCarta = " + TipoCarta + "|\t" + "|Plafond = " + Plafond + "|\t" + "|CVV = " + CVV + "|\t";
    }

}
