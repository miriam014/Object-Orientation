package DAO;

import DTO.Utente;
import java.sql.SQLException;

public interface UtenteDAO {

    public boolean insert(Utente user) throws SQLException;
    public boolean update(Utente user) throws SQLException;
    public boolean delete(Utente user) throws SQLException;

}
