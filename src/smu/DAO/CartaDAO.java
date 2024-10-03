package smu.DAO;

import smu.DTO.Carta;
import java.sql.SQLException;

public interface CartaDAO {

    boolean insert(Carta card) throws SQLException;
    boolean update(Carta card) throws SQLException;
    boolean delete(String cardNumber) throws SQLException;

    Carta getByNumeroCarta(String cardNumber) throws SQLException;
}
