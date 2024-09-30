package smu.Control;

import smu.DAO.UtenteDAO;
import smu.DAO_Implementation.UtenteDAOimp;
import smu.DTO.Utente;


import java.io.IOException;
import java.sql.SQLException;


public class LoginControl {

    public void login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Errore. Campi email e/o password sono vuoti.");
        } else {
            // chiamata al DAO
            try {
                UtenteDAO userDao = new UtenteDAOimp();
                Utente user = userDao.checkCredentials(email, password);

                if (user == null) {
                    System.out.println("Errore. Email e/o password errati.");
                } else {
                        //aggiungere il cambio di pagina
                }
            } catch (SQLException | IOException e) {
                System.err.println("Errore: " + e.getMessage());
            }
        }
    }
}
