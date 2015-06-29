package home.control;

import home.control.model.PinConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final int PWM_RANGE = 100;

    public static  List<PinConfiguration> allowedPwmPins = new ArrayList<>();
    public static  List<PinConfiguration> allowedSetPins = new ArrayList<>();
    public static  List<PinConfiguration> invertedPins = new ArrayList<>();

    private final int[] ALLOWED_PWM_PIN_NUMS = { 5, 6, 10, 11, 26, 27, 28, 29, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115};
    private final int[] ALLOWED_SET_PIN_NUMS = { 0, 2, 3, 4, 12 };
    private final int[] INVERTED_PIN_NUMS = { 0, 2, 3, 12 }; //Pins that turns ON the device, if the state is LOW. Example: relais-module that switches on GND.

    public Config() {
        allowedPwmPins = generatePinConfigurations(ALLOWED_PWM_PIN_NUMS);
        allowedSetPins = generatePinConfigurations(ALLOWED_SET_PIN_NUMS);
        invertedPins   = generatePinConfigurations(INVERTED_PIN_NUMS);
        allowedSetPins.addAll(allowedPwmPins); // Because PWM Pins are also allowed to be SET
    }

    private List<PinConfiguration> generatePinConfigurations(int[] nums) {
        List<PinConfiguration> pinConfigurations = new ArrayList<>();

        for (int i: nums) {
            pinConfigurations.add(new PinConfiguration(i));
        }

        return pinConfigurations;
    }
}
