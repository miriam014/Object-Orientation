package smu.Control;

import smu.DAO.UtenteDAO;
import smu.DAO_Implementation.UtenteDAOimp;
import smu.DTO.Utente;

import java.sql.SQLException;

public class LoginControl {

    private UtenteDAO userDao;

    public LoginControl() {
        userDao = new UtenteDAOimp(); // Inizializza il DAO
    }

    public Utente authenticateUser(String username, String password) throws SQLException {
        UtenteDAO userDAO = new UtenteDAOimp();
        Utente utente = userDAO.checkCredentials(username, password);
        return utente;
    }
}
