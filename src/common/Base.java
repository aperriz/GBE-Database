package common;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Base extends Application {   
   
	public static Stage globalStage;
	public static Scene globalScene;

	@Override
   public void start(Stage stage) throws Exception {
		
		
      Parent root = 
         FXMLLoader.load(getClass().getResource("/common/home.fxml"));
      
      globalStage = new Stage();
      
      globalScene = new Scene(root);
      globalScene.getStylesheets().add("/common/style.css");
      globalStage.setTitle("Game Select"); // displayed in window's title bar
      globalStage.setResizable(false);
      globalStage.setScene(globalScene);
      globalStage.show();
   }

   public static void main(String[] args) {
      launch(args);
   }
}
