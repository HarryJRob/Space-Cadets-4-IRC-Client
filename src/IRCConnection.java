import java.net.*;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;

public class IRCConnection implements Runnable {
	private Socket clientSocket;
	
	private PrintWriter outputToServer;
	private BufferedReader inputFromServer;
	
	private TextField inputFromUser;
	private TextArea outputToUser;
	private Tab clientTab;
	
	private Thread inputListener;
	
	public IRCConnection(Tab clientTab, String serverAddress, int serverPort) {
		try {
			inputFromUser = (TextField) clientTab.getContent().lookup("#userEntry");
			outputToUser = (TextArea) clientTab.getContent().lookup("#ouputToUser");
			this.clientTab = clientTab;
			
			clientSocket = new Socket(serverAddress,serverPort);
			
		} catch (SocketException socketE) {
			System.out.println(socketE.toString());
			outputToUser.setText("Cannot find this server: \n" + serverAddress + " : " + serverPort );
			
		} catch (Exception e) {
			
			System.out.println(e.toString());
			
		}
	}
	
	@Override
	public void run() {
		try {
			outputToServer = new PrintWriter(clientSocket.getOutputStream(), true);
			inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			{
				String serverName = inputFromServer.readLine();
				outputToUser.appendText("[Server] - Welcome to " + serverName);
				try {
				clientTab.setText(serverName);
				} catch (Exception e) {
					System.out.println(e.toString());
				}
			}
			outputToUser.appendText("\n[Server] - Please Enter a nickname");

			inputListener = new Thread(new listener());
			inputListener.start();
			
			clientTab.setOnCloseRequest(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					outputToServer.println("!quit");
					try {
						outputToServer.close();
						inputFromServer.close();
						clientSocket.close();
						inputListener.interrupt();
						Thread.currentThread().interrupt();
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				
			});
			
			inputFromUser.setOnKeyPressed(new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent event) {
					if(event.getCode().equals(KeyCode.ENTER)) {
						outputToServer.println(inputFromUser.getText());
						inputFromUser.setText("");
					}
				}
					
			});
			
		} catch (IOException e) {
			outputToUser.appendText("Cannot find server");
			System.out.println(e.toString());
		}
	}
	
	private class listener implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					outputToUser.appendText("\n" + inputFromServer.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(e.toString());
					break;
				} 
			}
		}
		
	}
	
}