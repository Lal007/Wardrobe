import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.CommandArgumentParser;
/*
Управление контактами
 */

import java.util.LinkedList;

public class GPIOdriver {

    private static GPIOdriver instance = null;
    private LinkedList<GpioPinDigitalOutput> pins = new LinkedList<GpioPinDigitalOutput>();

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

        GpioController gpio = GpioFactory.getInstance();


        Pin orangePin0 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_00 , "-p 0");
        Pin orangePin3 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_03 , "-p 3");
        Pin orangePin12 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_12 , "-p 12");
        Pin orangePin13 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_13 , "-p 13");
        Pin orangePin14 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_14 , "-p 14");
        Pin orangePin21 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_21 , "-p 21");
        Pin orangePin22 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_22 , "-p 22");
        Pin orangePin24 = CommandArgumentParser.getPin(OrangePiPin.class, OrangePiPin.GPIO_24 , "-p 24");

        final GpioPinDigitalOutput pin0 = gpio.provisionDigitalOutputPin(orangePin0, "My Pin0", PinState.LOW);
        final GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(orangePin3, "My Pin3", PinState.LOW);
        final GpioPinDigitalOutput pin12 = gpio.provisionDigitalOutputPin(orangePin12, "My Pin12", PinState.LOW);
        final GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(orangePin13, "My Pin13", PinState.LOW);
        final GpioPinDigitalOutput pin14 = gpio.provisionDigitalOutputPin(orangePin14, "My Pin14", PinState.LOW);
        final GpioPinDigitalOutput pin21 = gpio.provisionDigitalOutputPin(orangePin21, "My Pin21", PinState.LOW);
        final GpioPinDigitalOutput pin22 = gpio.provisionDigitalOutputPin(orangePin22, "My Pin22", PinState.LOW);
        final GpioPinDigitalOutput pin24 = gpio.provisionDigitalOutputPin(orangePin24, "My Pin24", PinState.LOW);

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

}
