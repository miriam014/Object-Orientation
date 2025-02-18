package smu.DAOImplementation;

import smu.DAO.SpeseProgrammateDAO;
import smu.DTO.SpeseProgrammate;
import smu.Database;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpeseProgrammateDAOimp implements SpeseProgrammateDAO {

    @Override
    public boolean insert (SpeseProgrammate sp) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO  smu.SpeseProgrammate(Descrizione, Periodicita, DataScadenza, DataFineRinnovo, Importo, Destinatario, NumeroCarta, Stato)VALUES(?,?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, sp.getDescrizione());
        ps.setString(2, sp.getPeriodicita());
        ps.setDate(3, sp.getDataScadenza());
        ps.setDate(4, sp.getFineRinnovo());
        ps.setFloat(5, sp.getImporto());
        ps.setString(6, sp.getDestinatario());
        ps.setString(7, sp.getNumeroCarta());
        ps.setBoolean(8, sp.getStato());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(SpeseProgrammate sp) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.SpeseProgrammate SET Descrizione = ?, Periodicita = ?, DataScadenza = ?, DataFineRinnovo = ?, Importo = ?, Destinatario = ?, NumeroCarta = ?, Stato = ? WHERE IDSpesa = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, sp.getDescrizione());
        ps.setString(2, sp.getPeriodicita());
        ps.setDate(3, sp.getDataScadenza());
        ps.setDate(4, sp.getFineRinnovo());
        ps.setFloat(5, sp.getImporto());
        ps.setString(6, sp.getDestinatario());
        ps.setString(7, sp.getNumeroCarta());
        ps.setBoolean(8, sp.getStato());
        ps.setInt(9, sp.getIdSpesa());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(Integer idSpesa) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.SpeseProgrammate WHERE IDSpesa = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, idSpesa);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public SpeseProgrammate getByID(String idSpesa) throws SQLException {
        Connection connection = Database.getConnection();
        SpeseProgrammate sp = null;

        String sql = "SELECT * FROM smu.SpeseProgrammate WHERE IDSpesa = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, idSpesa);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            sp = new SpeseProgrammate(rs.getInt("IDSpesa"),
                    rs.getString("Periodicita"),
                    rs.getDate("DataScadenza"),
                    rs.getFloat("Importo"),
                    rs.getString("Destinatario"),
                    rs.getDate("DataFineRinnovo"),
                    rs.getString("Descrizione"),
                    rs.getString("NumeroCarta"),
                    rs.getBoolean("Stato"));
        }
        rs.close();
        ps.close();
        return sp;
    }

    @Override
    public List<SpeseProgrammate> getByUsername(String username) throws SQLException{

        Connection connection = Database.getConnection();
        List<SpeseProgrammate> list = new ArrayList<>();

        String sql = "SELECT  S.IdSpesa, S.NumeroCarta, S.Descrizione,S.Importo, S.Periodicita, S.DataScadenza, S.DataFineRinnovo, S.Importo, S.Destinatario, S.Stato, CC.Username " +
                    "FROM smu.SpeseProgrammate AS S NATURAL JOIN (smu.Carta AS C JOIN smu.ContoCorrente AS CC ON C.NumeroConto= CC.NumeroConto) WHERE Username = ?;";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            SpeseProgrammate sp = new SpeseProgrammate(rs.getInt("IDSpesa"),
                    rs.getString("Periodicita"),
                    rs.getDate("DataScadenza"),
                    rs.getFloat("Importo"),
                    rs.getString("Destinatario"),
                    rs.getDate("DataFineRinnovo"),
                    rs.getString("Descrizione"),
                    rs.getString("NumeroCarta"),
                    rs.getBoolean("Stato"));
            list.add(sp);
        }
        rs.close();
        ps.close();
        return list;
    }

}
