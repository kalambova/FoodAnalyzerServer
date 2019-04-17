package fmi.project.food.analyzer.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.HttpClient;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import fmi.project.food.analyzer.LoggerUtil;
import fmi.project.food.analyzer.food.objects.Item;

public class FoodAnalyzerServer {
	private static final String CACHE_FILE = "foodsCache.ser";
	private static final int PORT = 8080;
	private static final String LOG_FILE_PATH = "logs/serverLogFile" + System.currentTimeMillis() + ".log";
	public static Hashtable<String, Item> foodsCache;
	private static Logger logger = Logger.getLogger(FoodAnalyzerServer.class.getName());
	static {
		readCache();
		LoggerUtil.init(logger, LOG_FILE_PATH);
	}

	private static void readCache() {
		try (FileInputStream fileIn = new FileInputStream(CACHE_FILE);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {

			foodsCache = (Hashtable<String, Item>) in.readObject();

		} catch (IOException i) {
			foodsCache = new Hashtable<String, Item>();
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Class is not found.", e);
		}

	}

	public synchronized static void writeCache() {
		try (FileOutputStream fileOut = new FileOutputStream(CACHE_FILE);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

			out.writeObject(foodsCache);
		} catch (IOException e) {
			logger.log(Level.WARNING, "Couldn't write cache into the file: " + CACHE_FILE, e);
		}
	}

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.printf("Server is running on localhost:%d%n", PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("A client connected to server" + socket.getInetAddress());

				ClientConnectionRunnable runnable = new ClientConnectionRunnable(socket, HttpClient.newHttpClient());
				new Thread(runnable).start();

			}

		} catch (IOException e) {
			logger.log(Level.WARNING, "maybe another server is running on port 8080", e);
		}
	}
}
