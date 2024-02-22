package addGamePage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.controlsfx.dialog.FontSelectorDialog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

public class AddGameController implements Initializable{
	
	@FXML
	TextField gameNameText;
	
	@FXML
	ImageView backgroundImage;
	
	@FXML
	Label alertToneLabel;
	
	@FXML
	Label soundtrackLabel;
	
	@FXML
	Label fontLabel;
	
	Font selectedFont;
	
	File temp = new File("temp.txt");
	
	JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home") + "\\Downloads");
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(temp.exists()) {
			try {
				FileReader fr = new FileReader("temp.txt");
				
				selectedFont = new Font(fr.read());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	public void setFont() {
		FontSelectorDialog fs = new FontSelectorDialog(fontLabel.getFont());
		
		Optional<Font> response = fs.showAndWait();
		
		if(!temp.exists() && response.get().getFamily() != "" && response.get() != null) {
			try {
				temp.createNewFile();
				
				FileWriter fw = new FileWriter("temp.txt");
				
				fw.write(response.get().toString());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fs.setTitle("Select Font");
		fs.showAndWait().ifPresent(alertToneLabel::setFont);
		
		
	}
	
	@FXML
	public void createGame() {
		
		
		
	}
	
}
