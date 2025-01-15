package smu.DAO_Implementation;
import smu.DAO.AssociazioneCartaPortafoglioDAO;
import smu.DTO.AssociazioneCartaPortafoglio;
import smu.DTO.Carta;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssociazioneCartaPortafoglioDAOimp implements AssociazioneCartaPortafoglioDAO {

    @Override
    public boolean insert(AssociazioneCartaPortafoglio associazione) throws SQLException {

        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.AssociazioneCartaPortafoglio(IdPortafoglio, NumeroCarta) VALUES(?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, associazione.getIdPortafoglio());
        ps.setString(2, associazione.getNumeroCarta());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(AssociazioneCartaPortafoglio associazione) throws SQLException{

        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.AssociazioneCartaPortafoglio WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, associazione.getIdPortafoglio());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(AssociazioneCartaPortafoglio associazione) throws SQLException{

        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.AssociazioneCartaPortafoglio SET NumeroCarta = ? WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, associazione.getNumeroCarta());
        ps.setInt(2, associazione.getIdPortafoglio());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public String getCardNumberByID(String walletID) throws SQLException{

        Connection connection = Database.getConnection();
        String sql = "SELECT NumeroCarta FROM smu.AssociazioneCartaPortafoglio WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(walletID));

        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString("NumeroCarta");
    }

}
