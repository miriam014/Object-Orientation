package smu.DTO;

public class Utente {

    // Attributi
    private String Username;
    private String Nome;
    private String Cognome;
    private String Telefono;
    private String Email;
    private String Password;

    // Costruttore
    public Utente(String username, String nome, String cognome, String telefono, String email, String password){
        this.Username = username;
        this.Nome = nome;
        this.Cognome = cognome;
        this.Telefono = telefono;
        this.Email = email;
        this.Password = password;
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


    @Override
    public String toString(){
        return "UTENTE: |Username = "+ Username + "|\t" + "|Nome = " + Nome + "|\t" + "|Cognome = " + Cognome +"|\t" + "|Telefono = "+
        Telefono+ "|\t" + "|Email = "+ Email+ "|\t" + "|Password = " + Password + "|";
    }

}
