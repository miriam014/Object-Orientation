package smu.DAO;

import smu.DTO.Famiglia;
import java.sql.SQLException;
import java.util.List;

public interface FamigliaDAO {

    boolean insert(Famiglia family) throws SQLException;
    boolean update(Famiglia family) throws SQLException;
    boolean delete(String familyID) throws SQLException;
    Famiglia getByID(String familyID) throws SQLException;
    List<Famiglia> getByUsername(String username) throws SQLException;
}
