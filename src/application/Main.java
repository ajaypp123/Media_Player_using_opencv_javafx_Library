package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

	public void start(Stage primaryStage) {
		
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Mediabuild.fxml"));
			Parent root = loader.load();
			MediabuildController controller = (MediabuildController)loader.getController();
			controller.init(primaryStage);
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Ajay Media Player");
			
			scene.setOnMouseClicked((MouseEvent event) -> {
				if (event.getClickCount() == 2) {
					primaryStage.setFullScreen(!primaryStage.isFullScreen());
				}
				});
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("a.png")));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
