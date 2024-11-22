package smu.DAO;

import smu.DTO.Categoria;

import java.sql.SQLException;

public interface CategoriaDAO {

    boolean insert(Categoria category) throws SQLException;
    boolean update(Categoria category) throws SQLException;
    boolean delete(String categoryName) throws SQLException;
}
