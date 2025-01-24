package smu.DTO;

public class UtentiInPortafogli {
    private String NomeUtente;
    private String IdPortafoglio;


    public UtentiInPortafogli(String nomeUtente, String idPortafoglio){
        this.NomeUtente = nomeUtente;
        this.IdPortafoglio = idPortafoglio;
    }

    public String getNomeUtente() {
        return NomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.NomeUtente = nomeUtente;
    }

    public String getIdPortafoglio() {
        return IdPortafoglio;
    }

    public void setIdPortafoglio(String idPortafoglio) {
        this.IdPortafoglio = idPortafoglio;
    }

    @Override
    public String toString() {
        return "UtentiInPortafogli : " + "| NomeUtente='" + NomeUtente + '\'' + ",| IdPortafoglio='" + IdPortafoglio + '\'' + "|";
    }

}
