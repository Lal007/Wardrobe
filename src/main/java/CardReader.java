import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class CardReader {

    private static SerialPort comPort = null;
    private static CardReader instance = null;

    private CardReader() {
        comPort = SerialPort.getCommPort("/dev/ttyACM0");
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
    }

    public static CardReader getInstance() {
        if (instance == null) {
            instance = new CardReader();
        }
        return instance;
    }

    public InputStream getInputStream() {
        return comPort.getInputStream();
    }

    public void close() throws IOException {
        comPort.closePort();
        comPort = null;
        instance = null;
    }
}
