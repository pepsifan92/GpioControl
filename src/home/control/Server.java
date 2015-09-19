package home.control;

import com.google.gson.Gson;
import home.control.Base.Timer;
import home.control.Exception.PinConfigurationUnauthorisedException;
import home.control.Thread.SendTempThread;
import home.control.controller.EventController;
import home.control.controller.PCA9685PwmControl;
import home.control.controller.ThreadController;
import home.control.model.PinConfiguration;
import home.control.model.Temperature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class Server extends WebSocketServer {
    private static final Logger log = Logger.getLogger(Server.class.getName());
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
        Timer timer = new Timer();
        timer.startTimer();
        System.out.println("Received message from " + socket.getRemoteSocketAddress() + ": " + message);

        try {
            handleMessage(message);
        } catch (PinConfigurationUnauthorisedException e) {
            e.printStackTrace();
        }

        timer.stopTimer();
        System.out.println("Message dispatched in " + timer.getTimeNeeded() + "ms");
    }

    private void handleMessage(String message) throws PinConfigurationUnauthorisedException {
        PinConfiguration conf = getPinconfigurationFromMessage(message);
        EventController eventController = new EventController(conf);
        eventController.handleEvent(conf.getEvent());

    }

    private PinConfiguration getPinconfigurationFromMessage(String message) {
        return gson.fromJson(message, PinConfiguration.class);
    }

    @Override
    public void onError(WebSocket socket, Exception ex) {
        try {
            System.err.println("an error occured on connection " + socket.getRemoteSocketAddress() + ":" + ex);
        } catch (Exception excep) {
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
