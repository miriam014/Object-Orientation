package smu.DAO_Implementation;

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
    public  List<String> getUsersByFamilyId(String familyID) throws SQLException {
        Connection connection = Database.getConnection();
        List<String> utenti = new ArrayList<>();

        //Query per ottenre i nomi degli utenti dalla tabella UtentiInFamiglia
        String sql = "SELECT u.Username FROM smu.UtentiInFamiglie uf JOIN smu.Utente u ON uf.NomeUtente = u.Username WHERE uf.IdFamiglia = CAST(? AS INTEGER)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, familyID);
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {
            utenti.add(rs.getString("Username"));
        }

        return utenti;
    }
}
