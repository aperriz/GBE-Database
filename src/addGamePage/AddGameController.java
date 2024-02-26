package addGamePage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import common.Base;
import homePage.homeController;

public class AddGameController implements Initializable{
	
	@FXML
    private ColorPicker fontColorPicker;
	
	@FXML
	private TextField gameNameText;
	
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		fileList.add(alertSound);
		fileList.add(winSound);
		fileList.add(loseSound);
		fileList.add(soundtrack);
		fileList.add(backgroundImageFile);
		
		fontColorPicker.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				String styleString = String.format("-fx-text-fill: %s;", toHexString(fontColorPicker.getValue()));
				
				if(selectedFont != null) {
					styleString = styleString.concat(String.format("-fx-font-family: \"%s\"; -fx-font-size: %fpx;", 
							selectedFont.getFamily(), selectedFont.getSize()));
				}
				
				fontLabel.setStyle(styleString);
			}
			
		});
		
	}

	@FXML
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
			soundtrackLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
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
			alertToneLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));
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
			winLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));
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
			lossLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));
			loseSound = fileChooser.getSelectedFile();
			
			fileChooser.setSelectedFile(null);
			
		}
		
	}
	
	@FXML
	public void createGame() {
		
		boolean cont = true;
		
		for (File f : fileList) {
			if (f == null) {
				//cont = false;
			}
		}
		
		if(cont) {
			

			
			try {
				JSch jsch = new JSch();
				Session session = jsch.getSession(smtpUsername, "aperriz.chickenkiller.com", 22);
				session.setPassword(smtpPassword);
				session.setConfig("StrictHostKeyChecking", "no"); 
				session.connect();
				
				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;
				
				System.out.println(sftp.pwd() + "/xampp/htdocs/");
				
				sftp.cd("/xampp/htdocs/");
				
				System.out.println(sftp.pwd());
				
				sftp.put(alertSound.getAbsolutePath(), String.format("%s%s", gameNameText.getText(), alertSound.getName().substring(alertSound.getName().lastIndexOf("."), alertSound.getName().length())));
				
				sftp.disconnect();
				channel.disconnect();
				session.disconnect();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		back();
		
	}
	
	public void resetGame() {
		try {
			//Reset Files
			if(temp.exists()) {
				if(!temp.delete()) {
					FileWriter fw = new FileWriter(temp);
					fw.write("");
					fw.close();
					temp.delete();
				}
			}
			
			selectedFont = null;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		for(File f: fileList) {
			f = null;
		}
	}
	
	@FXML
	public void back() {
		
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/common/home.fxml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		resetGame();
		
		Base.globalScene = new Scene(root);
		Base.globalScene.getStylesheets().add("/common/style.css");
		Base.globalStage.setTitle("Game Select");
		Base.globalStage.setResizable(false);
		Base.globalStage.setScene(Base.globalScene);
		Base.globalStage.show();
		
	}
		
	private static String toHexString(Color color) {
		  int r = ((int) Math.round(color.getRed()     * 255)) << 24;
		  int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
		  int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
		  int a = ((int) Math.round(color.getOpacity() * 255));
		  return String.format("#%08X", (r + g + b + a));
		}
	
}
