package smu.DAO_Implementation;

import smu.DAO.PortafoglioDAO;
import smu.DTO.Portafoglio;
import smu.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;

public class PortafoglioDAOimp implements PortafoglioDAO {

    @Override
    public boolean insert(Portafoglio wallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Portafoglio(NomePortafoglio, IdFamiglia) VALUES(?,?) RETURNING IdPortafoglio;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, wallet.getNomePortafoglio());

        String idFamiglia = wallet.getIdFamiglia();
        if (idFamiglia == null) {
            ps.setNull(2, NULL);
        } else {
            ps.setInt(2, Integer.parseInt(idFamiglia));
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int generatedId = rs.getInt("IdPortafoglio");
            wallet.setIdPortafoglio(String.valueOf(generatedId)); // Imposta l'ID generato nell'oggetto Portafoglio
        }
        rs.close();
        ps.close();
        return wallet.getIdPortafoglio() != null;
    }

    @Override
    public boolean update(Portafoglio wallet) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Portafoglio SET NomePortafoglio = ?, IdFamiglia = ? WHERE IdPortafoglio = ?;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, wallet.getNomePortafoglio());
        String idFamiglia = wallet.getIdFamiglia();
        if (idFamiglia == null) {
            ps.setNull(2, NULL);
        } else {
            ps.setInt(2, Integer.parseInt(idFamiglia));
        }
        ps.setInt(3, Integer.parseInt(wallet.getIdPortafoglio()));
        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(String walletID) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Portafoglio WHERE IdPortafoglio = ?;";

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

        String sql = "SELECT * FROM smu.Portafoglio WHERE IdPortafoglio = ?;";

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
        List<Portafoglio> wallets = new ArrayList<>();

        String sql = "SELECT * FROM smu.Portafoglio AS P JOIN (smu.AssociazioneCartaPortafoglio ACP NATURAL JOIN smu.Carta AS C) " +
                "ON P.IdPortafoglio = ACP.IdPortafoglio JOIN (smu.ContoCorrente AS CC JOIN smu.Utente AS U ON CC.Username = U.Username) " +
                "ON CC.NumeroConto = C.NumeroConto WHERE U.Username = ?;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Portafoglio wallet = new Portafoglio(rs.getString("IdPortafoglio"),
                    rs.getString("NomePortafoglio"),
                    rs.getFloat("Saldo"),
                    rs.getString("IdFamiglia"));
            wallets.add(wallet);
        }
        rs.close();
        ps.close();
        System.out.println("Numero totale di portafogli trovati: " + wallets.size());
        return wallets;
    }
}
