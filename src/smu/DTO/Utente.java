package smu.DTO;

public class Utente {

    // Attributi
    private String Username;
    private String Nome;
    private String Cognome;
    private String Telefono;
    private String Email;
    private String Password;
    private String IdFamiglia;

    // Costruttore
    public Utente(String username, String nome, String cognome, String telefono, String email, String password, String idFamiglia){
        this.Username = username;
        this.Nome = nome;
        this.Cognome = cognome;
        this.Telefono = telefono;
        this.Email = email;
        this.Password = password;
        this.IdFamiglia = idFamiglia;
    }
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        this.Nome = nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        this.Cognome = cognome;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        this.Telefono = telefono;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getIdFamiglia() {
        return IdFamiglia;
    }

    public void setIdFamiglia(String idFamiglia) {
        this.IdFamiglia = idFamiglia;
    }

    @Override
    public String toString(){
        return "UTENTE: |Username = "+ Username + "|\t" + "|Nome = " + Nome + "|\t" + "|Cognome = " + Cognome +"|\t" + "|Telefono = "+
        Telefono+ "|\t" + "|Email = "+ Email+ "|\t" + "|Password = " + Password + "|\t" + "|IdFamiglia = " + IdFamiglia+"|";
    }

}
