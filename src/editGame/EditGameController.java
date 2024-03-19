package editGame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.controlsfx.dialog.FontSelectorDialog;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditGameController implements Initializable{

	public static String gameName;
	
	@FXML
    private ColorPicker fontColorPicker;
	
	@FXML
	private ImageView backgroundImage;
	
	@FXML
	private Label alertToneLabel;
	
	@FXML
	private Label soundtrackLabel;
	
	@FXML
	private Label fontLabel, winLabel, lossLabel;
	
	private Font selectedFont = null;
	
	private File alertSound, soundtrack, winSound, loseSound, backgroundImageFile;
	
	private ArrayList<File> fileList = new ArrayList<File>();
	
	private File temp = new File("tmp");
	
	private JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Downloads");
	
	private String smtpUsername = homeController.properties.getProperty("sftpUser");
	private String smtpPassword = homeController.properties.getProperty("sftpPassword");
	private String smtpRemoteHost = homeController.properties.getProperty("sftpHost");
	
	@FXML
	private TextField gameNameText;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		gameNameText.setText(gameName);
		gameNameText.setEditable(false);
		
		fileList.add(alertSound);
		fileList.add(soundtrack);
		fileList.add(backgroundImageFile);
		fileList.add(winSound);
		fileList.add(loseSound);
		
		loadFiles();
	}
	
	public static void setGameName(String name) {
        gameName = name;
    }
	
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
	
	
	private void loadFiles() {
		
		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(smtpUsername, "localhost", 22);
			session.setPassword(smtpPassword);
			session.setConfig("StrictHostKeyChecking", "no"); 
			session.connect();
			
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftp = (ChannelSftp) channel;
			
			sftp.cd("/xampp/htdocs/game files/" + gameName);
			
			System.out.println(sftp.pwd());
			
			backgroundImageFile = getFile("Background", sftp);
			alertSound = getFile("Alert", sftp);
			soundtrack = getFile("Soundtrack", sftp);
			winSound = getFile("Win", sftp);
			loseSound = getFile("Lose", sftp);
			
			if (backgroundImageFile != null) {
				backgroundImage.setImage(new Image(backgroundImageFile.getAbsolutePath()));
			}
			
			if (alertSound != null) {
				alertToneLabel.setText(alertSound.getName());
				alertToneLabel.setEllipsisString(getFileExtention(alertSound));
			}
			
			if (soundtrack != null) {
				soundtrackLabel.setText(soundtrack.getName());
				soundtrackLabel.setEllipsisString(getFileExtention(soundtrack));
			}
			
			if (winSound != null) {
				winLabel.setText(winSound.getName());
				winLabel.setEllipsisString(getFileExtention(winSound));
			}
			
			if (loseSound != null) {
				lossLabel.setText(loseSound.getName());
				lossLabel.setEllipsisString(getFileExtention(loseSound));
			}
			
			sftp.disconnect();
			channel.disconnect();
			session.disconnect();
		}
		catch(Exception e) {
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
	
	public void setBackground() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Images", ImageIO.getReaderFileSuffixes()
				);
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			backgroundImage.setImage(new Image("file:///" + fileChooser.getSelectedFile().getAbsolutePath()));
			backgroundImageFile = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
		}
		
	}
	
	@FXML
	public void setSoundtrack() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			soundtrackLabel.setText(fileChooser.getSelectedFile().getName());
			soundtrackLabel.setEllipsisString(fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));
			soundtrack = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
			
			
		}
		
	}
	
	@FXML
	public void setAlertTone() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			alertToneLabel.setText(fileChooser.getSelectedFile().getName());
			alertToneLabel.setEllipsisString(getFileExtention(fileChooser));
			alertSound = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
			
		}
		
	}
	
	@FXML
	public void setFont() {
		
		FontSelectorDialog fs = new FontSelectorDialog(fontLabel.getFont());
		fs.setTitle("Select Font");
		Optional<Font> response = fs.showAndWait();
		
		try {
			selectedFont = response.get();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!temp.exists() && response.get().getFamily() != "" && response.get() != null) {
			try {
				temp.createNewFile();
				
				FileWriter fw = new FileWriter("tmp");
				
				fw.write(String.format("%s%n%f", response.get().getFamily().toString(), response.get().getSize()));
				
				System.out.printf("%s%n%f%n", response.get().getFamily(), response.get().getSize());
				
				fw.close();
				
				String styleString = String.format("-fx-font-family: \"%s\"; -fx-font-size: %fpx;", response.get().getFamily(), response.get().getSize());
				
				if(fontColorPicker.getValue() != Color.WHITE) {
					styleString.concat(String.format("-fx-text-fill: %s;", toHexString(fontColorPicker.getValue())));
				}
				
				fontLabel.setStyle(styleString);
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			if(temp.delete()) {
				try {
					temp.createNewFile();
					
					FileWriter fw = new FileWriter("tmp");
					
					fw.write(String.format("%s%n%f", response.get().getFamily().toString(), response.get().getSize()));
					
					System.out.printf("%s%n%f%n", response.get().getFamily(), response.get().getSize());
					
					fw.close();
					
					String styleString = String.format("-fx-font-family: \"%s\"; -fx-font-size: %fpx;", response.get().getFamily(), response.get().getSize());
					
					if(fontColorPicker.getValue() != Color.WHITE) {
						System.out.println(toHexString(fontColorPicker.getValue()));
						styleString = styleString.concat(String.format("-fx-text-fill: %s;", toHexString(fontColorPicker.getValue())));
					}
					
					fontLabel.setStyle(styleString);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@FXML
	public void setWinSound() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			winLabel.setText(fileChooser.getSelectedFile().getName());
			winLabel.setEllipsisString(getFileExtention(fileChooser));
			winSound = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
			
		}
		
	}
	
	@FXML
	public void setLossSound() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			lossLabel.setText(fileChooser.getSelectedFile().getName());
			lossLabel.setEllipsisString(getFileExtention(fileChooser));
			loseSound = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
			
		}
		
	}
	
	private void uploadFile(File f, ChannelSftp sftp, String gameName, String fType){
		// TODO Auto-generated method stub
		try {
			SftpATTRS attrs = null;
			
			String fileSuffix = getFileExtention(f);
			
			if (fileExists(f, sftp, gameName, fType)) {
				attrs = sftp.stat(String.format("%s%s%s", gameName, fType, fileSuffix));
			}
			
			if(attrs == null) {
				sftp.put(alertSound.getAbsolutePath(), String.format("%s%s%s", gameName, fType,
						fileSuffix));
			}
			else {
				sftp.rm(String.format("%s%s%s", gameName, fType,
						fileSuffix));
				
				sftp.put(alertSound.getAbsolutePath(), String.format("%s%s%s", gameName,fType, 
						fileSuffix));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getFileExtention(File f) {
		return f.getName().substring(f.getName().lastIndexOf("."), f.getName().length());
	}
	
	private String getFileExtention(JFileChooser fc) {
		
		return fc.getSelectedFile().getName().substring(
				fc.getSelectedFile().getName().lastIndexOf("."), fc.getSelectedFile().getName().length());
		
		
	}

	private static String toHexString(Color color) {
		  int r = ((int) Math.round(color.getRed()     * 255)) << 24;
		  int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
		  int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
		  int a = ((int) Math.round(color.getOpacity() * 255));
		  return String.format("#%08X", (r + g + b + a));
		}

	private File getFile(String type, ChannelSftp sftp) {
	
		SftpATTRS attrs = null;
		
		String noSpaceGameName = "";
		String fileSuffix = "";
		
		String[] audioFileExts = {"mp3", "wav", "ogg", "aiff"};
		
		for (char ch : gameName.toCharArray()) {
			if (!Character.isWhitespace(ch)) {
				if(Character.isUpperCase(ch) && ch == gameName.charAt(0)) {
					ch = Character.toLowerCase(ch);
				}
				noSpaceGameName += ch;
			}
		}
		
		String fileExts[];
		
        if(type.equals("Background")) {
            fileExts = new String[] {"png", "jpg", "jpeg"};
        }
        else {
            fileExts = audioFileExts;
        }
		
		for(String s : fileExts) {
			String fileLocation = null;
		
			try {
				fileLocation = sftp.pwd() + "/" + noSpaceGameName + type + "." + s;
				//System.out.println(fileLocation);
				attrs = sftp.stat(fileLocation);
				fileSuffix = "." + s;
				break;
			}
			catch (Exception e) {
				//System.out.println(fileLocation + " not found!");
			}
		}	
		
		if(attrs != null) {
			try {
				Path tempPath = Files.createTempDirectory(noSpaceGameName + "Temp");
				sftp.get(noSpaceGameName + type + fileSuffix, tempPath.toAbsolutePath().toString());
				//System.out.println(tempPath.getFileName());
				return tempPath.toFile();
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		else {
			System.out.println(type + " not found!");
		}
		
		return null;
		
	}

	private boolean fileExists(File f, ChannelSftp sftp, String gameName, String fType) {

		SftpATTRS attrs = null;

		String fileSuffix = getFileExtention(f);

		try {
			attrs = sftp.stat(sftp.pwd() + String.format("%s%s%s", gameName, fType, fileSuffix));
		} catch (SftpException e) {
			return false;
		}

		return attrs != null;
	}
	
	private boolean deleteFile(File f, ChannelSftp sftp, String gameName, String fType) {
		
		if (fileExists(f, sftp, gameName, fType)) {
			try {
				sftp.rm(String.format("%s%s%s", gameName, fType, getFileExtention(f)));
				return true;
			} catch (SftpException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
		
	}
	
	@FXML
	public void saveGame() {
		String[] fileTypes = {"Alert", "Soundtrack", "Background", "Win", "Lose"};
		
		boolean cont = true;
		
		for (File f : fileList) {
			if (f == null) {
				
				cont = false;
				
				ButtonType yes = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
				
				Alert alert = new Alert(AlertType.CONFIRMATION, String.format("%s is null!", fileTypes[fileList.indexOf(f)]), yes);
				
				alert.setHeaderText(String.format("Delete %s?", gameName));
				
				alert.showAndWait();
				
				break;
			}
		}
		
		if(cont) {
			
			
			String databaseURL = homeController.properties.getProperty("dbURL");
			String databaseUserName = homeController.properties.getProperty("dbUserName");
			String databasePassword = homeController.properties.getProperty("dbPassword");

			try {
				Connection con = DriverManager.getConnection(databaseURL, databaseUserName, databasePassword);

				Statement statement = con.createStatement();

				String SQL = String.format("SELECT * FROM leaderboard.games WHERE Name = \"%s\"", gameName);

				ResultSet resultSet = statement.executeQuery(SQL);

				System.out.println(String.format("%s", gameName));
				
				if(!resultSet.next()) {
					SQL = String.format("call leaderboard.createGame('%s');", gameName);
					//statement.execute(SQL);
				}
				
				resultSet.close();
				statement.close();
				con.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			try {
				JSch jsch = new JSch();
				Session session = jsch.getSession(smtpUsername, "aperriz.chickenkiller.com", 22);
				session.setPassword(smtpPassword);
				session.setConfig("StrictHostKeyChecking", "no"); 
				session.connect();
				
				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;
				
				//System.out.println(sftp.pwd() + "/xampp/htdocs/game files");
				
				//alertSound, soundtrack, winSound, loseSound, backgroundImageFile
				
				for(File f : fileList) {
					
					if(f != null) {
						
						deleteFile(f, sftp, gameName, fileTypes[fileList.indexOf(f)]);
						
						
					}
					
				}
				
				sftp.disconnect();
				channel.disconnect();
				session.disconnect();
				
				back();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
