package smu.DTO;

public class Categoria {
    private String NomeCategoria;
    private String ParolaChiave;

    public Categoria(String nomeCategoria, String parolaChiave) {
        this.NomeCategoria = nomeCategoria;
        this.ParolaChiave = parolaChiave;
    }

    public String getNomeCategoria() {
        return NomeCategoria;
    }

    public String getParolaChiave() {
        return ParolaChiave;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.NomeCategoria = nomeCategoria;
    }

    public void setParolaChiave(String parolaChiave) {
        this.ParolaChiave = parolaChiave;
    }

    @Override
    public String toString() {
        return "CATEGORIA: |NomeCategoria = "+ NomeCategoria + "|\t" + "|ParolaChiave = " + ParolaChiave +"|";
    }

}
