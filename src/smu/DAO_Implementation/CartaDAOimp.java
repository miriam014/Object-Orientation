package smu.DAO_Implementation;

import smu.DAO.CartaDAO;
import smu.DAO.TransazioneDAO;
import smu.DTO.Carta;
import smu.DTO.Transazione;
import smu.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartaDAOimp implements CartaDAO {

    @Override
    public boolean insert(Carta card) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Carta(NumeroCarta, DataScadenza, CVV, Saldo, IDUtente) VALUES(?,?,?,?,?)";

        return false;
    }

    @Override
    public boolean update(Carta card) throws SQLException {
        Connection connection = Database.getConnection();
        
        return false;
    }

    @Override
    public boolean delete(String cardNumber) throws SQLException {
        Connection connection = Database.getConnection();
        return false;
    }
}
