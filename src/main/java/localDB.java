import java.sql.*;

public class localDB {

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet reSet;

    public void tryConnect() throws ClassNotFoundException, SQLException{
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:/home/impuls/Projects/Wardrobe2/src/DbCells.db");
    }

}
