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
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

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

	private File temp = new File("font.txt");
	private Color selectedColor = Color.WHITE;
	private Font selectedFont = null;
	private String[] fileTypes = {"Alert", "Win", "Loss", "Soundtrack", "Background", "Font"};

	private File alertSound, soundtrack, winSound, loseSound, backgroundImageFile;

	private ArrayList<File> fileList = new ArrayList<File>();

	//private File temp = new File("tmp");

	private JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Downloads");

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		fileList.add(alertSound);
		fileList.add(winSound);
		fileList.add(loseSound);
		fileList.add(soundtrack);
		fileList.add(backgroundImageFile);
		fileList.add(temp);

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

		if(response.get().getFamily() != "" && response.get() != null) {

			selectedFont = new Font(response.get().getFamily(), response.get().getSize());
			selectedColor = fontColorPicker.getValue();
			
			try {
				FileWriter fw = new FileWriter(temp);
				fw.write(String.format("%s\n%s\n%f", response.get().getFamily(), toHexString(selectedColor) ,response.get().getSize()));
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String styleString = String.format("-fx-font-family: \"%s\"; -fx-font-size: %fpx;", response.get().getFamily(), response.get().getSize());

			if(fontColorPicker.getValue() != Color.WHITE) {
				styleString.concat(String.format("-fx-text-fill: %s;", toHexString(fontColorPicker.getValue())));
			}

			fontLabel.setStyle(styleString);
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

	@FXML
	public void createGame() {

		String gameName = gameNameText.getText();

		boolean cont = true;

		for (File f : fileList) {
			if (f == null) {
				cont = false;
				break;
			}
		}

		if(cont) {

			try {
				Connection con = DriverManager.getConnection(Base.databaseURL, Base.databaseUserName, Base.databasePassword);

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
				Session session = jsch.getSession(Base.sftpUsername, "localhost", 22);
				session.setPassword(Base.sftpPassword);
				session.setConfig("StrictHostKeyChecking", "no"); 
				session.connect();

				Channel channel = session.openChannel("sftp");
				channel.connect();
				ChannelSftp sftp = (ChannelSftp) channel;

				//System.out.println(sftp.pwd() + "/xampp/htdocs/game files");

				createDirectory(sftp, gameName);

				//alertSound, soundtrack, winSound, loseSound, backgroundImageFile

				for(File f: fileList) {
					uploadFile(f, sftp, gameName, fileTypes[fileList.indexOf(f)]);
				}

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

		selectedFont = null;

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

	private void uploadFile(File f, ChannelSftp sftp, String gameName, String fType){
		// TODO Auto-generated method stub
		try {
			SftpATTRS attrs = null;

			String fileSuffix = getFileExtention(f);

			String noSpaceGameName = "";

			for(char ch : gameName.toCharArray()) {
				if (!Character.isWhitespace(ch)) {
					if(ch == gameName.toCharArray()[0]) {
						ch = Character.toLowerCase(ch);
					}
					noSpaceGameName += ch;
				}
			}

			try {
				attrs = sftp.stat(sftp.pwd() + String.format("%s%s%s", noSpaceGameName, fType,
						fileSuffix));
			}catch(Exception e) {
			}

			if(attrs == null) {
				sftp.put(f.getAbsolutePath(), String.format("%s%s%s", noSpaceGameName, fType,
						fileSuffix));
			}
			else {
				sftp.rm(String.format("%s%s%s", noSpaceGameName, fType,
						fileSuffix));

				sftp.put(f.getAbsolutePath(), String.format("%s%s%s", noSpaceGameName,fType, 
						fileSuffix));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean createDirectory(ChannelSftp sftp, String gameName) {
		try {
			sftp.cd("/xampp/htdocs/game files/");
			System.out.println(sftp.pwd());

			SftpATTRS attrs=null;

			try {
				attrs = sftp.stat(sftp.pwd() + gameName);
				return false;
			} catch (Exception e) {
				System.out.println(sftp.pwd() + gameName + " not found");
			}

			if (attrs == null || !attrs.isDir()) {
				sftp.mkdir(gameName);

				try {
					attrs = sftp.stat(sftp.pwd() + gameName);
					System.out.println("gameName directory created successfully");	
					sftp.cd(gameName);
					System.out.println(sftp.pwd());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			else {
				System.out.println(attrs.isDir());
			}

			sftp.cd(gameName);

			System.out.println(sftp.pwd());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private String getFileExtention(File f) {
		return f.getName().substring(f.getName().lastIndexOf("."), f.getName().length());
	}

	private String getFileExtention(JFileChooser fc) {

		return fc.getSelectedFile().getName().substring(
				fc.getSelectedFile().getName().lastIndexOf("."), fc.getSelectedFile().getName().length());

	}
}
