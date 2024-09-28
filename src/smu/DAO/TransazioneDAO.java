package smu.DAO;

import smu.DTO.Transazione;

import java.sql.SQLException;

public interface TransazioneDAO {

    boolean insert(Transazione transaction) throws SQLException;
    boolean update(Transazione transaction) throws SQLException;
    boolean delete(String transactionID) throws SQLException;
}
