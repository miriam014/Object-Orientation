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

    public boolean authenticateUser(String email, String password) throws SQLException {
        Utente user = userDao.checkCredentials(email, password);
        return user != null; // Restituisce true se l'utente è autenticato
    }
}
