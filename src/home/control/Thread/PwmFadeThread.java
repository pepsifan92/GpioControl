package home.control.Thread;

import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import com.sun.deploy.pings.Pings;
import home.control.Config;
import home.control.PCA9685PwmControl;
import home.control.Server;
import home.control.controller.ThreadController;
import home.control.model.NaturalFading;
import home.control.model.PinConfiguration;

public class PwmFadeThread extends Thread{

    private PinConfiguration conf;
    private volatile boolean isRunning = true; //"You really should make it volatile. There are no synchronization problems, then."
    private long stepPause;

    public PwmFadeThread(PinConfiguration conf) {
        this.conf = conf;
        stepPause = conf.getCycleDuration() / Math.abs(conf.getEndVal() - conf.getStartVal());
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
                pwmLoop();
                pause(conf.getCyclePause()); //Pause between the cycles
            }
        } else {
            pwmLoop();
        }

        ThreadController.remove(Thread.currentThread());
    }

    private void pwmLoop() {

        if (conf.getStartVal() < conf.getEndVal()) {
            fadeUp();
        } else {
            fadeDown();
        }

    }

    private void fadeUp() {
        for (int i = conf.getStartVal(); i < conf.getEndVal() ; i++) {
            if (!isRunning) { break; }
            fade(i);
        }
        Server.socket.send("Fading Up Message from Server");
    }

    private void fadeDown() {
        for (int i = conf.getStartVal(); i > conf.getEndVal(); i--) {
            if (!isRunning) { break; }
            fade(i);
        }
        Server.socket.send("Fading Down Message from Server");
    }

    private void fade(int i) {
        //Number 100-115 means the pins of a I2C PWM-PortExpander PCA9685 (16 Channel(=16 Pins))
        //100 = PWM_00; 101 = PWM_01; ... ; 115 = PWM_15
        if(conf.getNumber() < 100) {
            SoftPwm.softPwmWrite(conf.getNumber(), i);
        } else {
            setPCAPinPwm(i);
        }
        pause(stepPause);
    }

    //Defines which pin of the PCA9685 is set for the given PinNumber.
    private void setPCAPinPwm(int value){
        switch(conf.getNumber()){
            case 100: Server.pca.setPwm(PCA9685Pin.PWM_00, value); break;
            case 101: Server.pca.setPwm(PCA9685Pin.PWM_01, value); break;
            case 102: Server.pca.setPwm(PCA9685Pin.PWM_02, value); break;
            case 103: Server.pca.setPwm(PCA9685Pin.PWM_03, value); break;
            case 104: Server.pca.setPwm(PCA9685Pin.PWM_04, value); break;
            case 105: Server.pca.setPwm(PCA9685Pin.PWM_05, value); break;
            case 106: Server.pca.setPwm(PCA9685Pin.PWM_06, value); break;
            case 107: Server.pca.setPwm(PCA9685Pin.PWM_07, value); break;
            case 108: Server.pca.setPwm(PCA9685Pin.PWM_08, value); break;
            case 109: Server.pca.setPwm(PCA9685Pin.PWM_09, value); break;
            case 110: Server.pca.setPwm(PCA9685Pin.PWM_10, value); break;
            case 111: Server.pca.setPwm(PCA9685Pin.PWM_11, value); break;
            case 112: Server.pca.setPwm(PCA9685Pin.PWM_12, value); break;
            case 113: Server.pca.setPwm(PCA9685Pin.PWM_13, value); break;
            case 114: Server.pca.setPwm(PCA9685Pin.PWM_14, value); break;
            case 115: Server.pca.setPwm(PCA9685Pin.PWM_15, value); break;
            default: break;
        }
    }

    private void pause(long pause) {
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
