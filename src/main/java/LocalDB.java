import java.sql.*;

public class LocalDB {

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet reSet;

    public void tryConnect() throws ClassNotFoundException, SQLException{
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:/home/impuls/Projects/Wardrobe2/src/DbCells.db");

        System.out.println("Connect successful");

        stmt = conn.createStatement();
    }

    public void writeToDB(int id, String card) throws SQLException {
        String sql = String.format("INSERT INTO cells (id, card) VALUES ('%d', '%s');" , id, card);
        stmt.executeUpdate(sql);

        System.out.println("Insert done");
    }

}
