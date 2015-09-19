package home.control.Base;

public class Timer {

    private long startMillis;
    private long endMillis;

    public void startTimer() {
        startMillis = System.currentTimeMillis();
    }

    public void stopTimer() {
        endMillis = System.currentTimeMillis();
    }

    public long getTimeNeeded() {
        return endMillis - startMillis;
    }
}
