//package home.control;
//
//import com.pi4j.wiringpi.SoftPwm;
//import home.control.model.event.*;
//import home.control.model.Pin;
//
//public class PwmModes extends Thread {
//    private Pin pin;
//    private String mode;
//    private Event event;
//    private boolean stopFadeLoop = false;
//
//    PwmModes(Pin pin, Event event){
//        this.pin = pin;
//        this.event = event;
//    }
//
//    public void run(){
//        if (event instanceof PwmFadeEvent) {
//            // Fading stuff
//
//        } else if (event instanceof PwmStopEvent) {
//             // Stopping stuff
//        }
////        switch (this.mode){
////            case "fade":
////                try {
////                    setPwmFade(0,100,1000,true);
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////                break;
////            case "blink":
////                try {
////                    setBlink(50,200);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                break;
////        }
//    }
//
//    public void stopFade(){
//        stopFadeLoop = true;
//    }
//
//
//    public boolean setPwmFade(int startVal, int endVal, int milliSeconds, boolean repeat) throws InterruptedException{
//        if((startVal < 0) || (startVal > 100) || (endVal < 0) || (endVal > 100)){
//            return false; //One value is out of range.
//        }
//        do {
//            if (startVal < endVal) {
//                int pause = milliSeconds / (endVal - startVal); //Get pause-time to end up in the given milliseconds.
//                for (int i = startVal; i < endVal; i++) {
//                    if (stopFadeLoop) {
//                        stopFadeLoop = false;
//                        break;
//                    } //If fading should end
//                    SoftPwm.softPwmWrite(pin.getNumber(), i);
//                    Thread.sleep(pause);
//                }
//            } else if (startVal > endVal) {
//                int pause = milliSeconds / (startVal - endVal); //Get pause-time to end up in the given milliseconds.
//                for (int i = endVal; i < startVal; i++) {
//                    if (stopFadeLoop) {
//                        stopFadeLoop = false;
//                        break;
//                    } //If fading should end
//                    SoftPwm.softPwmWrite(pin.getNumber(), i);
//                    Thread.sleep(pause);
//                }
//            } else {
//                return false; //StartVal and endVal must be the same.
//            }
//            System.out.printf(" fadeOutput ");
//        }while(repeat && !stopFadeLoop);
//        stopFadeLoop = false;
//
//        return true;
//    }
//
//    public boolean setBlink(int milliSecondsOn, int milliSecondsOff){
//        do {
//            try {
//                SoftPwm.softPwmWrite(pin.getNumber(), 0);
//                Thread.sleep(milliSecondsOn);
//                SoftPwm.softPwmWrite(pin.getNumber(), 100);
//                Thread.sleep(milliSecondsOff);
//            } catch (InterruptedException e) {
//                SoftPwm.softPwmWrite(pin.getNumber(), 100); //If Thread gets interrupted while blinking, set val to 100
//                //System.out.println("PwmModes.setBlink e = " + e);
//            }
//            System.out.printf(" blinkOutput ");
//        } while (!stopFadeLoop);
//        return true;
//    }
//}
