package smu.DAO;

import java.sql.SQLException;
import java.util.List;

public interface UtentiInFamiglieDAO {
    List<String> getUsersByFamilyId (String familyID) throws SQLException;
}
