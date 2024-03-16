package gui;


import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Order;

public class DepartmentManagerApprovalCC   {
	public static ClientController chat;
	@FXML
	private Button btnReject,btnAccept,btnBack;
	
	@FXML
	private TableView listApprove;
	/**
	   * This method loads the current order into the fx
	   *
	   *@param order
  
	   */	
	public void Accept(ActionEvent event) throws Exception {
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
	public void Rejected (ActionEvent event) throws Exception {
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
	
public void goBackBtn(ActionEvent event) throws Exception {
	
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/DepartmentManagerApproval.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/DepartmentManagerApproval.css").toExternalForm());
}
}

