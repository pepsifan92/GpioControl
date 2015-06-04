package home.control;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.pi4j.wiringpi.Gpio;
import home.control.controller.PinController;
import home.control.controller.ThreadController;
import home.control.model.Event;
import home.control.model.PinConfiguration;
import home.control.model.Temperature;
import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

public class Main {
	private static final String HOST = "123.123.123.31";
	private static final int PORT = 1234;

	private static WebSocketServer server;


	public static void main(String[] args) throws Exception {
		generateDummyJsonObjects();
		initServer();
	}

	private static void generateDummyJsonObjects() {
		Gson gson = new Gson();
		PinConfiguration pinConfiguration = new PinConfiguration(Event.FADE, 0, 1000, 100, 0, true, 3, 0);
		Temperature temperature = new Temperature("28-00044a7273ff");
		System.out.println(gson.toJson(pinConfiguration));
		System.out.println(gson.toJson(temperature));
	}

	private static void initServer() throws Exception {

		System.out.println("Starting server...");

		new Config();

		new ThreadController();

		Gpio.wiringPiSetup();

		server = new Server(new InetSocketAddress(HOST, PORT));
		server.start();

		System.out.println("Server started on " + HOST + ":" + PORT);

		inputLoop();

		System.out.println("Stopping server...");
		server.stop();
	}

	private static void inputLoop() {
		Scanner sc = new Scanner(System.in);
		System.out.println("q - quit server \n" + "s - show connections \n" + "t - show threads");

		loop: while (sc.hasNext()) {
			String input = sc.next();
			switch (input) {
			case "q":
				break loop;
			case "s":
				printConnections(server.connections());
				break;
			case "t":
				ThreadController.printThreads();
				break;
			}
		}


		sc.close();
	}

	private static void printConnections(Collection<WebSocket> connections) {
		
		if (connections.size() < 1) {
			System.out.println("No connections");
		}
		
		for (WebSocket socket : connections) {
			System.out.println(socket.getRemoteSocketAddress().getHostName()
					+ ":" + socket.getRemoteSocketAddress().getPort());
		}
	}

	private static void printRunningThreads(List<Thread> threads) {
		System.out.println("Currently Running Threads: " + threads.size());
	}

}