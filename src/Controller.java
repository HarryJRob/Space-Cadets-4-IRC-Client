import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class Controller {

	@FXML private TabPane chatTabPane;
	@FXML private Button buttonMakeServer;
	@FXML private TextField textFieldServerIP;
	@FXML private TextField textFieldServerPort;
	
	public void initialize() { 
		
		buttonMakeServer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				Tab newTab = new Tab();
				try {
					newTab.setContent(FXMLLoader.load(getClass().getResource("chatTab.fxml")));
					newTab.setText("Unnamed Server");
					chatTabPane.getSelectionModel().select(newTab);
				} catch (IOException e1) {
					System.out.println(e.toString());
				}
				
				if (textFieldServerIP.getText().length() != 0 && textFieldServerPort.getText().length() != 0) {
					chatTabPane.getTabs().add(newTab);
					new Thread(new IRCConnection(newTab, textFieldServerIP.getText(), Integer.parseInt(textFieldServerPort.getText()))).start();
				}
			}
			
		});
	}
}
