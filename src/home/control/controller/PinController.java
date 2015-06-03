package home.control.controller;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
import home.control.Exception.PinConfigurationUnauthorisedException;
import home.control.Thread.PwmFadeThread;
import home.control.Thread.PwmFadeUpDownThread;
import home.control.model.PinConfiguration;

public class PinController {

	private PinConfiguration conf;

	public PinController(PinConfiguration conf) {
		this.conf = conf;
	}

	private void checkPwmPinAllowed() throws PinConfigurationUnauthorisedException {
		for (PinConfiguration allowedConf : Config.allowedPwmPins) {
			System.out.println("allowedConfPin: " + allowedConf.getNumber());
			if (allowedConf.getNumber() == conf.getNumber()) {
				return;
			}
		}
		throw new PinConfigurationUnauthorisedException();
	}

	private void checkSetPinAllowed() throws PinConfigurationUnauthorisedException {
		for (PinConfiguration allowedConf : Config.allowedSetPins) {
			if (allowedConf.getNumber() == conf.getNumber()) {
				return;
			}
		}
		throw new PinConfigurationUnauthorisedException();
	}

	public void set() throws PinConfigurationUnauthorisedException {
		checkSetPinAllowed();
		Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
		Gpio.digitalWrite(conf.getNumber(), conf.isOutputHigh());
	}

	public void dim() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
		SoftPwm.softPwmCreate(conf.getNumber(), conf.getPwmValue(), Config.PWM_RANGE);
	}

	public void fade() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		Thread thread = new PwmFadeThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

	public void fadeUpDown() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		Thread thread = new PwmFadeUpDownThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

	public void blink() {

	}

}
