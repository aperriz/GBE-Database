package homePage;
import common.Base;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class homeController implements Initializable{

	Connection con = null;
	Statement statement = null;
	ResultSet resultSet = null;
	
	String selectedGame = null;
	
	@FXML
	private Button addGameButton;
	
	private final ObservableList<GameCell> games = 
		      FXCollections.observableArrayList();

    @FXML
    private ListView<GameCell> gameView;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		addGameButton.setOnAction(e -> addGame());
		addGameButton.setStyle("-fx-font-size: 18px");

		ArrayList<Node> allNodes = new ArrayList<Node>();

		allNodes.add(gameView);

		gameView.setId("mainView");
		
		try {

			InputStream input = getClass().getResourceAsStream("/common/config.properties");

			Properties properties = new Properties();

			properties.load(input);

			String databaseURL = properties.getProperty("dbURL");
			String databaseUserName = properties.getProperty("dbUserName");
			String databasePassword = properties.getProperty("dbPassword");

			con = DriverManager.getConnection(databaseURL, databaseUserName, databasePassword);

			statement = con.createStatement();

			String SQL = "Select * FROM leaderboard.games";

			resultSet = statement.executeQuery(SQL);

			String s;
			
			while(resultSet.next()) {
				s = resultSet.getString(1);
				
				games.add(new GameCell(s));
			}
			
			gameView.setItems(games);
			


		}
		catch(IOException | SQLException e) {
			e.printStackTrace();
		}
		finally {

			try {
				if(resultSet != null) {
					resultSet.close();
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}

			try {
				if(statement != null) {
					statement.close();
				}

			}catch(SQLException e) {
				e.printStackTrace();
			}

			try {

				if(con != null) {

					con.close();

				}

			}catch(SQLException e) {
				e.printStackTrace();
			}

		}

	}
	
	public URL getURL(String fileName) {
		URL fileURL = null;
	
		String noSpaceFileName = "";
		
		for(int i = 0; i < fileName.length(); i++) {
			char ch = fileName.charAt(i);
			
			if(!Character.isWhitespace(ch)) {
				noSpaceFileName += ch;
			}
		}
		
		try {
			fileURL = new URL("http://leaderboard/game%20files/" + fileName + "/add/" + noSpaceFileName + ".png");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return fileURL;
	
	}
	
	public static void selectGame(String gameName) {
		
		System.out.println(gameName);
		
	}
	
	public void addGame() {
		
		Parent root = null;
		
		try {
			root = FXMLLoader.load(getClass().getResource("/common/addGame.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Base.globalStage.close();
		
		Base.globalScene = new Scene(root);
		Base.globalScene.getStylesheets().add("/addGamePage/AddGame.css");
		Base.globalStage.setTitle("Add Game"); // displayed in window's title bar
		Base.globalStage.setScene(Base.globalScene);
		Base.globalStage.show();
		Base.globalStage.centerOnScreen();
		
	}
	
}
