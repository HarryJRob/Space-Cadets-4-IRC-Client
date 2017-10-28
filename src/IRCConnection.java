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
	
	public IRCConnection(Tab clientTab, String serverAddress, int serverPort) {
		//Get the components for this tab
		try {
			inputFromUser = (TextField) clientTab.getContent().lookup("#userEntry");
			outputToUser = (TextArea) clientTab.getContent().lookup("#ouputToUser");
			this.clientTab = clientTab;
			
			//Create a socket to use with the server
			clientSocket = new Socket(serverAddress,serverPort);
			
		} catch (SocketException socketE) {
			System.out.println(socketE.toString());
			outputToUser.setText("Cannot find this server: \n" + serverAddress + " : " + serverPort );
			
		} catch (Exception e) {
			
			System.out.println(e.toString());
			
		}
	}
	
	//Runnable method
	@Override
	public void run() {
		try {
			outputToServer = new PrintWriter(clientSocket.getOutputStream(), true);
			inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			//Get the server name
			{
				String serverName = inputFromServer.readLine();
				outputToUser.appendText("[Server] - Welcome to " + serverName);
				clientTab.setText(serverName);
			}
			
			outputToUser.appendText("\n[Server] - Please Enter a nickname");
			//The next message sent will be the users nickname
			
			//Start a new input listener to append the messages received to the TextArea
			Thread inputThread = new Thread(new listener());
			inputThread.start();
			
			//Set the event handler for closing a tab
			clientTab.setOnCloseRequest(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					outputToServer.println("!quit");
					//Close exisiting connections
					try {
						inputThread.interrupt();
						outputToServer.close();
						inputFromServer.close();
						clientSocket.close();
						Thread.currentThread().interrupt();
					} catch (Exception e) {
						System.out.println(e.toString());
					}
				}
				
			});
			
			//Set the event handler for when the enter key is pressed
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
			
		} catch (Exception e) { System.out.println(e.toString()); }
	}
	
	//A simple input listener which implements Runnable
	private class listener implements Runnable {
		
		// If there is a line to read from the server then append it as a new line to the TextArea
		@Override
		public void run() { 
			while(true) {
				try {
					outputToUser.appendText("\n" + inputFromServer.readLine());
				} catch (IOException e) {
					System.out.println(e.toString());
					break;
				} 
			}
		}
		
	}
	
}