package smu.DAO;

import smu.DTO.Portafoglio;
import java.sql.SQLException;

public interface PortafoglioDAO {

    boolean insert(Portafoglio wallet) throws SQLException;
    boolean update(Portafoglio wallet) throws SQLException;
    boolean delete(String walletID) throws SQLException;

}
