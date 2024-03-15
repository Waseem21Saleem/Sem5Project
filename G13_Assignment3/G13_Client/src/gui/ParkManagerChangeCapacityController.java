
package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import common.ChatIF;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;



public  class ParkManagerChangeCapacityController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	

	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtMaxAmount;
	
	@FXML
	private ComboBox cmbChooseParkName;
	
	private String getMaxAmount() {
		return txtMaxAmount.getText();
	}

	
	public void goBack(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ParkManagerHomePage.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/gui/ParkManagerHomePage.css").toExternalForm());
		primaryStage.setTitle("Park Manager home page");

		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	

	public void Confirm(ActionEvent event) throws Exception {
		/*FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/Login.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/Login.css").toExternalForm());
		primaryStage.setTitle("Login page");

		primaryStage.setScene(scene);		
		primaryStage.show();*/
		
	}
	
	
	

	
}