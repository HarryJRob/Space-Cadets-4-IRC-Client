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
		
		/* Event handler for buttonMakeServer
		 * Creates a new tab in the tab pane and sets the content equal to chatTab.fxml
		 * The tab uses the port and IP provided to try and connect
		 * Select the new tab and start it on it's own thread
		 */
		buttonMakeServer.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if (!textFieldServerIP.getText().equals("") && !textFieldServerPort.getText().equals("")) {
					
					Tab newTab = new Tab();
					try {
						newTab.setContent(FXMLLoader.load(getClass().getResource("chatTab.fxml")));
						newTab.setText("Unnamed Server");
						chatTabPane.getSelectionModel().select(newTab);
						chatTabPane.getTabs().add(newTab);
						
						new Thread(new IRCConnection(newTab, textFieldServerIP.getText(), Integer.parseInt(textFieldServerPort.getText()))).start();
						
					} catch (Exception e1) {
						System.out.println(e1.toString());
					}
				}
			}
			
		});
	}
}
