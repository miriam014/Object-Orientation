package smu.Control;

import smu.DAO.SpeseProgrammateDAO;
import smu.DAOImplementation.SpeseProgrammateDAOimp;
import smu.DTO.SpeseProgrammate;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SpeseProgrammateControl {

    private SpeseProgrammateDAO speseProgrammateDAO;

    public SpeseProgrammateControl() {
        this.speseProgrammateDAO = new SpeseProgrammateDAOimp();
    }

    // Ottieni tutte le spese programmate per l'utente
    public List<SpeseProgrammate> getSpeseProgrammateByUsername(String username) throws SQLException {
        return speseProgrammateDAO.getByUsername(username);
    }

    // Elimina una spesa programmata
    public void deleteSpesa(int idSpesa) throws SQLException {
        speseProgrammateDAO.delete(idSpesa);
    }

    // Gestisce il pagamento della spesa programmata
    public boolean pagaSpesa(SpeseProgrammate spesa) throws SQLException {
        LocalDate dataRinnovo = spesa.getDataScadenza().toLocalDate();
        LocalDate dataAttuale = LocalDate.now();

        if (dataAttuale.isAfter(dataRinnovo) || dataAttuale.isEqual(dataRinnovo)) {
            spesa.setStato(true); // Imposta la spesa come pagata
            return speseProgrammateDAO.update(spesa); // Restituisce se l'update è avvenuto con successo
        }
        return false; // Se la data attuale non è valida per il pagamento
    }

    // Altri metodi per creare e modificare le spese possono essere aggiunti qui
}