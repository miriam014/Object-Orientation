package smu.DTO;

public class Categoria {
    private String NomeCategoria;
    private String ParoleChiavi;

    public Categoria(String nomeCategoria, String paroleChiavi) {
        this.NomeCategoria = nomeCategoria;
        this.ParoleChiavi = paroleChiavi;
    }

    public String getNomeCategoria() {
        return NomeCategoria;
    }

    public String getParoleChiavi() {
        return ParoleChiavi;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.NomeCategoria = nomeCategoria;
    }

    public void setParoleChiavi(String paroleChiavi) {
        this.ParoleChiavi = paroleChiavi;
    }

    @Override
    public String toString() {
        return "CATEGORIA: |NomeCategoria = "+ NomeCategoria + "|\t" + "|ParolaChiave = " + ParoleChiavi +"|";
    }

}
