//package home.control.controller;
//
//import home.control.PwmFadeThread;
//import home.control.model.event.*;
//
//public class PwmController {
//
//    private PwmEvent pwmEvent;
//    private PwmFadeThread pwmThread;
//
//    public PwmController() {}
//
//    public PwmController(PwmEvent pwmEvent) {
//        this.pwmEvent = pwmEvent;
//    }
//
//    /**
//     * Second Level PwmEvent check
//     * @param pwmEvent
//     */
//    public void determinePwmEvent(PwmEvent pwmEvent) {
////        if (pwmEvent instanceof PwmFadeEvent) {
////            startPwmFade((PwmFadeEvent) pwmEvent);
////        } else if(pwmEvent instanceof PwmSetEvent) {
////
////        } else if (pwmEvent instanceof PwmStopEvent) {
////            stopPwmFadeEvent((PwmStopEvent) pwmEvent);
////        }
//    }
//
////    private void startPwmFade(PwmFadeEvent pwmFadeEvent) {
////        pwmThread = new PwmFadeThread(pwmFadeEvent);
////    }
//
//    private void setPwm(PwmSetEvent pwmSetEvent) {
//        pwmSetEvent.pin.setPwmValue(pwmSetEvent.value);
//    }
//
//    private void stopPwmFadeEvent(PwmStopEvent event) {
//        pwmThread.stopFade();
//        pwmThread = null;
//    }
//
//}
