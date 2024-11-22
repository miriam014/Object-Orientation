package smu.DTO;

public class Famiglia {
    private String IdFamiglia;
    private String NomeFamiglia;

    public Famiglia(String idFamiglia, String nomeFamiglia){
        this.IdFamiglia = idFamiglia;
        this.NomeFamiglia = nomeFamiglia;
    }

    public String getIdFamiglia(){
        return IdFamiglia;
    }

    public String getNomeFamiglia(){
        return NomeFamiglia;
    }

    public void setIdFamiglia(String idFamiglia){
        this.IdFamiglia = idFamiglia;
    }

    public void setNomeFamiglia(String nomeFamiglia){
        this.NomeFamiglia = nomeFamiglia;
    }

    @Override
    public String toString(){
        return "FAMIGLIA: |IdFamiglia = "+ IdFamiglia + "|\t" + "|NomeFamiglia = " + NomeFamiglia +"|";
    }
}
