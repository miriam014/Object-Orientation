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
        ps.setInt(1, Integer.parseInt(walletID));

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
        ps.setInt(1, Integer.parseInt(walletID));

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
    public List<Portafoglio> getPersonalByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        List<Portafoglio> personalWallets = new ArrayList<>();

        String sql = "SELECT DISTINCT P.IdPortafoglio, P.NomePortafoglio, P.Saldo, P.IdFamiglia " +
                "FROM smu.Portafoglio AS P " +
                "LEFT JOIN smu.AssociazioneCartaPortafoglio AS ACP ON P.IdPortafoglio = ACP.IdPortafoglio " +
                "LEFT JOIN smu.Carta AS C ON ACP.NumeroCarta = C.NumeroCarta " +
                "LEFT JOIN smu.ContoCorrente AS CC ON C.NumeroConto = CC.NumeroConto " +
                "LEFT JOIN smu.Utente AS U ON CC.Username = U.Username " +
                "WHERE (U.Username = ? OR P.IdFamiglia IS NULL) AND P.IdFamiglia IS NULL;"; // Filtra per IdFamiglia NULL

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Portafoglio wallet = new Portafoglio(rs.getString("IdPortafoglio"),
                    rs.getString("NomePortafoglio"),
                    rs.getFloat("Saldo"),
                    rs.getString("IdFamiglia"));
            personalWallets.add(wallet);
        }
        rs.close();
        ps.close();
        System.out.println("Numero totale di portafogli personali trovati: " + personalWallets.size());
        return personalWallets;
    }

    @Override
    public List<Portafoglio> getFamiliarByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        List<Portafoglio> familiarWallets = new ArrayList<>();

        String sql =   "SELECT DISTINCT P.IdPortafoglio, P.NomePortafoglio, P.Saldo, P.IdFamiglia " +
                "FROM smu.Portafoglio AS P " +
                "JOIN smu.Famiglia AS F ON P.IdFamiglia = F.IdFamiglia " +
                "JOIN smu.UtentiInFamiglie AS UF ON F.IdFamiglia = UF.IdFamiglia " +
                "WHERE UF.NomeUtente = ? AND P.IdFamiglia IS NOT NULL;";  // Filtra per IdFamiglia non NULL

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Portafoglio wallet = new Portafoglio(rs.getString("IdPortafoglio"),
                    rs.getString("NomePortafoglio"),
                    rs.getFloat("Saldo"),
                    rs.getString("IdFamiglia"));
            familiarWallets.add(wallet);
        }
        rs.close();
        ps.close();
        System.out.println("Numero totale di portafogli familiari trovati: " + familiarWallets.size());
        for (Portafoglio wallet : familiarWallets) {
            System.out.println("Portafoglio familiare trovato -> ID: " + wallet.getIdPortafoglio() + ", Nome: " + wallet.getNomePortafoglio());
        }
        return familiarWallets;
    }

    @Override
    public String getCardNumberByWalletID(String walletID) throws SQLException{
        Connection connection = Database.getConnection();
        String cardNumber = null;

        String sql = "SELECT NumeroCarta FROM smu.AssociazioneCartaPortafoglio WHERE IdPortafoglio = ?;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(walletID));

        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            cardNumber = rs.getString("NumeroCarta");
        }
        rs.close();
        ps.close();
        return cardNumber;
    }
}
