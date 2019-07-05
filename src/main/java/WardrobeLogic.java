import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
/*
Главный класс
 */

public class WardrobeLogic {

    private static LocalDB localDB;

    private static CardReader cardReader;

    private static GPIOdriver gpioDriver;

    private static BufferedReader reader;

    public static void main(String[] args) {

        WardrobeLogic logic = new WardrobeLogic();

        logic.init();

        while (true){

            String line;
            try {
                while ((line = reader.readLine()) != null){
                    if (localDB.isExist(line)){
                        //Открываем ячейку, удаляем запись из базы
                    }else{
                        if(localDB.isAnyEmptyCell()){

                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
        localDB = new LocalDB();
        try {
            localDB.tryConnect();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Подключение к считывателю
        cardReader = CardReader.getInstance();
        reader = new BufferedReader(new InputStreamReader(cardReader.getInputStream()));

        //Подключение к контактам GPIO
        gpioDriver = GPIOdriver.getInstance();

    }
}
