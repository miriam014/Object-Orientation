package smu.DAOImplementation;

import smu.DAO.UtentiInFamiglieDAO;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtentiInFamiglieDAOimp implements UtentiInFamiglieDAO {
    @Override
    public  List<String> getUsersByFamilyId(Integer familyID) throws SQLException {
        Connection connection = Database.getConnection();
        List<String> utenti = new ArrayList<>();

        //Query per ottenre i nomi degli utenti dalla tabella UtentiInFamiglia
        String sql = "SELECT u.Username FROM smu.UtentiInFamiglie uf JOIN smu.Utente u ON uf.NomeUtente = u.Username WHERE uf.IdFamiglia = CAST(? AS INTEGER)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, familyID);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            utenti.add(rs.getString("Username"));
        }

        ps.close();
        return utenti;
    }

    @Override
    public void removeUserFromFamily(String username, Integer familyID) throws SQLException {
        Connection connection = Database.getConnection();

        //Query per rimuovere l'utente dalla famiglia
        String sql = "DELETE FROM smu.UtentiInFamiglie WHERE NomeUtente = ? AND IdFamiglia = CAST(? AS INTEGER)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, familyID);
        ps.executeUpdate();
    }

    @Override
    public void addUserToFamily(Integer familyID, String username) throws SQLException {
        Connection connection = Database.getConnection();

        //Query per aggiungere l'utente alla famiglia
        String sql = "INSERT INTO smu.UtentiInFamiglie (NomeUtente, IdFamiglia) VALUES (?, CAST(? AS INTEGER))";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setInt(2, familyID);

        int rowsAffected = ps.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Utente " + username + " aggiunto alla famiglia " + familyID);
        } else {
            System.out.println("Errore nell'aggiungere l'utente " + username + " alla famiglia " + familyID);
        }

        ps.close();
    }
}
