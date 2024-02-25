package editGame;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import common.Base;
import homePage.homeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class EditGameController implements Initializable{

	public static String gameName;
	
	@FXML
	public void back(){
		
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/common/home.fxml"));
			
			//Close current window and open home page
			Base.globalStage.close();
			
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
