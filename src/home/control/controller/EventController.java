package home.control.controller;

import home.control.model.Event;
import home.control.model.PinConfiguration;

public class EventController {

    private PinController pinController;

    public EventController(PinConfiguration pinConfiguration) {
        pinController = new PinController(pinConfiguration);
    }

    public void handleEvent(Event event) {
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
            case SHUTDOWN:

                break;
            case REBOOT:

                break;
            default:

                break;
        }
    }

}
