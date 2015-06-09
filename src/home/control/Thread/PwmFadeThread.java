package home.control.Thread;

import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
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
        this.stepPause = calculateStepPause();
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
        for (int i = conf.getStartVal(); i <= conf.getEndVal() ; i = i+stepSize()) {
            if (!isRunning) { break; }
            fade(i);
        }
        //Server.socket.send("Fading Up Message from Server");
    }


    private void fadeDown() {
        for (int i = conf.getStartVal(); i >= conf.getEndVal(); i = i-stepSize()) {
            if (!isRunning) { break; }
            fade(i);
        }
        //Server.socket.send("Fading Down Message from Server");
    }

    private void fade(int i) {
        //Number 100-115 means the pins of a I2C PWM-PortExpander PCA9685 (16 Channel(=16 Pins))
        //100 = PWM_00; 101 = PWM_01; ... ; 115 = PWM_15
        if (conf.getNumber() < 100) {
            SoftPwm.softPwmWrite(conf.getNumber(), i);
        } else {
            Server.pca.setPwm(PCA9685Pin.ALL[conf.getNumber()-100], NaturalFading.STEPS_100[i]); //Example: PinNumber 104 should fire on Pin 04 of the PCA9685.
        }
        pause(stepPause * stepSize()); //Needed to match to the perhaps higher stepSize in the fade-loops (in fadeUp and fadeDown).
    }

    /**
     * returns the step size for fading-loops, based on stepPause value.
     * If the pause between the steps (calculated in constructor with cycleDuration time, start- and endvalue)
     * is small, the PWM-set-steps can be bigger without a visual effect but with a great reduce of pwm set interval.
     * This should enhance the performance.
     * @return
     */
    private int stepSize() {
        if(stepPause <= 10 && stepPause > 5){
            return 2;
        } else if (stepPause <= 5 && stepPause > 2) {
            return 5;
        } else if (stepPause <= 2) {
            return 10;
        } else {
            return 1;
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

    private long calculateStepPause(){
        if(conf.getEndVal() - conf.getStartVal() != 0) { //Ok
            return conf.getCycleDuration() / Math.abs(conf.getEndVal() - conf.getStartVal());
        } else { //Prevent division by zero
            System.err.println("PwmFadeThread:EndVal-StartVal=0. Choose valid values! (stepPause will be set to 10)");
            return 10;
        }
    }
}
