package smu.DAO_Implementation;

import java.sql.*;
import java.time.*;

import smu.DAO.UtenteDAO;
import smu.DTO.Utente;
import smu.Database;


public class UtenteDAO_Implementation  implements UtenteDAO {

    @Override
    public Utente getByUsername(String username) throws SQLException {
        return null;
    }

    @Override
    public Utente checkLogin(String username, String password) throws SQLException {
        return null;
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
        return false;
    }

    @Override
    public boolean delete(Utente user) throws SQLException {
        return false;
    }
}
