package home.control;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import home.control.Exception.PinConfigurationUnauthorisedException;
import home.control.controller.EventController;
import home.control.model.PinConfiguration;
import home.control.model.Temperature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;

public class Server extends WebSocketServer {
    private static final Logger log = Logger.getLogger( Server.class.getName() );
	private Gson gson;

    public static WebSocket socket;

	public Server(InetSocketAddress address) {
		super(address);
        gson = new Gson();
	}

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        socket = conn;
        System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket socket, int code, String reason, boolean remote) {
        System.out.println("closed " + socket.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket socket, String message) {
        long startMillis = System.currentTimeMillis();
        System.out.println("Received message from " + socket.getRemoteSocketAddress() + ": " + message);
    	System.out.println("Incoming Message: " + message);

        PinConfiguration pinConfiguration = null;
        Temperature temperature = null;

        try {
            pinConfiguration = gson.fromJson(message, PinConfiguration.class);
        } catch (Exception e) {
            System.out.println("No PinConfiguration send");
        }

        if (pinConfiguration.getNumber() == -1) {
            try {
                temperature = gson.fromJson(message, Temperature.class);
            } catch (Exception e) {
                System.out.println("No Temperature send");
            }
        }

        try {
            if (pinConfiguration.getNumber() != -1) {
                EventController eventController = new EventController(pinConfiguration);
                eventController.handleEvent(pinConfiguration.getEvent());
            } else if (temperature != null) {
                EventController eventController = new EventController(temperature);
                eventController.handleEvent(temperature.getEvent());
            }

        } catch (Exception e) {
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

    }
}
