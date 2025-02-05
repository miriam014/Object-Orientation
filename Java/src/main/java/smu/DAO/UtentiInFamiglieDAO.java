package smu.DAO;

import java.sql.SQLException;
import java.util.List;

public interface UtentiInFamiglieDAO {
    List<String> getUsersByFamilyId (Integer familyID) throws SQLException;
    void removeUserFromFamily (String username, Integer familyID) throws SQLException;
    void addUserToFamily (Integer familyID, String username) throws SQLException;
}
