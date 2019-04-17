package fmi.project.food.analyzer.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {

	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			while (true) {
				if (socket.isClosed()) {
					System.out.println("Client socket is closed, stop waiting for server messages");
					return;
				}
				String command = reader.readLine();
			
				if (command != null) {
					System.out.println(command);
				}
			}
		}

		catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("IOException thrown when closing client socket.");

			}
		}
	}

}
