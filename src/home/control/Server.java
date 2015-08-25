package home.control;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.pi4j.io.gpio.Pin;
import home.control.Thread.SendTempThread;
import home.control.controller.EventController;
import home.control.controller.PCA9685PwmControl;
import home.control.controller.ThreadController;
import home.control.model.PinConfiguration;
import home.control.model.Temperature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

public class Server extends WebSocketServer {
    private static final Logger log = Logger.getLogger( Server.class.getName() );
	private Gson gson;

//    public static WebSocket socket;
    private static List<WebSocket> webSocketList;
    private static boolean webSocketListLock = false;
    public static PCA9685PwmControl pca;

	public Server(InetSocketAddress address) {
		super(address);
        webSocketList = new ArrayList<>();
        gson = new Gson();
        pca = new PCA9685PwmControl(0x40);
        setupTemperatureThreads();
	}

    public static void sendToAllSockets(String message) {
        System.out.println("webSocketList Size: " + webSocketList.size());
        Iterator<WebSocket> iter = webSocketList.iterator();
        while (iter.hasNext()) {
            iter.next().send(message);
        }
    }

    private static void removeSocketFromList(WebSocket socket) {
        System.out.println("Removing Socket with Remote Socket Address: " + socket.getRemoteSocketAddress());

        Iterator<WebSocket> iter = webSocketList.iterator();
        while (iter.hasNext()) {
            WebSocket listSocket = iter.next();
            if (socket.getRemoteSocketAddress().equals(listSocket.getRemoteSocketAddress())) {
                iter.remove();
            }
        }
    }

    private static void waitForUnlock() {
        while (webSocketListLock) {
            sleep(100);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        webSocketList.add(conn);
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket socket, int code, String reason, boolean remote) {
        System.out.println("closed " + socket.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
        removeSocketFromList(socket);
    }

    @Override
    public void onMessage(WebSocket socket, String message) {
        long startMillis = System.currentTimeMillis();
        System.out.println("Received message from " + socket.getRemoteSocketAddress() + ": " + message);
    	//System.out.println("Incoming Message: " + message);

        PinConfiguration pinConfiguration = null;
        Temperature temperature = null;
        boolean isPinConfiguration = false;

        try {
            if(gson.fromJson(message, PinConfiguration.class).getNumber() != -1){
                isPinConfiguration = true;
                pinConfiguration = new PinConfiguration(gson.fromJson(message, PinConfiguration.class));
            } else {
                System.out.println("No PinConfiguration send");
            }
        } catch (Exception e) {
            System.out.println("Exception: No PinConfiguration send");
        }

        if (!isPinConfiguration) {
            try {
                temperature = gson.fromJson(message, Temperature.class);
            } catch (Exception e) {
                System.out.println("Exception: No Temperature send");
            }
        }

        try {
            if (isPinConfiguration) {
                EventController eventController = new EventController(pinConfiguration);
                eventController.handleEvent(pinConfiguration.getEvent());
            } else if (temperature != null) {
                EventController eventController = new EventController(temperature);
                eventController.handleEvent(temperature.getEvent());
            }
        } catch (Exception e) {
            System.out.println("No PinConfiguration or Temperature send");
            e.printStackTrace();
        }

        long endMillis = System.currentTimeMillis();
        long timeNeeded = endMillis - startMillis;
        System.out.println("Message dispatched in " + timeNeeded + "ms");
    }

    @Override
    public void onError(WebSocket socket, Exception ex) {
    	try {
    		System.err.println("an error occured on connection " + socket.getRemoteSocketAddress()  + ":" + ex);
    	} catch(Exception excep){
    		System.err.println("Error by handle Error (method: onError). Exception message: " + excep.getMessage());
    	}
        removeSocketFromList(socket);
    }

    private void setupTemperatureThreads() {
        Thread temperatureThread1 = new SendTempThread(Config.TEMPERATURE_SEND_INTERVAL, new Temperature("28-00044a7273ff")); //First sensor thread, that sends the Temperature continuously
        Thread temperatureThread2 = new SendTempThread(Config.TEMPERATURE_SEND_INTERVAL, new Temperature("28-00044a72b1ff")); //Second sensor thread, that sends the Temperature continuously
        PinConfiguration dummyPinConfig1 = new PinConfiguration(1000);
        PinConfiguration dummyPinConfig2 = new PinConfiguration(1001);

//        ThreadController.interruptThreadWhenRunningOnPin(1000);
//        ThreadController.interruptThreadWhenRunningOnPin(1001);

        ThreadController.addAndStart(temperatureThread1, dummyPinConfig1);
        ThreadController.addAndStart(temperatureThread2, dummyPinConfig2);
    }
}
