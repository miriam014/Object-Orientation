/*
package smu.DAO_Implementation;

import smu.DAO.CartaDAO;
import smu.DTO.Carta;
import smu.Database;

import java.sql.*;

public class CartaDAOimp implements CartaDAO {

    @Override
    public boolean insert(Carta card) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Carta(NumeroCarta, DataScadenza, CVV, Saldo, IDUtente) VALUES(?,?,?,?,?)";

       PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, card.getNumeroCarta());
        //ps.setDate(2, card.getDataScadenza());
        ps.setString(3, card.getCVV());
        ps.setFloat(4, card.getSaldo());
        // ps.setString(5, card.getIDUtente());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;

    }

    @Override
    public boolean update(Carta card) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Carta SET DataScadenza = ?, CVV = ?, Saldo = ?, IDUtente = ? WHERE NumeroCarta = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //ps.setDate(1, card.getDataScadenza());
        ps.setString(2, card.getCVV());
        ps.setFloat(3, card.getSaldo());
        //ps.setString(4, card.getIDUtente());
        ps.setString(5, card.getNumeroCarta());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;

    }

    @Override
    public boolean delete(String cardNumber) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Carta WHERE NumeroCarta = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, cardNumber);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public Carta getByNumeroCarta(String cardNumber) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "SELECT * FROM smu.Carta WHERE NumeroCarta = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, cardNumber);
        ResultSet rs = ps.executeQuery();
        Carta card = null;
        if (rs.next()) {
            card = new Carta();
            card.setNumeroCarta(rs.getString("NumeroCarta"));
            //card.setDataScadenza(rs.getDate("DataScadenza"));
            card.setCVV(rs.getString("CVV"));
            card.setSaldo(rs.getFloat("Saldo"));
            //card.setIDUtente(rs.getString("IDUtente"));
        }
        rs.close();
        ps.close();
        return card;
    }
}

*/