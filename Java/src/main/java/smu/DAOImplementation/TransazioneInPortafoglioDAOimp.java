package smu.DAOImplementation;

import smu.DAO.TransazioneInPortafoglioDAO;
import smu.DTO.TransazioneInPortafoglio;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransazioneInPortafoglioDAOimp implements TransazioneInPortafoglioDAO {

    @Override
    public boolean insert(TransazioneInPortafoglio transactionInWallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql ="INSERT INTO smu.TransazioniInPortafogli(IdTransazione, IdPortafoglio) VALUES(?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(transactionInWallet.getIdTransazione()));
        ps.setInt(2, Integer.parseInt(transactionInWallet.getIdPortafoglio()));

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

    @Override
    public Integer getPortafoglioByIdTransazione(String idTransazione) throws SQLException{
        Connection connection = Database.getConnection();
        String sql ="SELECT IdPortafoglio FROM Smu.TransazioniInPortafogli WHERE IdTransazione = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(idTransazione));

        ResultSet rs = ps.executeQuery();
        Integer idPortafoglio = null;

        if(rs.next()){
            idPortafoglio = rs.getInt("IdPortafoglio");
        }

        rs.close();
        ps.close();
        return idPortafoglio;
    }

    public List<String> getTransazioniInPortafoglio(String idPortafoglio) throws SQLException {
        List<String> transazioni = new ArrayList<>();

        String sql = "SELECT IdTransazione FROM smu.TransazioniInPortafogli WHERE IdPortafoglio = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(idPortafoglio));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    transazioni.add(rs.getString("IdTransazione"));
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'id del portafoglio deve essere un numero valido.", e);
        }

        return transazioni;
    }

}
