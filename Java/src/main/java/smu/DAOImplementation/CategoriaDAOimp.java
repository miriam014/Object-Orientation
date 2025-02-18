package smu.DAOImplementation;

import smu.DAO.CategoriaDAO;
import smu.DTO.Categoria;
import smu.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAOimp implements CategoriaDAO {

    @Override
    public boolean insert(Categoria category) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "INSERT INTO smu.Categoria(NomeCategoria, ParoleChiavi) VALUES(?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, category.getNomeCategoria());
        ps.setString(2, category.getParoleChiavi());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean update(Categoria category) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "UPDATE smu.Categoria SET  ParoleChiavi = ? WHERE NomeCategoria = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, category.getParoleChiavi());
        ps.setString(2, category.getNomeCategoria());

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    @Override
    public boolean delete(String categoryName) throws SQLException {
        Connection connection = Database.getConnection();
        String sql = "DELETE FROM smu.Categoria WHERE NomeCategoria = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, categoryName);

        int result = ps.executeUpdate();
        ps.close();
        return result != 0;
    }

    public List<String> getAllCategorie()throws Exception{
        List<String> categorie= new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection = Database.getConnection();
            String sql = "SELECT NomeCategoria FROM smu.Categoria";
            ps = connection.prepareStatement(sql);

            rs = ps.executeQuery();
            while(rs.next()){
                categorie.add(rs.getString("NomeCategoria"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Errore durante il recupero delle categorie", e);
        }
        ps.close();
        return categorie;
    }

}
