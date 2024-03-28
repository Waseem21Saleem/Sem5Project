package common;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class OpenGUI {

	public void goToGUI(ActionEvent event,String fxml,String css, String title) {
		FXMLLoader loader = new FXMLLoader();
		Stage primaryStage = new Stage();
		Pane root;
		try {
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			root = loader.load(getClass().getResource(fxml).openStream());
			Scene scene = new Scene(root);	
			if (css!="")
				scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
			primaryStage.setTitle(title);
	
			primaryStage.setScene(scene);		
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	
}

