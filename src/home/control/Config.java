package home.control;

import home.control.model.PinConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final int PWM_RANGE = 100;

    public static  List<PinConfiguration> allowedPwmPins = new ArrayList<>();
    public static  List<PinConfiguration> allowedSetPins = new ArrayList<>();

    private final int[] ALLOWED_PWM_PIN_NUMS = { 5, 6, 10, 11, 26, 27, 28, 29 };
    private final int[] ALLOWED_SET_PIN_NUMS = { 0, 2, 3, 4, 12 };

    public Config() {
        allowedPwmPins = generatePinConfigurations(ALLOWED_PWM_PIN_NUMS);
        allowedSetPins = generatePinConfigurations(ALLOWED_SET_PIN_NUMS);
        allowedSetPins.addAll(allowedPwmPins); // Because PWM Pins are allowed to get set
    }

    private List<PinConfiguration> generatePinConfigurations(int[] nums) {
        List<PinConfiguration> pinConfigurations = new ArrayList<>();

        for (int i: nums) {
            pinConfigurations.add(new PinConfiguration(i));
        }

        return pinConfigurations;
    }
}