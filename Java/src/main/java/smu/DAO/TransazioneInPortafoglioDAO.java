package smu.DAO;

import smu.DTO.TransazioneInPortafoglio;

import java.sql.SQLException;

public interface TransazioneInPortafoglioDAO {

    public boolean insert(TransazioneInPortafoglio transactionInWallet) throws SQLException;
    public boolean delete(TransazioneInPortafoglio transactionInWallet) throws SQLException;
    public boolean update(TransazioneInPortafoglio transactionInWallet) throws SQLException;
}
