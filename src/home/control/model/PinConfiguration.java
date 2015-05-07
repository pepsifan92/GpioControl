package home.control.model;

public class PinConfiguration {

    public enum PinState {
        INPUT, OUTPUT, SOFT_PWM
    }

    private Event event;
    private PinState pinState;
    private int number;
    private String name;
    private int debounce;
    private int pwmValue;
    private boolean outputHigh;
    private long duration;
    private long uptime;
    private long downtime;
    private int startVal;
    private int endVal;
    private boolean repeat;
    private int cycles;
    private long pause;

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
        this.pwmValue = pwmValue;
    }

    /**
     * Used for FADE Events
     * @param event
     * @param number
     * @param startVal
     * @param endVal
     * @param repeat
     * @param cycles
     */
    public PinConfiguration(Event event, int number, long duration, int startVal, int endVal, boolean repeat, int cycles) {
        this.event = event;
        this.number = number;
        this.duration = duration;
        this.startVal = startVal;
        this.endVal = endVal;
        this.repeat = repeat;
        this.cycles = cycles;
        this.pause = duration / Math.abs(endVal - startVal);
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public PinState getPinState() {
        return pinState;
    }

    public void setPinState(PinState pinState) {
        this.pinState = pinState;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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

    public long getPause() {
        return pause;
    }

    public void setPause(long pause) {
        this.pause = pause;
    }
}
