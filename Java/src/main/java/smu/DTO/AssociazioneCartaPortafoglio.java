package smu.DTO;

public class AssociazioneCartaPortafoglio {
    private int IdPortafoglio;
    private String NumeroCarta;

    public AssociazioneCartaPortafoglio(int IdPortafoglio, String NumeroCarta) {
        this.IdPortafoglio = IdPortafoglio;
        this.NumeroCarta = NumeroCarta;
    }

    public int getIdPortafoglio() {
        return IdPortafoglio;
    }

    public void setIdPortafoglio(int IdPortafoglio) {
        this.IdPortafoglio = IdPortafoglio;
    }

    public String getNumeroCarta() {
        return NumeroCarta;
    }

    public void setNumeroCarta(String NumeroCarta) {
        this.NumeroCarta = NumeroCarta;
    }
}
