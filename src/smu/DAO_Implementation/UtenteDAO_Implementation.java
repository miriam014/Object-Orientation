package smu.DAO_Implementation;

import java.sql.*;
import java.time.*;

import smu.DAO.UtenteDAO;
import smu.DTO.Utente;
import smu.Database;


public class UtenteDAO_Implementation  implements UtenteDAO {

    @Override
    public Utente getByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        Utente user = null;

        String sql = "SELECT * FROM smu.Utente WHERE Username = ?";
        PreparedStatement p = connection.prepareStatement(sql);

        p.setString(1, username);

        ResultSet rs =p.executeQuery();

        if(rs.next()){
            String username2 = rs.getString("Username");
            String nome = rs.getString("Nome");
            String cognome = rs.getString("Cognome");
            String telefono = rs.getString("Telefono");
            String email = rs.getString("Email");
            String password = rs.getString("Password");
            Integer id_famiglia = rs.getInt("IdFamiglia");
            user = new Utente(username2, nome, cognome, telefono, email, password,id_famiglia);
        }
        rs.close();
        p.close();
        return user;
    }

    @Override
    public boolean insert(Utente user) throws SQLException{
        Connection connection = Database.getConnection();

        String sql = "INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password, IdFamiglia) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement p = connection.prepareStatement(sql);

        p.setString(1, user.getUsername());
        p.setString(2, user.getNome());
        p.setString(3, user.getCognome());
        p.setString(4, user.getTelefono());
        p.setString(5, user.getEmail());
        p.setString(6, user.getPassword());
        p.setInt(7, user.getIdFamiglia());

        int result = p.executeUpdate();
        p.close();

        return result != 0;
    }

    @Override
    public boolean update(Utente user) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE smu.Utente SET Username = ?,Nome = ?, Cognome = ?, Telefono = ?, Email = ?, Password = ?, IdFamiglia = ?";
        PreparedStatement p = connection.prepareStatement(sql);

        p.setString(1, user.getUsername());
        p.setString(2, user.getNome());
        p.setString(3, user.getCognome());
        p.setString(4, user.getTelefono());
        p.setString(5, user.getEmail());
        p.setString(6, user.getPassword());
        p.setInt(7, user.getIdFamiglia());

        int result = p.executeUpdate();
        p.close();

        return result != 0;
    }

    @Override
    public boolean delete(String username) throws SQLException {

        Connection connection = Database.getConnection();

        String sql ="DELETE FROM smu.Utente WHERE Username = ? CASCADE";
        PreparedStatement p = connection.prepareStatement(sql);

        p.setString(1, username);
        int result = p.executeUpdate();
        p.close();

        return result != 0;
    }
}
