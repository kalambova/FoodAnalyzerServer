package fmi.project.food.analyzer.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import fmi.project.food.analyzer.LoggerUtil;
import fmi.project.food.analyzer.server.ClientCommands;

public class FoodAnalyzerClient {
	private static Logger logger = Logger.getLogger(FoodAnalyzerClient.class.getName());
	private static final String LOG_FILE_PATH = "logs/clientLogFile" + System.currentTimeMillis() + ".log";

	private static final String IMG_TAG = "--img=";
	private static final String UPC_TAG = "--upc=";
	private PrintWriter writer;
	private Socket socket;
	static {
		LoggerUtil.init(logger, LOG_FILE_PATH);
	}

	private void connect(String host, int port) {
		try {

			socket = new Socket(host, port);
			writer = new PrintWriter(socket.getOutputStream(), true);

			System.out.println("successfuly opened a socket");
			ClientRunnable clientRunnable = new ClientRunnable(socket);
			new Thread(clientRunnable).start();
		} catch (IOException e) {
			logger.log(Level.WARNING,
					"=> cannot connect to server on" + host + ":" + port + " , make sure that the surver is running",
					e);
		}
	}

	private static String decodeImage(InputStream inputStream) {
		try {
			BinaryBitmap bitmap = new BinaryBitmap(
					new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(inputStream))));
			if (bitmap.getWidth() < bitmap.getHeight()) {
				if (bitmap.isRotateSupported()) {
					bitmap = bitmap.rotateCounterClockwise();
				}
			}
			return decode(bitmap);
		} catch (IOException e) {
			logger.log(Level.WARNING, "File is not found", e);

		} catch (NotFoundException | ChecksumException | FormatException e) {
			logger.log(Level.WARNING, e.getMessage(), e);

		}
		return "";
	}

	private static String decode(BinaryBitmap bitmap) throws NotFoundException, ChecksumException, FormatException {
		Reader reader = new MultiFormatReader();
		Result result = reader.decode(bitmap);
		return result.getText();
	}

	private String convertBarcodeCommand(String commandContent) throws FileNotFoundException {
		String result = "";
		if (commandContent.contains(IMG_TAG) && commandContent.contains(UPC_TAG)) {
			String[] commandParts = commandContent.split(" ");
			for (String part : commandParts) {
				if (part.startsWith(UPC_TAG)) {
					result = ClientCommands.BY_BARCODE.getCommand() + " " + part;
				}
			}
		} else {
			if (commandContent.startsWith(IMG_TAG)) {
				final int barcodeIndex = 6;
				String path = commandContent.substring(barcodeIndex);
				File initialFile = new File(path);
				InputStream targetStream = new FileInputStream(initialFile);
				result = ClientCommands.BY_BARCODE.getCommand() + " " + UPC_TAG + decodeImage(targetStream);
			}
		}

		return result;
	}

	private void run() throws IOException {
		System.out.println("Welcome! Please connect.");

		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String input = scanner.nextLine();
				String[] splited = input.split(" ");
				String commandID = splited[0];
				String commandContent = input.substring(commandID.length()).trim();
				ClientCommands command = ClientCommands.findCommand(commandID);
				switch (command) {
					case CONNECT:
						String host = splited[1];
						int port = Integer.parseInt(splited[2]);
						connect(host, port);
						break;
					case BY_BARCODE:
						if (commandContent.contains(IMG_TAG)) {
							writer.println(convertBarcodeCommand(commandContent));
						} else
							writer.println(input);
	
						break;
					default:
						writer.println(input);
						break;
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new FoodAnalyzerClient().run();
		String a = "Gosho";
		a.no
	}
}
