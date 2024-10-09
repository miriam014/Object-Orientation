package smu.DAO_Implementation;

import smu.DAO.ContoCorrenteDAO;
import smu.DTO.ContoCorrente;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContoCorrenteDAOimp implements ContoCorrenteDAO {

    public boolean insert(ContoCorrente account) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.ContoCorrente(NumeroConto, IBAN, Saldo, NomeBanca, BIC, Username)VALUES (?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, account.getNumeroConto());
        ps.setString(2, account.getIBAN());
        ps.setFloat(3, account.getSaldo());
        ps.setString(4, account.getNomeBanca());
        ps.setString(5, account.getBIC());
        ps.setString(6, account.getUsername());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean update(ContoCorrente account) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.ContoCorrente SET IBAN = ?, Saldo = ?, NomeBanca = ?, BIC = ?, Username = ? WHERE NumeroConto = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, account.getIBAN());
        ps.setFloat(2, account.getSaldo());
        ps.setString(3, account.getNomeBanca());
        ps.setString(4, account.getBIC());
        ps.setString(5, account.getUsername());
        ps.setString(6, account.getNumeroConto());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean delete(String accountNumber) throws SQLException{
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.ContoCorrente WHERE NumeroConto = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, accountNumber);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public ContoCorrente getByAccountNumber(String accountNumber) throws SQLException{
        Connection connection = Database.getConnection();
        ContoCorrente account = null;

        String sql = "SELECT * FROM smu.ContoCorrente WHERE NumeroConto = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, accountNumber);

        ResultSet rs = ps.executeQuery();
        if(rs.next())
            account = new ContoCorrente(rs.getString("NumeroConto"),
                    rs.getString("IBAN"),
                    rs.getFloat("Saldo"),
                    rs.getString("NomeBanca"),
                    rs.getString("BIC"),
                    rs.getString("Username"));
        ps.close();
        rs.close();
        return account;
    }

    public List<ContoCorrente> getByUsername(String username) throws SQLException{
        Connection connection = Database.getConnection();
        List<ContoCorrente> list = new ArrayList<>();

        String sql = "SELECT * FROM smu.ContoCorrente WHERE Username = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            ContoCorrente account = new ContoCorrente(rs.getString("NumeroConto"),
                    rs.getString("IBAN"),
                    rs.getFloat("Saldo"),
                    rs.getString("NomeBanca"),
                    rs.getString("BIC"),
                    rs.getString("Username"));
            list.add(account);
        }
        ps.close();
        rs.close();
        return list;
    }

    public List<ContoCorrente> getByFamilyID(String familyID) throws SQLException{
        Connection connection = Database.getConnection();
        List<ContoCorrente> list = new ArrayList<>();

        String sql = "SELECT * FROM smu.ContoCorrente NATURAL JOIN smu.Utente WHERE IdFamiglia = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, familyID);

        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            ContoCorrente account = new ContoCorrente(rs.getString("NumeroConto"),
                    rs.getString("IBAN"),
                    rs.getFloat("Saldo"),
                    rs.getString("NomeBanca"),
                    rs.getString("BIC"),
                    rs.getString("Username"));
            list.add(account);
        }
        ps.close();
        rs.close();
        return list;
    }

}
