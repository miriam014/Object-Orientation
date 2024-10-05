package smu.DAO_Implementation;

import java.sql.*;

import smu.DAO.UtenteDAO;
import smu.DTO.Utente;
import smu.Database;


public class UtenteDAOimp implements UtenteDAO {

    @Override
    public Utente getByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        Utente user = null;

        String sql = "SELECT * FROM smu.Utente WHERE Username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, username);

        ResultSet rs =ps.executeQuery();

        if(rs.next()){
            String username2 = rs.getString("Username");
            String nome = rs.getString("Nome");
            String cognome = rs.getString("Cognome");
            String telefono = rs.getString("Telefono");
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            String id_famiglia = rs.getString("IdFamiglia");
            user = new Utente(username2, nome, cognome, telefono, email, password,id_famiglia);
        }
        rs.close();
        ps.close();
        return user;
    }

    @Override
    public Utente getByEmail(String email) throws SQLException {
        Connection connection = Database.getConnection();
        Utente user = null;

        String sql = "SELECT * FROM smu.Utente WHERE Email = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, email);

        ResultSet rs =ps.executeQuery();

        if(rs.next()){
            String username = rs.getString("Username");
            String nome = rs.getString("Nome");
            String cognome = rs.getString("Cognome");
            String telefono = rs.getString("Telefono");
            String email2 = rs.getString("Email");
            String password = rs.getString("Password");
            String id_famiglia = rs.getString("IdFamiglia");
            user = new Utente(username, nome, cognome, telefono, email2, password,id_famiglia);
        }
        rs.close();
        ps.close();
        return user;
    }

    public Utente checkCredentials(String username, String password) throws SQLException{
        Connection connection = Database.getConnection();
        Utente user = null;

        String sql = "SELECT * FROM smu.Utente WHERE username = ? AND Password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs =ps.executeQuery();

        if(rs.next()){
            String username2 = rs.getString("Username");
            String nome = rs.getString("Nome");
            String cognome = rs.getString("Cognome");
            String telefono = rs.getString("Telefono");
            String email2 = rs.getString("Email");
            String password2 = rs.getString("Password");
            String id_famiglia = rs.getString("IdFamiglia");
            user = new Utente(username2, nome, cognome, telefono, email2, password2,id_famiglia);
        }
        rs.close();
        ps.close();
        return user;
    }


    @Override
    public boolean insert(Utente user) throws SQLException{
        Connection connection = Database.getConnection();

        String sql = "INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password, IdFamiglia) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getNome());
        ps.setString(3, user.getCognome());
        ps.setString(4, user.getTelefono());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getPassword());
        ps.setString(7, user.getIdFamiglia());

        int result = ps.executeUpdate();
        ps.close();

        return result != 0;
    }

    @Override
    public boolean update(Utente user) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE smu.Utente SET Nome = ?, Cognome = ?, Telefono = ?, Email = ?, Password = ?, IdFamiglia = ? WHERE Username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getNome());
        ps.setString(2, user.getCognome());
        ps.setString(3, user.getTelefono());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getPassword());
        ps.setString(6, user.getIdFamiglia());
        ps.setString(7, user.getUsername());

        int result = ps.executeUpdate();
        ps.close();

        return result != 0;
    }

    @Override
    public boolean delete(String username) throws SQLException {

        Connection connection = Database.getConnection();

        String sql ="DELETE FROM smu.Utente WHERE Username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, username);
        int result = ps.executeUpdate();
        ps.close();

        return result != 0;
    }
}
