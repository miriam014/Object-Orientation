package smu.DAO;

import smu.DTO.Carta;
import java.sql.SQLException;
import java.util.List;

public interface CartaDAO {

    boolean insert(Carta card) throws SQLException;
    boolean update(Carta card) throws SQLException;
    boolean delete(String cardNumber) throws SQLException;

    Carta getByNumeroCarta(String cardNumber) throws SQLException;

    List<Carta> getCardsByUsername(String username) throws SQLException;
}
