package smu.DAO_Implementation;

import smu.DAO.TransazioneDAO;
import smu.DTO.Transazione;
import smu.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransazioneDAOimp implements TransazioneDAO {

    @Override
    public boolean insert(Transazione transaction) throws SQLException{
        Connection connection = Database.getConnection();
        String sql ="INSERT INTO smu.Transazione(Importo, Data, Ora, Causale, Tipo, Mittente, Destinatario, NumeroCarta, NomeCategoria) VALUES(?,?,?,?,?,?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setFloat(1, transaction.getImporto());
        ps.setDate(2, transaction.getData());
        ps.setTime(3, transaction.getOra());
        ps.setString(4, transaction.getCausale());
        ps.setString(5, transaction.getTipoTransazione());
        ps.setString(6, transaction.getMittente());
        ps.setString(7, transaction.getDestinatario());
        ps.setString(8, transaction.getNumeroCarta());
        ps.setString(9, transaction.getCategoria());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(Transazione transaction) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Transazione SET Importo = ?, Data = ?, Ora = ?, Causale = ?, Tipo = ?, Mittente = ?, Destinatario = ?, NumeroCarta = ?, NomeCategoria = ? WHERE IDTransazione = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setFloat(1, transaction.getImporto());
        ps.setDate(2, transaction.getData());
        ps.setTime(3, transaction.getOra());
        ps.setString(4, transaction.getCausale());
        ps.setString(5, transaction.getTipoTransazione());
        ps.setString(6, transaction.getMittente());
        ps.setString(7, transaction.getDestinatario());
        ps.setString(8, transaction.getNumeroCarta());
        ps.setString(9, transaction.getCategoria());
        ps.setString(10, transaction.getIDTransazione());


        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(String idTransazione) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Transazione WHERE IDTransazione = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, idTransazione);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public Transazione getByID(String idTransazione) throws SQLException {
        Connection connection = Database.getConnection();
        Transazione transaction = null;

        String sql = "SELECT * FROM smu.Transazione WHERE IDTransazione = ?";
        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, idTransazione);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            String id2 = rs.getString("IDTransazione");
            String cro = rs.getString("CRO");
            Float importo = rs.getFloat("Importo");
            Date data = rs.getDate("Data");
            Time ora = rs.getTime("Ora");
            String causale = rs.getString("Causale");
            String tipo = rs.getString("Tipo");
            String mittente = rs.getString("Mittente");
            String destinatario = rs.getString("Destinatario");
            String numeroCarta = rs.getString("NumeroCarta");
            String categoria = rs.getString("NomeCategoria");
            transaction = new Transazione(id2,cro,importo,data,ora,causale,tipo,mittente,destinatario,numeroCarta,categoria);
        }

        rs.close();
        ps.close();
        return transaction;
    }

    @Override
    public List<Transazione> getByCardNumber(String cardNumber, String x) throws SQLException {

        Connection connection = Database.getConnection();
        List<Transazione> transazioni = new ArrayList<>();


        String sql = "SELECT * FROM smu.Transazione WHERE NumeroCarta = ? ";
        if( x != null) {
            if (x.equals("Entrata")) {  //se è entrata allora le transazioni di tipo uscita vengono ignorate
                sql += "AND Tipo = 'Entrata' ";
            } else if (x.equals("Uscita")) {
                sql += "AND Tipo = 'Uscita' ";
            }
        }
        sql += "ORDER BY Data DESC, Ora DESC";


        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, cardNumber);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String id = rs.getString("IDTransazione");
            String cro = rs.getString("CRO");
            Float importo = rs.getFloat("Importo");
            Date data = rs.getDate("Data");
            Time ora = rs.getTime("Ora");
            String causale = rs.getString("Causale");
            String tipo = rs.getString("Tipo");
            String mittente = rs.getString("Mittente");
            String destinatario = rs.getString("Destinatario");
            String numeroCarta = rs.getString("NumeroCarta");
            String categoria = rs.getString("NomeCategoria");

            Transazione transaction = new Transazione(id,cro,importo,data,ora,causale,tipo,mittente,destinatario,numeroCarta,categoria);
            transazioni.add(transaction);
        }
        rs.close();
        ps.close();

        return transazioni;
    }

    @Override
    public List<Transazione> getByCategory(String category) throws SQLException {
        Connection connection = Database.getConnection();
        List<Transazione> list = new ArrayList<>();

        String sql = "SELECT * FROM smu.Transazione WHERE NomeCategoria = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, category);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String id = rs.getString("IDTransazione");
            String cro = rs.getString("CRO");
            Float importo = rs.getFloat("Importo");
            Date data = rs.getDate("Data");
            Time ora = rs.getTime("Ora");
            String causale = rs.getString("Causale");
            String tipo = rs.getString("Tipo");
            String mittente = rs.getString("Mittente");
            String destinatario = rs.getString("Destinatario");
            String numeroCarta = rs.getString("NumeroCarta");
            String categoria2 = rs.getString("NomeCategoria");

            Transazione transaction = new Transazione(id, cro, importo, data, ora, causale, tipo, mittente, destinatario, numeroCarta, categoria2);
            list.add(transaction);
        }
        rs.close();
        ps.close();
        return list;
    }

    @Override
    public List<Transazione> getByWalletId(String walletId) throws SQLException{
        Connection connection = Database.getConnection();
        List<Transazione> list = new ArrayList<>();

        String sql = "SELECT T.IdTransazione, T.CRO, T.Importo, T.Data, T.Ora, T.Causale, T.Tipo, T.Mittente, T.Destinatario, T.NumeroCarta, T.NomeCategoria " +
                "FROM smu.TransazioniInPortafogli AS TP JOIN smu.Transazione AS T ON TP.IdTransazione = T.IdTransazione WHERE TP.IdPortafoglio= ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Integer.parseInt(walletId));
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String id = rs.getString("IDTransazione");
            String cro = rs.getString("CRO");
            Float importo = rs.getFloat("Importo");
            Date data = rs.getDate("Data");
            Time ora = rs.getTime("Ora");
            String causale = rs.getString("Causale");
            String tipo = rs.getString("Tipo");
            String mittente = rs.getString("Mittente");
            String destinatario = rs.getString("Destinatario");
            String numeroCarta = rs.getString("NumeroCarta");
            String categoria = rs.getString("NomeCategoria");

            Transazione transaction = new Transazione(id, cro, importo, data, ora, causale, tipo, mittente, destinatario, numeroCarta, categoria);
            list.add(transaction);
        }
        rs.close();
        ps.close();
        return list;
    }

}

