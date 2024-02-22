package addGamePage;

import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.apache.commons.net.ftp.*;

public class AddGameController implements Initializable{
	
	@FXML
	TextField gameNameText;
	
	@FXML
	ImageView backgroundImage;
	
	@FXML
	Label alertToneLabel;
	
	@FXML
	Label soundtrackLabel;
	
	JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Downloads");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
		
		
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

		}
		
	}
	
	@FXML
	public void setSoundtrack() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			backgroundImage.setImage(new Image("file:///" + fileChooser.getSelectedFile().getAbsolutePath()));
			soundtrackLabel.setText(fileChooser.getSelectedFile().getName());
			soundtrackLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));

		}
		
	}
	
	@FXML
	public void setAlertTone() {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Sound Files", "mp3", "wav", "ogg", "aiff");
		
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(null);

		if(returnValue == JFileChooser.APPROVE_OPTION) {

			backgroundImage.setImage(new Image("file:///" + fileChooser.getSelectedFile().getAbsolutePath()));
			alertToneLabel.setText(fileChooser.getSelectedFile().getName());
			alertToneLabel.setEllipsisString("..." + fileChooser.getSelectedFile().getName().substring(
					fileChooser.getSelectedFile().getName().lastIndexOf("."), fileChooser.getSelectedFile().getName().length()));

		}
		
	}
	
	@FXML
	public void createGame() {
		
		
		
	}
	
}
