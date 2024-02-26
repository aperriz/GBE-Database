package editGame;

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.Optional;
import java.util.ResourceBundle;

import common.Base;
import homePage.homeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class EditGameController implements Initializable{

	public static String gameName;
	
	@FXML
	public void back(){
		
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/common/home.fxml"));
			
			Base.globalScene = new Scene(root);
			Base.globalScene.getStylesheets().add("/common/style.css");
			Base.globalStage.setTitle("Game Select");
			Base.globalStage.setResizable(false);
			Base.globalStage.setScene(Base.globalScene);
			Base.globalStage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@FXML
    void delGame(ActionEvent event) {
		
		ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
		
		Alert alert = new Alert(AlertType.CONFIRMATION, String.format("Are you sure you want to delete %s?", gameName), yes, no);
		
		alert.setHeaderText(String.format("Delete %s?", gameName));
		
		Optional<ButtonType> resp = alert.showAndWait();
		
		if(resp.get() == yes) {
			
			String databaseURL = homeController.properties.getProperty("dbURL");
			String databaseUserName = homeController.properties.getProperty("dbUserName");
			String databasePassword = homeController.properties.getProperty("dbPassword");

			try {
				Connection con = DriverManager.getConnection(databaseURL, databaseUserName, databasePassword);

				Statement statement = con.createStatement();

				String SQL = String.format("DELETE FROM leaderboard.games WHERE Name = \"%s\"", gameName);

				ResultSet resultSet = statement.executeQuery(SQL);

				if(!resultSet.next()) {
					System.out.printf("%s deleted!%n", gameName);
				}
				
				resultSet.close();
				statement.close();
				con.close();
				
				back();
				
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			
		}
		
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
