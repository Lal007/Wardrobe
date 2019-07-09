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
        conn = DriverManager.getConnection("jdbc:sqlite:/home/impuls/Projects/WardrobeIDEA/Wardrobe-master/src/main/resources/DbCells.db");

        System.out.println("Connect successful");

        stmt = conn.createStatement();
    }

    public void createTable() throws SQLException {

        String checkSQL = "SELECT name FROM sqlite_master WHERE type='table' AND name='cells';";

        ResultSet rs = stmt.executeQuery(checkSQL);

        if (rs.next()){
            System.out.println("Таблица \"cells\" уже существует");
        }else {
            String sql = ("CREATE TABLE cells (id INTEGER UNIQUE NOT NULL PRIMARY KEY, card STRING);");
            stmt.execute(sql);

            String sql2 = "INSERT INTO cells (card) VALUES (NULL);";

            for (int i = 0; i < 8; i++) {
                stmt.executeUpdate(sql2);
            }
        }

    }

    public void takeCell(int cellNumber, String card) throws SQLException {
        String sql = String.format("UPDATE cells SET card = '%s' WHERE id = '%d';", card, cellNumber);
        stmt.executeUpdate(sql);
    }

    public void emptyCell(String card){
        String sql = String.format("UPDATE cells SET card = NULL WHERE card = '%s';", card);
    }

    public int getIdByCard(String card) throws SQLException {
        String sql = String.format("SELECT id FROM cells WHERE card = '%s';", card);
        ResultSet rs = stmt.executeQuery(sql);
        return rs.getInt(1);
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
