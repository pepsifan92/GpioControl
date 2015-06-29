package home.control.Thread;

import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Server;
import home.control.controller.TemperatureController;
import home.control.controller.ThreadController;
import home.control.model.NaturalFading;
import home.control.model.Temperature;

public class SendTempThread extends Thread{

    private long sendFrequency;
    private TemperatureController temperatureController;
    private volatile boolean isRunning = true; //"You really should make it volatile. There are no synchronization problems, then."

    public SendTempThread(long frequency, Temperature temperature) {
        this.sendFrequency = frequency;
        this.temperatureController = new TemperatureController(temperature);
    }

    public void kill() {
        isRunning = false;
    }

    @Override
    public void run() {
        while(isRunning) {
            try {
                temperatureController.sendTemperature();
                pause(sendFrequency);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ThreadController.remove(Thread.currentThread());
    }

    private void pause(long pause) {
        if (!isRunning) { return; }
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            System.out.println("Interrupting Thread");
        }
    }
}
