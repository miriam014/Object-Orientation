package smu.DAO_Implementation;

import smu.DAO.FamigliaDAO;
import smu.DTO.Famiglia;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FamigliaDAOimp implements FamigliaDAO {

    @Override
    public boolean insert(Famiglia family) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Famiglia(NomeFamiglia) VALUES (?)";

        PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        ps.setString(1, family.getNomeFamiglia());

        int result = ps.executeUpdate();
        if (result != 0) {
            // Ottieni l'ID generato
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Imposta l'ID generato sull'oggetto family
                    family.setIdFamiglia(generatedKeys.getInt(1));
                }
            }
        }

        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(Famiglia family) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Famiglia SET NomeFamiglia = ? WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, family.getNomeFamiglia());
        ps.setInt(2, family.getIdFamiglia());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(Integer familyID) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Famiglia WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, familyID);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public Famiglia getByID(Integer familyID) throws SQLException{
        Connection connection = Database.getConnection();
        Famiglia family = null;

        String sql = "SELECT * FROM smu.Famiglia WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, familyID);

        ResultSet rs =ps.executeQuery();
        if(rs.next())
            family = new Famiglia(rs.getInt("IdFamiglia"),rs.getString("NomeFamiglia"));

        ps.close();
        rs.close();

        return family;
    }

    @Override
    public List<Famiglia> getByUsername(String username) throws SQLException{
        Connection connection = Database.getConnection();
        List<Famiglia> families = new ArrayList<>();

        String sql = "SELECT * FROM smu.Famiglia AS F NATURAL JOIN smu.UtentiInFamiglie AS UF WHERE UF.NomeUtente = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next())
            families.add(new Famiglia(rs.getInt("IdFamiglia"),rs.getString("NomeFamiglia")));

        ps.close();
        rs.close();

        return families;
    }

}
