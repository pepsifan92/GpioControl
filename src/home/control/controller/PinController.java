package home.control.controller;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
import home.control.Thread.PwmFadeThread;
import home.control.model.PinConfiguration;

public class PinController {

	private PinConfiguration conf;

	public PinController(PinConfiguration conf) {
		this.conf = conf;
	}

	public void set() {
		Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
		Gpio.digitalWrite(conf.getNumber(), conf.isOutputHigh());
	}

	public void dim() {
		Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
		SoftPwm.softPwmCreate(conf.getNumber(), conf.getPwmValue(), Config.PWM_RANGE);
	}

	public void fade() {
		Thread thread = new PwmFadeThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

	public void fadeUpDown() {

	}

	public void blink() {

	}

}
