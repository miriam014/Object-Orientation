package smu.DTO;

public class ContoCorrente {
    private String NumeroConto;
    private String IBAN;
    private float Saldo;
    private String NomeBanca;
    private String BIC;
    private String Username;


    public ContoCorrente(String numeroConto, String iban, float saldo, String nomeBanca, String bic, String username){
        this.NumeroConto = numeroConto;
        this.IBAN = iban;
        this.Saldo = saldo;
        this.NomeBanca = nomeBanca;
        this.BIC = bic;
        this.Username = username;
    }

    public String getNumeroConto(){
        return NumeroConto;
    }

    public String getIBAN(){
        return IBAN;
    }

    public float getSaldo(){
        return Saldo;
    }

    public String getNomeBanca(){
        return NomeBanca;
    }

    public String getBIC(){
        return BIC;
    }

    public String getUsername(){
        return Username;
    }

    public void setNumeroConto(String numerConto){
        this.NumeroConto = numerConto;
    }

    public void setIBAN(String iban){
        this.IBAN = iban;
    }

    public void setSaldo(float saldo){
        this.Saldo = saldo;
    }

    public void setNomeBanca(String nomeBanca){
        this.NomeBanca = nomeBanca;
    }

    public void setBIC(String bic){
        this.BIC = bic;
    }

    public void setUsername(String username){
        this.Username = username;
    }

    @Override
    public String toString(){
        return "CONTO CORRENTE: |NumeroConto = "+ NumeroConto + "|\t" + "|IBAN = " + IBAN + "|\t" + "|Saldo = " + Saldo +"|\t" + "|NomeBanca = "+
                NomeBanca+"\t"+ "|BIC = " + BIC+"| |Username = " + Username + "|";
    }


}
