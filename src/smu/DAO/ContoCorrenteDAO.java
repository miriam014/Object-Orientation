package smu.DAO;

import smu.DTO.ContoCorrente;
import java.sql.SQLException;

public interface ContoCorrenteDAO {

    boolean insert(ContoCorrente account) throws SQLException;
    boolean update(ContoCorrente account) throws SQLException;
    boolean delete(String accountNumber) throws SQLException;

}
