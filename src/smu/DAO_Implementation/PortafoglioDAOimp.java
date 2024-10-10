package smu.DAO_Implementation;

import smu.DAO.PortafoglioDAO;
import smu.DTO.Portafoglio;
import smu.DTO.Transazione;
import smu.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortafoglioDAOimp implements PortafoglioDAO {

    @Override
    public boolean insert(Portafoglio wallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES(?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, wallet.getNomePortafoglio());
        ps.setString(2, wallet.getIdFamiglia());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(Portafoglio wallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Portafoglio SET NomePortafoglio = ?, IdFamiglia = ?, Saldo = ? WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, wallet.getNomePortafoglio());
        ps.setString(2, wallet.getIdFamiglia());
        ps.setFloat(3, wallet.getSaldo());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(String walletID) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Portafoglio WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, walletID);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public Portafoglio getByID(String walletID) throws SQLException {
        Connection connection = Database.getConnection();
        Portafoglio wallet = null;

        String sql = "SELECT * FROM smu.Portafoglio WHERE IdPortafoglio = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, walletID);

        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            wallet = new Portafoglio(rs.getString("IdPortafoglio"),
                    rs.getString("NomePortafoglio"),
                    rs.getFloat("Saldo"),
                    rs.getString("IdFamiglia"));
        }
        rs.close();
        ps.close();
        return wallet;
    }

    @Override
    public List<Portafoglio> getByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        List<Portafoglio> list = new ArrayList<>();

        String sql = "SELECT * FROM smu.Portafoglio NATURAL JOIN (smu.Famiglia NATURAL JOIN smu.Utente) WHERE Username = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Portafoglio wallet = new Portafoglio(rs.getString("IdPortafoglio"),
                    rs.getString("NomePortafoglio"),
                    rs.getFloat("Saldo"),
                    rs.getString("IdFamiglia"));
            list.add(wallet);
        }
        rs.close();
        ps.close();
        return list;
    }
}
