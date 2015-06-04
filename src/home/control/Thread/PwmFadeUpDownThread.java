package home.control.Thread;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
import home.control.Server;
import home.control.model.PinConfiguration;

public class PwmFadeUpDownThread extends Thread {

    private PinConfiguration conf;
    private volatile boolean isRunning = true;
    private long stepPause;

    public PwmFadeUpDownThread(PinConfiguration conf) {
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
                pause(conf.getCyclePause());
                pwmLoopReverse();
            }
        } else {
            pwmLoop();
            pause(conf.getCyclePause());
            pwmLoopReverse();
        }
    }

    private void pwmLoop() {

        if (conf.getStartVal() < conf.getEndVal()) {
            fadeUp();
        } else {
            fadeDown();
        }

    }

    private void pwmLoopReverse() {
        if (conf.getStartVal() < conf.getEndVal()) {
            fadeDown();
        } else  {
            fadeUp();
        }
    }

    private void fadeUp() {
        for (int i = conf.getStartVal(); i < conf.getEndVal(); i++) {
            if (!isRunning) { break; }
            fade(i);
        }
        Server.socket.send("Fading Up Message from Server");
    }

    private void fadeDown() {
        for (int i = conf.getStartVal(); i > conf.getEndVal(); i++) {
            if (!isRunning) { break; }
            fade(i);
        }
        Server.socket.send("Fading Down Message from Server");
    }

    private void fade(int i) {
        SoftPwm.softPwmWrite(conf.getNumber(), i);

    }

    private void pause(long pause) {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            System.out.println("Interrupting Thread");
        }
    }


}
