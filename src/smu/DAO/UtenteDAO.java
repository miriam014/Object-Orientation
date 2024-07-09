package smu.DAO;

import smu.DTO.Utente;
import java.sql.SQLException;

public interface UtenteDAO {

    Utente getByUsername(String username) throws SQLException;
    Utente checkLogin(String username, String password) throws SQLException;
     boolean insert(Utente user) throws SQLException;
     boolean update(Utente user) throws SQLException;
     boolean delete(Utente user) throws SQLException;



}
