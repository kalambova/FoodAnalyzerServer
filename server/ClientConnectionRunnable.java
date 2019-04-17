package fmi.project.food.analyzer.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

import fmi.project.food.analyzer.LoggerUtil;
import fmi.project.food.analyzer.food.objects.FoodResponse;
import fmi.project.food.analyzer.food.objects.Item;
import fmi.project.food.analyzer.food.objects.ReportWrapper;

public class ClientConnectionRunnable implements Runnable {
	private static final String KEY = "m2htzF4O9M6xKHiDIchKChJeAYfApc4DhHSptzAW";
	private static final String API_URL = "https://api.nal.usda.gov";
	private static final String LOG_FILE_PATH = "logs/serverRequestsLogFile" + System.currentTimeMillis() + ".log";
	private static final int ERROR_NUMBER = 404;
	private HttpClient client;
	
	private  static Logger logger = Logger.getLogger(ClientConnectionRunnable.class.getName());
	private Socket socket;
	static {
		LoggerUtil.init(logger, LOG_FILE_PATH);
	}

	public ClientConnectionRunnable(Socket socket, HttpClient client) {
		this.socket = socket;
		this.client = client;
		
	}

	public  FoodResponse getFood(String name) throws IOException, InterruptedException {
		String url = API_URL + "/ndb/search/?q=" + name + "&api_key=" + KEY;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        try {
        	HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        	if (response.statusCode() == ERROR_NUMBER)
        	{
        		return null;
        	}
        	String json = client.send(request, BodyHandlers.ofString()).body();
    		Gson gson = new Gson();
    		FoodResponse foods = gson.fromJson(json, FoodResponse.class);
    		if (foods != null) {
    			for (Item food : foods.getList().getItem())
    			{
    				if (!food.getUPC().equals(""))
    				{
    					FoodAnalyzerServer.foodsCache.put(food.getUPC(), food);
    				}
    			}
    			
    		}
    		return foods;
        } catch (Exception e)
        {
        	throw new RuntimeException(e);
        }
		

	}

	public  ReportWrapper getFoodReport(String nbdno) throws IOException, InterruptedException {
		String url = API_URL + "/ndb/nutrients/?format=json&api_key=" + KEY
				+ "&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno=" + nbdno;
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			if (response.statusCode() == ERROR_NUMBER)
			{
				return null;
			}
			String json = client.send(request, BodyHandlers.ofString()).body();
			Gson gson = new Gson();
			ReportWrapper report = gson.fromJson(json, ReportWrapper.class);
			return report;
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	
	}

	public  String getFoodByBarCode(String upcCode) throws IOException, InterruptedException {
		Item food = FoodAnalyzerServer.foodsCache.get(upcCode);
		if (food != null) {
			
			return getFoodReport(food.getNdbno()).toString();
		}
		return String.format("Food with UPC %s is not found in the cache.", upcCode);
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
			while (true) {
				String commandInput = reader.readLine();
				if (commandInput == null) {
					continue;
				}
				String[] splited = commandInput.split("\\s+");
				String commandID = splited[0];
				String commandContent = commandInput.substring(commandID.length()).trim();
				String result;
				ClientCommands command = ClientCommands.findCommand(commandID);

				switch (command) {
				    case FOOD_INFO:
				    	FoodResponse food = getFood(commandContent);
				    	result = food.toString();
				    	break;

				    case FOOD_REPORT:
				    	ReportWrapper report = getFoodReport(commandContent);
				    	if (report == null) {
				    		result = "Not found food with this nbdno:" + commandContent;
				    	} else {
				    		result = report.toString();
				    	}

				    	break;
					case BY_BARCODE:
						final int barcodeIndex = 6;
						result = getFoodByBarCode(commandContent.substring(barcodeIndex).trim());
						break;
					case EXIT:
						FoodAnalyzerServer.writeCache();
						result = "Bye";
						writer.println(result);
						break;
					default:
						result = "Invalid command" + commandContent;
				}
				writer.println(result);

			}

		} catch (IOException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			try {
				socket.close();
			} catch (IOException e1) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		} 
	}

}
