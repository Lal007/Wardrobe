import java.sql.SQLException;

public class WardrobeLogik {
    public static void main(String[] args) {
        LocalDB localDB = new LocalDB();

        try {
            localDB.tryConnect();

            localDB.writeToDB(42, "Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
