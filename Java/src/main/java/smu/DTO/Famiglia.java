package smu.DTO;

public class Famiglia {
    private Integer IdFamiglia;
    private String NomeFamiglia;

    public Famiglia(Integer idFamiglia, String nomeFamiglia){
        this.IdFamiglia = idFamiglia;
        this.NomeFamiglia = nomeFamiglia;
    }

    public Integer getIdFamiglia(){
        return IdFamiglia;
    }

    public String getNomeFamiglia(){
        return NomeFamiglia;
    }

    public void setIdFamiglia(Integer idFamiglia){
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
