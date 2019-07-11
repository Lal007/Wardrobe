import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.CommandArgumentParser;
/*
Управление контактами
 */

import java.util.ArrayList;

public class GPIOdriver {

    private static GPIOdriver instance = null;

    private ArrayList<GpioPinDigitalOutput> pins = new ArrayList<GpioPinDigitalOutput>();

    private GPIOdriver() {
        initialize();
    }

    public static GPIOdriver getInstance(){
        if (instance == null){
            instance = new GPIOdriver();

        }
        return instance;
    }


    private void initialize(){
        try {
            PlatformManager.setPlatform(Platform.ORANGEPI);
        } catch (PlatformAlreadyAssignedException e) {
            e.printStackTrace();
        }

        final GpioController gpio = GpioFactory.getInstance();


        Pin orangePin0 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_00 , "-p 0");
        Pin orangePin3 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_03 , "-p 3");
        Pin orangePin12 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_12 , "-p 12");
        Pin orangePin13 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_13 , "-p 13");
        Pin orangePin14 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_14 , "-p 14");
        Pin orangePin21 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_21 , "-p 21");
        Pin orangePin22 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_22 , "-p 22");
        Pin orangePin24 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_24 , "-p 24");

        Pin orangePin27 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_27 , "-p 27");
        Pin orangePin28 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_28 , "-p 28");
        Pin orangePin29 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_29 , "-p 29");

        final GpioPinDigitalOutput pin0 = gpio.provisionDigitalOutputPin(orangePin0, "My Pin0", PinState.LOW);
        final GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(orangePin3, "My Pin3", PinState.LOW);
        final GpioPinDigitalOutput pin12 = gpio.provisionDigitalOutputPin(orangePin12, "My Pin12", PinState.LOW);
        final GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(orangePin13, "My Pin13", PinState.LOW);
        final GpioPinDigitalOutput pin14 = gpio.provisionDigitalOutputPin(orangePin14, "My Pin14", PinState.LOW);
        final GpioPinDigitalOutput pin21 = gpio.provisionDigitalOutputPin(orangePin21, "My Pin21", PinState.LOW);
        final GpioPinDigitalOutput pin22 = gpio.provisionDigitalOutputPin(orangePin22, "My Pin22", PinState.LOW);
        final GpioPinDigitalOutput pin24 = gpio.provisionDigitalOutputPin(orangePin24, "My Pin24", PinState.LOW);

        final GpioPinDigitalOutput readyPin = gpio.provisionDigitalOutputPin(orangePin27, "My Pin27", PinState.LOW);
        final GpioPinDigitalOutput errPin = gpio.provisionDigitalOutputPin(orangePin28, "My Pin28", PinState.LOW);
        final GpioPinDigitalOutput fullPin = gpio.provisionDigitalOutputPin(orangePin29, "My Pin29", PinState.LOW);

        pin0.setShutdownOptions(true, PinState.LOW);
        pin3.setShutdownOptions(true, PinState.LOW);
        pin12.setShutdownOptions(true, PinState.LOW);
        pin13.setShutdownOptions(true, PinState.LOW);
        pin14.setShutdownOptions(true, PinState.LOW);
        pin21.setShutdownOptions(true, PinState.LOW);
        pin22.setShutdownOptions(true, PinState.LOW);
        pin24.setShutdownOptions(true, PinState.LOW);

        pins.add(pin0);
        pins.add(pin3);
        pins.add(pin12);
        pins.add(pin13);
        pins.add(pin14);
        pins.add(pin21);
        pins.add(pin22);
        pins.add(pin24);

        pins.add(readyPin);
        pins.add(errPin);
        pins.add(fullPin);
    }

    public void open(int cell){
        switch (cell){
            case (1): pins.get(0).pulse(1000, true);
                    break;
            case (2): pins.get(1).pulse(1000, true);
                    break;
            case (3): pins.get(2).pulse(1000, true);
                    break;
            case (4): pins.get(3).pulse(1000, true);
                    break;
            case (5): pins.get(4).pulse(1000, true);
                    break;
            case (6): pins.get(5).pulse(1000, true);
                    break;
            case (7): pins.get(6).pulse(1000, true);
                    break;
            case (8): pins.get(7).pulse(1000, true);
                    break;
        }
    }

    public void turnOnReadyLed(PinState state){
        GpioPinDigitalOutput pin = pins.get(8);
        if (state.isHigh()){
            pin.high();
        }else {
            pin.low();
        }
    }

    public void turnOnErrLed(PinState state){
        GpioPinDigitalOutput pin = pins.get(9);
        if (state.isHigh()){
            pin.high();
        }else {
            pin.low();
        }
    }

    public void turnOnFullLed(PinState state){
        GpioPinDigitalOutput pin = pins.get(10);
        if (state.isHigh()){
            pin.high();
        }else {
            pin.low();
        }
    }

    public void shineUP(){
        GpioPinDigitalOutput green = pins.get(8);
        GpioPinDigitalOutput blue = pins.get(9);
        GpioPinDigitalOutput red = pins.get(10);

        PinState greenState = green.getState();
        PinState blueState = blue.getState();
        PinState redState = red.getState();

        green.setState(PinState.LOW);
        blue.setState(PinState.LOW);
        red.setState(PinState.LOW);

        red.pulse(500, true);
        blue.pulse(500, true);
        green.pulse(500, true);

        green.setState(greenState);
        blue.setState(blueState);
        red.setState(redState);
    }

    public void shineDown(){
        GpioPinDigitalOutput green = pins.get(8);
        GpioPinDigitalOutput blue = pins.get(9);
        GpioPinDigitalOutput red = pins.get(10);

        PinState greenState = green.getState();
        PinState blueState = blue.getState();
        PinState redState = red.getState();

        green.setState(PinState.LOW);
        blue.setState(PinState.LOW);
        red.setState(PinState.LOW);

        green.pulse(500, true);
        blue.pulse(500, true);
        red.pulse(500, true);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        green.setState(greenState);
        blue.setState(blueState);
        red.setState(redState);
    }

}
