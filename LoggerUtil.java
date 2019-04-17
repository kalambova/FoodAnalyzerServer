package fmi.project.food.analyzer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {

	public static void init(Logger logger, String filePath) {
		try {
			FileHandler logFileHandler = new FileHandler(filePath);
			logger.addHandler(logFileHandler);
			SimpleFormatter formatter = new SimpleFormatter();
			logFileHandler.setFormatter(formatter);

		} catch (SecurityException | IOException e) {
			System.out.println("problem with file ");
		}

	}

}
