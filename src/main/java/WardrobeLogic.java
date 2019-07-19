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

    private static RemoteDB remoteDB;

    private static GPIOdriver gpioDriver;

    private static BufferedReader reader;

    private static boolean ready = false;

    private static boolean remoteDBConnected = false;

    private static final Logger log = Logger.getLogger(WardrobeLogic.class);

    private static WardrobeLogic logic = new WardrobeLogic();

    public static void main(String[] args) {

        logic.init(); // Инициализация всех подключений
        logic.initRemoteDB();
        if (remoteDBConnected){
            gpioDriver.turnOnReadyLed(PinState.HIGH); //Зажигаем зеленый светодиод
        }else{
            gpioDriver.pulseReadyLed();//Мигаем зеленым светодиодом
        }
        logic.checkCapacity(); //Проверка на загруженность системы хранения
        logic.MainLogic();

    }

    public void init(){

        //Подключение локальной базы данных
        localDB = new LocalDB();
        try {
            localDB.tryConnect();
            localDB.createTable(); // Создание таблицы (если она есть, то ничего не будет изменено)
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //Подключение к считывателю
        CardReader cardReader = CardReader.getInstance();
        reader = new BufferedReader(new InputStreamReader(cardReader.getInputStream()));

        //Подключение к контактам GPIO
        gpioDriver = GPIOdriver.getInstance();
        
        //Подключение конфигуратора для логера
        String log4jConfigPath = "/home/impuls/WardrobePro/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfigPath);

        ready = true;

    }

    public void initRemoteDB() {
        remoteDB = new RemoteDB();
        try {
            if (remoteDB.tryConnect()){
                log.info("Успешное подключение к удаленной БД");
                remoteDBConnected = true;
            }
        } catch (SQLException e) {
            log.error("Ошибка инициализации удаленной БД");
            logic.reconnectRemoteDB();
        }
    }

    public void MainLogic(){
        while (true) { // Основной цикл
            System.out.println("Wait for card");

            String card;
            try {
                if ((card = reader.readLine()) != null){
                    System.out.println("Card = " + card);
                    log.info("Считана карта: " + card);
                    int prefix = CardConverter.getPrefixDec(card);
                    int cardCode = CardConverter.getCardCode(card);

                    if (ready){
                        if (localDB.isExist(card)){ // Если карта есть в локальной базе
                            //Открываем ячейку, удаляем запись из базы
                            logic.emptyCell(card);

                        }else if (remoteDBConnected && remoteDB.isCardValid(prefix, cardCode)){
                            //Проверяем наличие свободной ячейки, получаем ее номер и записываем в базу с сохранением номера карточки и имени, открываем ячейку
                            logic.takeCell(card, remoteDB.getName(prefix, cardCode));
                        }else {
                            gpioDriver.pulseErrLed();
                            logic.checkConnect();
                        }
                    }

                }
            } catch (IOException e) {
                log.error(e.toString());
            } catch (SQLException e) {
                gpioDriver.pulseErrLed();
                logic.reconnectRemoteDB();
            }
        }
    }

    public void checkCapacity(){
        try {
            if (localDB.isAnyEmptyCell()){
                gpioDriver.turnOnFullLed(PinState.LOW);
            }else {
                gpioDriver.turnOnFullLed(PinState.HIGH);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (remoteDB.isConnect()){
                            break;
                        }else {
                            gpioDriver.turnOnReadyLed(PinState.LOW);
                        }
                    } catch (SQLException e) {
                        log.error("Подключение закрыто");
                        gpioDriver.turnOnReadyLed(PinState.LOW);
                        logic.reconnectRemoteDB();
                        break;
                    }
                }
            }
        }).start();
    }

    public void emptyCell(String card) throws SQLException {
        int cell = localDB.getIdByCard(card);
        gpioDriver.open(cell);
        localDB.emptyCell(card);
        logic.checkCapacity();
        System.out.println("Card exist");
        log.info("Карте " + card + " соответствует ячейка № " + cell + ". Ячейка освобождена");
        gpioDriver.shineDown();
        logic.delay();
    }

    public void takeCell(String card, String name) throws SQLException {
        if(localDB.isAnyEmptyCell()){
            int cell = localDB.getEmptyCell();
            localDB.takeCell(cell, card);
            logic.checkCapacity();
            System.out.println("Empty cell = " + cell);
            gpioDriver.open(cell);
            log.info("Работнику " + name + " присвоена пустая ячейка № " + cell + " , номер карты: " + card);
            gpioDriver.shineUP();
            logic.delay();
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

    public void reconnectRemoteDB(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    remoteDBConnected = false;

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        remoteDB.tryConnect();
                        remoteDBConnected = true;
                        gpioDriver.turnOnReadyLed(PinState.HIGH);
                        break;
                    } catch (SQLException e) {
                       log.error("Неудачная попытка подключения к удаленной БД");
                    }
                }
            }
        }).start();
    }
}
