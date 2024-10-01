package smu.DAO;

import smu.DTO.Transazione;
import java.sql.SQLException;
import java.util.List;

public interface TransazioneDAO {

    boolean insert(Transazione transaction) throws SQLException;
    boolean update(Transazione transaction) throws SQLException;
    boolean delete(String transactionID) throws SQLException;
    Transazione getByID(String transactionID) throws SQLException;
    List<Transazione> getByCardNumber(String cardNumber) throws SQLException;

}
