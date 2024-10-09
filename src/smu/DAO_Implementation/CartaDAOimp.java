
package smu.DAO_Implementation;

import smu.DAO.CartaDAO;
import smu.DTO.Carta;
import smu.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CartaDAOimp implements CartaDAO {

    @Override
    public boolean insert(Carta card) throws SQLException {
        Connection connection = Database.getConnection(); // Ottiene la connessione al database
        String sql = "INSERT INTO smu.Carta(NumeroCarta, Nome, CVV, Scadenza, Saldo, TipoCarta, Plafond, NumeroConto) VALUES(?,?,?,?,?,?,?,?)"; // Query SQL per l'inserimento di una carta

        PreparedStatement ps = connection.prepareStatement(sql); // Prepara la query
        ps.setString(1, card.getNumeroCarta());  // Imposta i parametri della query
        ps.setString(2, card.getNomeCarta());
        ps.setString(3, card.getCVV());
        ps.setDate(4, card.getScadenza());
        ps.setFloat(5, card.getSaldo());
        ps.setString(6, card.getTipoCarta());
        ps.setFloat(7, card.getPlafond());
        ps.setString(8, card.getNumeroConto());

        int result = ps.executeUpdate();   // Esegue la query
        ps.close(); // Chiude la query
        return result != 0; // Ritorna true se la query ha avuto successo, false altrimenti
    }

    @Override
    public boolean update(Carta card) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Carta SET Nome = ?, CVV = ?, Scadenza = ?, Saldo = ?, TipoCarta = ?, Plafond = ?, NumeroConto = ? WHERE NumeroCarta = ?"; // Query SQL per l'aggiornamento di una carta

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, card.getNomeCarta()); // Imposta i parametri della query
        ps.setString(2, card.getCVV());
        ps.setDate(3, card.getScadenza());
        ps.setFloat(4, card.getSaldo());
        ps.setString(5, card.getTipoCarta());
        ps.setFloat(6, card.getPlafond());
        ps.setString(7, card.getNumeroConto());
        ps.setString(8, card.getNumeroCarta());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(String cardNumber) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Carta WHERE NumeroCarta = ?"; // Query SQL per l'eliminazione di una carta
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, cardNumber);

        int result = ps.executeUpdate(); // Esegue la query
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
            card = new Carta(
                    rs.getString("NumeroCarta"),
                    rs.getString("Nome"),
                    rs.getDate("Scadenza"),
                    rs.getFloat("Saldo"),
                    rs.getString("TipoCarta"),
                    rs.getFloat("Plafond"),
                    rs.getString("CVV"),
                    rs.getString("NumeroConto")
            );
        }
        return card; // Ritorna la carta trovata o null se non trovata
    }

    @Override
    public List<Carta> getCardsByUsername(String username) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "SELECT * FROM smu.CARTA AS C NATURAL JOIN smu.ContoCorrente AS CC WHERE CC.Username = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        System.out.println("Eseguendo la query con Username = " + username); // Debug

        ResultSet rs = ps.executeQuery();

        List<Carta> cards = new ArrayList<>();

        while (rs.next()) {
            Carta card = new Carta(
                    rs.getString("NumeroCarta"),
                    rs.getString("Nome"),
                    rs.getDate("Scadenza"),
                    rs.getFloat("Saldo"),
                    rs.getString("TipoCarta"),
                    rs.getFloat("Plafond"),
                    rs.getString("CVV"),
                    rs.getString("NumeroConto")
            );
            System.out.println("Carta trovata: " + card); // Debug
            cards.add(card);
        }
        rs.close();
        ps.close();
        System.out.println("Numero totale di carte trovate: " + cards.size()); // Debug
        return cards;
    }
}

