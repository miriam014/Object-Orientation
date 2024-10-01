package smu;
import java.sql.*;

public class Database {
    private static Connection instance;

    public static Connection getConnection() throws SQLException {
        if(instance == null){
            try{
                Class.forName("org.postgresql.Driver");
                instance = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "1234");
                System.out.println("Connesso al database!");
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return instance;
    }
    public static void cleanConnection(){
        try{
            instance.close();
            instance = null;
        }
        catch(SQLException ex){
            System.out.println("Errore: " + ex.getMessage());
        }
    }
}
