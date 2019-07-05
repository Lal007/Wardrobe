import java.sql.SQLException;

public class WardrobeLogic {

    private CardReader cardReader;

    private GPIOdriver gpioDriver;

    public static void main(String[] args) {

        WardrobeLogic logic = new WardrobeLogic();

        logic.init();

        /*LocalDB localDB = new LocalDB();

        try {
            localDB.tryConnect();

            localDB.writeToDB(42, "Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


    public void init(){

        //Подключение базы данных
        LocalDB localDB = new LocalDB();
        try {
            localDB.tryConnect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Подключение к считывателю
        cardReader = CardReader.getInstance();

        //Подключение к контактам GPIO
        gpioDriver = GPIOdriver.getInstance();

    }
}
