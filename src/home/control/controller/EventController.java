package home.control.controller;

import home.control.Exception.PinConfigurationUnauthorisedException;
import home.control.model.Event;
import home.control.model.PinConfiguration;
import home.control.model.Temperature;

import java.io.IOException;

public class EventController {

    private PinController pinController;
    private TemperatureController temperatureController;

    public EventController(PinConfiguration pinConfiguration) {
        pinController = new PinController(pinConfiguration);
    }

    public EventController(Temperature temperature) {
        temperatureController = new TemperatureController(temperature);
    }

    public void handleEvent(Event event) throws PinConfigurationUnauthorisedException {
        switch (event) {
            case SET:
                pinController.set();
                break;
            case DIM:
                pinController.dim();
                break;
            case FADE:
                pinController.fade();
                break;
            case FADE_UP_DOWN:
                pinController.fadeUpDown();
                break;
            case BLINK:
                pinController.blink();
                break;
            case INPUT:

                break;
            case TEMP:
//                temperatureController.sendTemperature(); Why did we need this!?
                break;
            case SHUTDOWN:

                break;
            case REBOOT:

                break;
            default:

                break;
        }
    }

}
