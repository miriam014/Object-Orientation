package smu.DTO;

public class Portafoglio {
    private String IdPortafoglio;
    private String NomePortafoglio;
    private float Saldo;
    private String IdFamiglia;

    public Portafoglio(String idPortafoglio, String nomePortafoglio, float saldo, String idFamiglia){
        this.IdPortafoglio = idPortafoglio;
        this.NomePortafoglio = nomePortafoglio;
        this.Saldo = saldo;
        this.IdFamiglia = idFamiglia;
    }

    public Portafoglio(String IdPortafoglio, String nomePortafoglio, String idFamiglia){
        this.IdPortafoglio = IdPortafoglio;
        this.NomePortafoglio = nomePortafoglio;
        this.IdFamiglia = idFamiglia;
    }

    public Portafoglio(String nomePortafoglio, String idFamiglia){
        this.NomePortafoglio = nomePortafoglio;
        this.IdFamiglia = idFamiglia;
    }

    public String getIdPortafoglio(){
        return IdPortafoglio;
    }

    public String getNomePortafoglio(){
        return NomePortafoglio;
    }

    public float getSaldo(){
        return Saldo;
    }

    public String getIdFamiglia(){
        return IdFamiglia;
    }

    public void setIdPortafoglio(String idPortafoglio){
        this.IdPortafoglio = idPortafoglio;
    }

    public void setNomePortafoglio(String nomePortafoglio){
        this.NomePortafoglio = nomePortafoglio;
    }

    public void setSaldo(float saldo){
        this.Saldo = saldo;
    }

    public void setIdFamiglia(String idFamiglia){
        this.IdFamiglia = idFamiglia;
    }

    @Override
    public String toString(){
        return "PORTAFOGLIO: IdPortafoglio = "+ IdPortafoglio + "\t" + "NomePortafoglio = " + NomePortafoglio + "\t" + "Saldo = " + Saldo + "\t" + "IdFamiglia = " + IdFamiglia;
    }

}
