package smu.DAO_Implementation;

import smu.DAO.FamigliaDAO;
import smu.DTO.Famiglia;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FamigliaDAOimp implements FamigliaDAO {
    public boolean insert(Famiglia family) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Famiglia(NomeFamiglia) VALUES (?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, family.getNomeFamiglia());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean update(Famiglia family) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Famiglia SET NomeFamiglia = ? WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, family.getNomeFamiglia());
        ps.setString(2, family.getIdFamiglia());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean delete(String familyID) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Famiglia WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, familyID);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public Famiglia getByID(String familyID) throws SQLException{
        Connection connection = Database.getConnection();
        Famiglia family = null;

        String sql = "SELECT * FROM smu.Famiglia WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, familyID);

        ResultSet rs =ps.executeQuery();
        if(rs.next())
            family = new Famiglia(rs.getString("IdFamiglia"),rs.getString("NomeFamiglia"));

        ps.close();
        rs.close();

        return family;
    }
}
