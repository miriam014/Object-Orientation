package smu.DAO;

import smu.DTO.ContoCorrente;
import java.sql.SQLException;
import java.util.List;

public interface ContoCorrenteDAO {

    boolean insert(ContoCorrente account) throws SQLException;
    boolean update(ContoCorrente account) throws SQLException;
    boolean delete(String accountNumber) throws SQLException;
    ContoCorrente getByAccountNumber(String accountNumber) throws SQLException;
    List<ContoCorrente> getByUsername(String username) throws SQLException;
    List<ContoCorrente> getByFamilyID(String familyID) throws SQLException;
}
