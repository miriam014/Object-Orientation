package smu.DAO;

import smu.DTO.Portafoglio;
import java.sql.SQLException;
import java.util.List;

public interface PortafoglioDAO {

    boolean insert(Portafoglio wallet) throws SQLException;
    boolean update(Portafoglio wallet) throws SQLException;
    boolean delete(String walletID) throws SQLException;

    Portafoglio getByID(String walletID) throws SQLException;
    List<Portafoglio> getFamiliarByUsername(String username) throws SQLException;
    List<Portafoglio> getPersonalByUsername(String username) throws SQLException;
    String getCardNumberByWalletID(String walletID) throws SQLException;
}
