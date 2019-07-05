import java.sql.*;
/*
Подключение к локальной базе
 */

public class LocalDB {

    private static Connection conn;
    private static Statement stmt;

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

    public boolean isExist(String card) throws SQLException {
        String sql = String.format("SELECT card FROM cells WHERE card = '%s';", card);
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public boolean isAnyEmptyCell() throws SQLException {
        String sql = ("SELECT card FROM cells WHERE card is NULL;");
        ResultSet rs = stmt.executeQuery(sql);
        return rs.next();
    }

    public int getEmptyCell() throws SQLException {
        String sql = ("SELECT id FROM cells WHERE card is NULL;");
        ResultSet rs = stmt.executeQuery(sql);
        return rs.getInt(1);
    }

}
