package home.control.Exception;

import home.control.Thread.PwmFadeUpDownThread;

/**
 * Created by Michael on 09.08.2015.
 */
public class PwmValueOutOfRangeException extends Exception{
    public PwmValueOutOfRangeException(){
        super();
    }
    public PwmValueOutOfRangeException(String message){
        super(message);
    }
}
