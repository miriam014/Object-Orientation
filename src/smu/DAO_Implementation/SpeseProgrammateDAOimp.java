package smu.DAO_Implementation;

import smu.DAO.SpeseProgrammateDAO;
import smu.DTO.SpeseProgrammate;
import smu.Database;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpeseProgrammateDAOimp implements SpeseProgrammateDAO {

    public boolean insert (SpeseProgrammate sp) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta)VALUES(?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, sp.getDescrizione());
        ps.setString(2, sp.getPeriodicita());
        ps.setDate(3, sp.getDataScadenza());
        ps.setDate(4, sp.getFineRinnovo());
        ps.setFloat(5, sp.getImporto());
        ps.setString(6, sp.getDestinatario());
        ps.setString(7, sp.getNumeroCarta());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean update(SpeseProgrammate sp) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.SpeseProgrammate SET Descrizione = ?, Periodicita = ?, DataScadenza = ?, DataFineRinnovo = ?, Importo = ?, Destinatario = ?, NumeroCarta = ? WHERE IDSpesa = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, sp.getDescrizione());
        ps.setString(2, sp.getPeriodicita());
        ps.setDate(3, sp.getDataScadenza());
        ps.setDate(4, sp.getFineRinnovo());
        ps.setFloat(5, sp.getImporto());
        ps.setString(6, sp.getDestinatario());
        ps.setString(7, sp.getNumeroCarta());
        ps.setString(8, sp.getIdSpesa());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public boolean delete(String idSpesa) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.SpeseProgrammate WHERE IDSpesa = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, idSpesa);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }
}
