package smu.DAO_Implementation;

import java.sql.*;

import smu.DAO.UtenteDAO;
import smu.DTO.Utente;
import smu.Database;


public class UtenteDAOimp implements UtenteDAO {

    @Override
    public boolean insert(Utente user) throws SQLException{
        Connection connection = Database.getConnection();

        String sql = "INSERT INTO smu.Utente(Username, Nome, Cognome, Telefono, Email, Password) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getUsername());
        ps.setString(2, user.getNome());
        ps.setString(3, user.getCognome());
        ps.setString(4, user.getTelefono());
        ps.setString(5, user.getEmail());
        ps.setString(6, user.getPassword());

        int result = ps.executeUpdate();
        ps.close();

        return result != 0;
    }

    @Override
    public boolean update(Utente user) throws SQLException {
        Connection connection = Database.getConnection();

        String sql = "UPDATE smu.Utente SET Nome = ?, Cognome = ?, Telefono = ?, Email = ?, Password = ?  WHERE Username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, user.getNome());
        ps.setString(2, user.getCognome());
        ps.setString(3, user.getTelefono());
        ps.setString(4, user.getEmail());
        ps.setString(5, user.getPassword());
        ps.setString(6, user.getUsername());

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
            user = new Utente(username2, nome, cognome, telefono, email, password);
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
            user = new Utente(username, nome, cognome, telefono, email2, password);
        }
        rs.close();
        ps.close();
        return user;
    }

    @Override
    public Utente checkCredentials(String username, String password) throws SQLException{

        Connection connection = Database.getConnection();
        Utente user = null;

        String sql = "SELECT * FROM smu.Utente WHERE Username = ? AND Password = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs =ps.executeQuery();

        if(rs.next()){
            String username2 = rs.getString("Username");
            String nome = rs.getString("Nome");
            String cognome = rs.getString("Cognome");
            String telefono = rs.getString("Telefono");
            String email = rs.getString("Email");
            String password2 = rs.getString("Password");
            user = new Utente(username2, nome, cognome, telefono, email, password2);
        }
        rs.close();
        ps.close();
        return user;
    }

    @Override
    public Utente getByNumberCount(String numeroConto) throws SQLException {
        String query = "SELECT u.* FROM smu.UTENTE u " +
                "JOIN smu.CONTOCORRENTE c ON u.CF = c.CF " +
                "WHERE c.NumeroConto = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, numeroConto);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Utente(
                        resultSet.getString("Username"),
                        resultSet.getString("Nome"),
                        resultSet.getString("Cognome"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password")
                );
            }
        }
        return null;
    }

}
