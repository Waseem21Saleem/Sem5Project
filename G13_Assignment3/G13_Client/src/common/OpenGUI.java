package common;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Utility class for opening GUI windows.
 */
public class OpenGUI {

	  /**
     * Opens a new GUI window based on the provided FXML file, CSS file, and window title.
     *
     * @param event The ActionEvent that triggered the GUI opening.
     * @param fxml The path to the FXML file for the new GUI window.
     * @param css The path to the CSS file for styling the new GUI window.
     * @param title The title of the new GUI window.
     */
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

