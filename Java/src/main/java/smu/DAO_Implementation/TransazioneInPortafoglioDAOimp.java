package smu.DAO_Implementation;

import smu.DAO.TransazioneInPortafoglioDAO;
import smu.DTO.TransazioneInPortafoglio;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransazioneInPortafoglioDAOimp implements TransazioneInPortafoglioDAO {

    @Override
    public boolean insert(TransazioneInPortafoglio transactionInWallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql ="INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, transactionInWallet.getIdTransazione());
        ps.setString(2, transactionInWallet.getIdPortafoglio());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(TransazioneInPortafoglio transactionInWallet) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.TransazionInPortafogli WHERE IDTransazione = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, transactionInWallet.getIdTransazione());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(TransazioneInPortafoglio transactionInWallet) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.TransazioniInPortafogli SET IdPortafoglio = ? WHERE IdTransazione = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, transactionInWallet.getIdPortafoglio());
        ps.setString(2, transactionInWallet.getIdTransazione());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }
}
