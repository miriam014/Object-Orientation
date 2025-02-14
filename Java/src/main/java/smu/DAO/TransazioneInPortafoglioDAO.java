package smu.DAO;

import smu.DTO.TransazioneInPortafoglio;

import java.sql.SQLException;
import java.util.List;

public interface TransazioneInPortafoglioDAO {

    public boolean insert(TransazioneInPortafoglio transactionInWallet) throws SQLException;
    public boolean delete(TransazioneInPortafoglio transactionInWallet) throws SQLException;
    public boolean update(TransazioneInPortafoglio transactionInWallet) throws SQLException;

    public Integer getPortafoglioByIdTransazione(String idTransazione) throws SQLException;
    public List<String> getTransazioniInPortafoglio(String idPortafoglio) throws SQLException;
}
