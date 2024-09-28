package smu.DAO;

import smu.DTO.SpeseProgrammate;
import java.sql.SQLException;

public interface SpeseProgrammateDAO {

        boolean insert(SpeseProgrammate sheduledExpense) throws SQLException;
        boolean update(SpeseProgrammate sheduledExpense) throws SQLException;
        boolean delete(String expenseID) throws SQLException;
}
