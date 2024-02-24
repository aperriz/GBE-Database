package editGame;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import common.Base;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EditGameController implements Initializable{

	public static String gameName;
	
	@FXML
	public void back(ActionEvent event) throws IOException {
		
		Parent root = 
		         FXMLLoader.load(getClass().getResource("/common/home.fxml"));
		
		
		//Close current window and open home page
		Base.globalStage.close();
		
		Base.globalScene = new Scene(root);
		Base.globalScene.getStylesheets().add("/common/style.css");
		Base.globalStage.setTitle("Game Select");
		Base.globalStage.setResizable(false);
		Base.globalStage.setScene(Base.globalScene);
		Base.globalStage.show();
		
	}
	
	@FXML
    void delGame(ActionEvent event) {
		
		System.out.println("delete");
		
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
