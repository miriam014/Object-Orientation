package smu.DAO;

import smu.DTO.Utente;
import java.sql.SQLException;

public interface UtenteDAO {

    Utente getByUsername(String username) throws SQLException;
    Utente getByEmail(String email) throws SQLException;
    Utente getByCF(String cf) throws SQLException;
    boolean insert(Utente user) throws SQLException;
    boolean update(Utente user) throws SQLException;
    boolean delete(String username) throws SQLException;

}
