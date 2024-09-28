package smu.DAO;

import smu.DTO.Famiglia;
import java.sql.SQLException;

public interface FamigliaDAO {

    boolean insert(Famiglia family) throws SQLException;
    boolean update(Famiglia family) throws SQLException;
    boolean delete(String familyID) throws SQLException;
}
