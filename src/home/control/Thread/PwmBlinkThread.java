package home.control.Thread;

import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
import home.control.Server;
import home.control.controller.ThreadController;
import home.control.model.NaturalFading;
import home.control.model.PinConfiguration;

public class PwmBlinkThread extends Thread{

    private PinConfiguration conf;
    private volatile boolean isRunning = true; //"You really should make it volatile. There are no synchronization problems, then."

    public PwmBlinkThread(PinConfiguration conf) {
        this.conf = conf;
    }

    public void kill() {
        isRunning = false;
    }

    @Override
    public void run() {
        Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
        SoftPwm.softPwmCreate(conf.getNumber(), 0, Config.PWM_RANGE);

        if (conf.isRepeat()) {
            for (int i = 0; i < conf.getCycles(); i++) {
                if (!isRunning) { break; }
                oneBlinkWithPwmValue();
            }
        } else {
            oneBlinkWithPwmValue();
        }

        ThreadController.remove(Thread.currentThread());
    }

    private void oneBlinkWithPwmValue() {
        //Number 100-115 means the pins of a I2C PWM-PortExpander PCA9685 (16 Channel(=16 Pins))
        //100 = PWM_00; 101 = PWM_01; ... ; 115 = PWM_15
        if (conf.getNumber() < 100) { //On Pi
            SoftPwm.softPwmWrite(conf.getNumber(), conf.getPwmValue());
            pause(conf.getUptime());
            SoftPwm.softPwmWrite(conf.getNumber(), 0);
            pause(conf.getDowntime());
        } else { //On PCA9685
            Server.pca.setPwm(PCA9685Pin.ALL[conf.getNumber()-100], NaturalFading.STEPS_100[conf.getPwmValue()]);
            pause(conf.getUptime());
            Server.pca.setOff(PCA9685Pin.ALL[conf.getNumber()-100]);
            pause(conf.getDowntime());
        }
    }

    private void pause(long pause) {
        if (!isRunning) { return; }
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            System.out.println("Interrupting Thread");
            //Thread.currentThread().interrupt();
            // FUCK THE EXCEPTION!
//            e.printStackTrace();
        }
    }
}
