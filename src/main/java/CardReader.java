import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class CardReader {

    private static InputStream in = null;
    private static SerialPort comPort = null;

    public void initializeReader(){
        comPort = SerialPort.getCommPort("/dev/ttyACM0");
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        in = comPort.getInputStream();

    }

    public InputStream getInputStream() {
        return in;
    }

    public void close() throws IOException {
        comPort.closePort();
        in.close();
    }
}
