import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class IRC_Client extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		Parent root = null;
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Terminal.fxml"));
			root = fxmlLoader.load();
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		primaryStage.setMinWidth(600);
		primaryStage.setMinHeight(400);
		primaryStage.setWidth(600);
		primaryStage.setHeight(400);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		primaryStage.setTitle("IRC Client");
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("Terminal.css").toExternalForm());
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
