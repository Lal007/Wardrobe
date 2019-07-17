import java.sql.*;
import java.util.concurrent.Executors;

public class RemoteDB {


    private Statement stmt;

    public boolean tryConnect() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://core32:1433;databaseName=RedRoseIII;user=UserTest;password=ASDFG_1";

        Connection conn = null;
        String testSQL = ("SELECT 1;");
        ResultSet rs = null;

        conn = DriverManager.getConnection(connectionUrl);
        conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), 2000);
        stmt = conn.createStatement();

        rs = stmt.executeQuery(testSQL);

        return rs.next();
    }

    public boolean isConnect() throws SQLException {
         //Проверка подключения
        String testSQL = ("SELECT 1;");
        ResultSet rs = stmt.executeQuery(testSQL);

        return rs.next();
    }

    public boolean isCardValid(int prefix, int code) throws SQLException {
        String sql = String.format("SELECT * FROM [RedRoseIII].[dbo].[Cards], [RedRoseIII].[dbo].[Users] WHERE Cards.CardPrefix = '%d' AND Cards.CardCode = '%d' AND Cards.UserID = Users.ID;", prefix, code);

        ResultSet rs = stmt.executeQuery(sql);

        return rs.next();
    }

    public String getName(int prefix, int code) throws SQLException {
        String sql = String.format("SELECT * FROM [RedRoseIII].[dbo].[Cards], [RedRoseIII].[dbo].[Users] WHERE Cards.CardPrefix = '%d' AND Cards.CardCode = '%d' AND Cards.UserID = Users.ID;", prefix, code);

        ResultSet rs = stmt.executeQuery(sql);

        if (rs.next()){
            return rs.getString("Name");
        }else return "Имя не найдено";
    }
}
