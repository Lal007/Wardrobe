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

        logic.init(); // Инициализация всех подключений

        while (true){ // Основной цикл

            String card;
            try {
                if ((card = reader.readLine()) != null){
                    if (localDB.isExist(card)){ // Если карта есть в базе
                        //Открываем ячейку, удаляем запись из базы
                        localDB.emptyCell(card);
                        gpioDriver.open(localDB.getIdByCard(card));

                    }else{
                        //Проверяем наличие свободной ячейки, получаем ее номер и записываем в базу с сохранением номера карочки, открываем ячейку
                        if(localDB.isAnyEmptyCell()){
                            int cell = localDB.getEmptyCell();
                            localDB.takeCell(cell, card);
                            gpioDriver.open(cell);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void init(){

        //Подключение локальной базы данных
        localDB = new LocalDB();
        try {
            localDB.tryConnect();
            localDB.createTable(); // Создание таблицы (если она есть, то ничего не будет изменено)
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
