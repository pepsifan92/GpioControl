package home.control.controller;

import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import home.control.Config;
import home.control.Exception.PinConfigurationUnauthorisedException;
import home.control.Server;
import home.control.Thread.PwmBlinkThread;
import home.control.Thread.PwmFadeThread;
import home.control.Thread.PwmFadeUpDownThread;
import home.control.model.NaturalFading;
import home.control.model.PinConfiguration;

public class PinController {

	private PinConfiguration conf;

	public PinController(PinConfiguration conf) {
		this.conf = conf;
	}

	private void checkPwmPinAllowed() throws PinConfigurationUnauthorisedException {
		for (PinConfiguration allowedConf : Config.allowedPwmPins) {
			//System.out.println("allowedConfPin: " + allowedConf.getNumber());
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

	private boolean isPinInverted() throws PinConfigurationUnauthorisedException {
		for (PinConfiguration invertedConf : Config.invertedPins) {
			if (invertedConf.getNumber() == conf.getNumber()) {
				return true;
			}
		}
		return false;
	}

	public void set() throws PinConfigurationUnauthorisedException {
		boolean tempIsHighConfig;
		checkSetPinAllowed();
		ThreadController.interruptThreadWhenRunningOnPin(conf.getNumber());

		if(isPinInverted()){
			tempIsHighConfig = !conf.isOutputHigh();
		} else {
			tempIsHighConfig = conf.isOutputHigh();
		}

		if(conf.getNumber() < 100) {
			Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
			Gpio.digitalWrite(conf.getNumber(), tempIsHighConfig);
		} else { //Focus on Pins of the PCA9685 board
			if (tempIsHighConfig) {
				Server.pca.setOn(PCA9685Pin.ALL[conf.getNumber()-100]);
			} else {
				Server.pca.setOff(PCA9685Pin.ALL[conf.getNumber()-100]);
			}
		}
	}

	public void dim() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		ThreadController.interruptThreadWhenRunningOnPin(conf.getNumber());
		if(conf.getNumber() < 100) {
			Gpio.pinMode(conf.getNumber(), Gpio.OUTPUT);
			SoftPwm.softPwmCreate(conf.getNumber(), conf.getPwmValue(), Config.PWM_RANGE);
		} else { //Focus on Pins of the PCA9685 board
			Server.pca.setPwm(PCA9685Pin.ALL[conf.getNumber()-100], NaturalFading.STEPS_100[conf.getPwmValue()]);
		}
	}

	public void fade() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		//Selection of Board (PCA9685) inside thread
		Thread thread = new PwmFadeThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

	public void fadeUpDown() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		//Selection of Board (PCA9685) inside thread
		Thread thread = new PwmFadeUpDownThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

	public void blink() throws PinConfigurationUnauthorisedException {
		checkPwmPinAllowed();
		//Selection of Board (PCA9685) inside thread
		Thread thread = new PwmBlinkThread(conf);
		ThreadController.addAndStart(thread, conf);
	}

}
