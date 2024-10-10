package smu.DAO;

import smu.DTO.SpeseProgrammate;
import java.sql.SQLException;
import java.util.List;

public interface SpeseProgrammateDAO {

        boolean insert(SpeseProgrammate sheduledExpense) throws SQLException;
        boolean update(SpeseProgrammate sheduledExpense) throws SQLException;
        boolean delete(String expenseID) throws SQLException;
        SpeseProgrammate getByID(String expenseID) throws SQLException;
        List<SpeseProgrammate> getByUsername(String username) throws SQLException;
        List<SpeseProgrammate> getByFamilyID(String familyID) throws SQLException;
}
