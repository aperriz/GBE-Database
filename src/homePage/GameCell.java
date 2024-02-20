package homePage;

import javafx.scene.text.Font;

import javax.swing.GroupLayout.Alignment;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

public class GameCell extends ListCell<String> {
	
	HBox hbox = new HBox(150.0);
	Label nameLabel = new Label();
	Button playButton = new Button("Play");
	Button editButton = new Button("Edit");
	String name;
	
	GameCell(String s){
		
		super();
		
		setStyle("-fx-background-color: transparent;");
		hbox.setStyle("-fx-background-color: transparent;");
		
		name = s;		
		nameLabel.setTextAlignment(TextAlignment.CENTER);
		playButton.setTextAlignment(TextAlignment.CENTER);
		editButton.setTextAlignment(TextAlignment.CENTER);
		nameLabel.setPrefWidth(USE_PREF_SIZE);
		
		playButton.setOnAction(e -> homeController.selectGame(name));
		
		nameLabel.setMinSize(100, BASELINE_OFFSET_SAME_AS_HEIGHT);
		playButton.setMinSize(USE_PREF_SIZE, BASELINE_OFFSET_SAME_AS_HEIGHT);
		editButton.setMinSize(USE_PREF_SIZE, BASELINE_OFFSET_SAME_AS_HEIGHT);
		
		hbox.getChildren().addAll(nameLabel, playButton, editButton);
		
		setPrefWidth(USE_PREF_SIZE);
		
		updateItem(s, false);
		
	}
	
	@Override
	public void updateItem(String name, boolean empty) {
		
		super.updateItem(name, empty);
		
		if(empty || name == null) {
		
			setGraphic(null);
			
		}
		else {
			
			playButton.setText("Play");
			editButton.setText("Edit");
			
			nameLabel.setFont(Font.font("Impact"));
			
			nameLabel.setText(name);
			setGraphic(hbox);
		}
	}
	
}
