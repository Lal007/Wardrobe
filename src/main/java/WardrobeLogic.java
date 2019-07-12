import com.pi4j.io.gpio.PinState;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

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

    private static boolean ready = false;

    private static final Logger log = Logger.getLogger(WardrobeLogic.class);

    public static void main(String[] args) {

        WardrobeLogic logic = new WardrobeLogic();

        logic.init(); // Инициализация всех подключений
        gpioDriver.turnOnReadyLed(PinState.HIGH); //Зажигаем зеленый светодиод

        try {
            if (localDB.isAnyEmptyCell()){
                gpioDriver.turnOnFullLed(PinState.LOW);
            }else {
                gpioDriver.turnOnFullLed(PinState.HIGH);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        while (true){ // Основной цикл
            System.out.println("Wait for card");            
            
            String card;
            try {
                if ((card = reader.readLine()) != null){
                    System.out.println("Card = " + card);
                    log.info("Считана карта: " + card);

                    if (ready){
                        if (localDB.isExist(card)){ // Если карта есть в базе
                            //Открываем ячейку, удаляем запись из базы
                            int cell = localDB.getIdByCard(card);
                            gpioDriver.open(cell);
                            localDB.emptyCell(card);
                            System.out.println("Card exist");
                            log.info("Карте " + card + " соответствует ячейка № " + cell + ". Ячейка освобождена");
                            gpioDriver.shineDown();
                            logic.delay();

                        }else{
                            //Проверяем наличие свободной ячейки, получаем ее номер и записываем в базу с сохранением номера карочки, открываем ячейку
                            if(localDB.isAnyEmptyCell()){
                                int cell = localDB.getEmptyCell();
                                localDB.takeCell(cell, card);
                                System.out.println("Empty cell = " + cell);
                                gpioDriver.open(cell);
                                log.info("Карте " + card + " присвоена пустая ячейка № " + cell);
                                gpioDriver.shineUP();
                                logic.delay();
                            }
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
        
        //Подключение конфигуратора для логера
        String log4jConfigPath = "/home/impuls/Projects/WardrobeIDEA/Wardrobe-master/src/main/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfigPath);

        ready = true;

    }

    public void skipLines(){
        String line;
        while (true) {
            try {
                if ((line = reader.readLine()) != null){
                    reader.readLine();
                }else break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void delay(){
        new Thread(new Runnable() {
            public void run() {
                gpioDriver.turnOnReadyLed(PinState.LOW);
                ready = false;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                gpioDriver.turnOnReadyLed(PinState.HIGH);
                ready = true;
            }
        }).start();
    }
}
