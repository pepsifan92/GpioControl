package home.control.model;

import com.pi4j.io.gpio.Pin;

public class PinConfiguration {

    private final int PWM_OFF_VALUE = 0;
    private final int PWM_MAX_VALUE = 100;

    private Event event;
    private int number = -1;
    private String name;
    private int debounce;
    private int pwmValue;
    private boolean outputHigh;
    private long cycleDuration;
    private long uptime;
    private long downtime;
    private int startVal;
    private int endVal;
    private boolean repeat;
    private int cycles;
    private long cyclePause;

    public PinConfiguration() {}

    public PinConfiguration(PinConfiguration pinConfig) {
        this.event = pinConfig.event;
        this.number = pinConfig.number;
        this.name = pinConfig.name;
        this.debounce = pinConfig.debounce;
        this.pwmValue = correctPwmValue(pinConfig.pwmValue);
        this.outputHigh = pinConfig.outputHigh;
        this.cycleDuration = pinConfig.cycleDuration;
        this.uptime = pinConfig.uptime;
        this.downtime = pinConfig.downtime;
        this.startVal = correctPwmValue(pinConfig.startVal);
        this.endVal = correctPwmValue(pinConfig.endVal);
        this.repeat = pinConfig.repeat;
        this.cycles = pinConfig.cycles;
        this.cyclePause = pinConfig.cyclePause;
    }

    public PinConfiguration(int number) {
        this.number = number;
    }

    /**
     * Used for SET EVENT
     * @param event
     * @param number
     * @param outputHigh
     */
    public PinConfiguration(Event event, int number, boolean outputHigh) {
        this.event = event;
        this.number = number;
        this.outputHigh = outputHigh;
    }

    /**
     * Used for DIM Event
     * @param event
     * @param number
     * @param pwmValue
     */
    public PinConfiguration(Event event, int number, int pwmValue) {
        this.event = event;
        this.number = number;
        this.pwmValue = correctPwmValue(pwmValue);
    }

    /**
     *
     * @param event Event to run
     * @param number the number of the Pin
     * @param cycleDuration How long should a cycle (one repeating) last
     * @param startVal start value for the PWM fader
     * @param endVal end value of the PWM fader
     * @param repeat defines if the effect should run only once or more times
     * @param cycles the amount of repeatings
     * @param cyclePause the pause between the repeatings in ms
     */
    public PinConfiguration(Event event, int number, long cycleDuration, int startVal, int endVal, boolean repeat, int cycles, int cyclePause) {
        this.event = event;
        this.number = number;
        this.cycleDuration = cycleDuration;
        this.startVal = correctPwmValue(startVal);
        this.endVal = correctPwmValue(endVal);
        this.repeat = repeat;
        this.cycles = cycles;
        this.cyclePause = cyclePause;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDebounce() {
        return debounce;
    }

    public void setDebounce(int debounce) {
        this.debounce = debounce;
    }

    public int getPwmValue() {
        return pwmValue;
    }

    public void setPwmValue(int pwmValue) {
        this.pwmValue = pwmValue;
    }

    public boolean isOutputHigh() {
        return outputHigh;
    }

    public void setOutputHigh(boolean outputHigh) {
        this.outputHigh = outputHigh;
    }

    public long getCycleDuration() {
        return cycleDuration;
    }

    public void setCycleDuration(long cycleDuration) {
        this.cycleDuration = cycleDuration;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public long getDowntime() {
        return downtime;
    }

    public void setDowntime(long downtime) {
        this.downtime = downtime;
    }

    public int getStartVal() {
        return startVal;
    }

    public void setStartVal(int startVal) {
        this.startVal = startVal;
    }

    public int getEndVal() {
        return endVal;
    }

    public void setEndVal(int endVal) {
        this.endVal = endVal;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public long getCyclePause() {
        return cyclePause;
    }

    public void setCyclePause(long cyclePause) {
        this.cyclePause = cyclePause;
    }

    public int correctPwmValue(int value){
        if(value > PWM_MAX_VALUE){
            System.err.println("PinConfiguration:correctPwmValue:value to high. Set to " + PWM_MAX_VALUE);
            return PWM_MAX_VALUE;
        } else if (value < PWM_OFF_VALUE) {
            System.err.println("PinConfiguration:correctPwmValue:value to low. Set to " + PWM_OFF_VALUE);
            return PWM_OFF_VALUE;
        } else {
            return value;
        }
    }
}
