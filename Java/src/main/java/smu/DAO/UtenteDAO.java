package smu.DAO;

import smu.DTO.Utente;
import java.sql.SQLException;

public interface UtenteDAO {

    boolean insert(Utente user) throws SQLException;
    boolean update(Utente user) throws SQLException;
    boolean delete(String username) throws SQLException;
    Utente getByUsername(String username) throws SQLException;
    Utente getByEmail(String email) throws SQLException;
    Utente checkCredentials(String username, String password) throws SQLException;
    Utente getByNumberCount(String numeroConto) throws SQLException;
}
