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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Message;
import logic.User;
import ocsf.server.ConnectionToClient;



public  class LoginController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	
	@FXML
	private Button btnBack = null;
	
	@FXML
	private Button btnLogin = null;
	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtId,txtPassword;
	
	private String getId() {
		return txtId.getText();
	}
	private String getPassword() {
		return txtPassword.getText();
	}
	
	/**
	   * This method runs the order form fx 
	   * and adds the client info into the database when "open order manager" button pressed
	   *@param event , the "open order manager" button
	   
	   */
	public void Login(ActionEvent event) throws Exception {
		/* Send a message to server to check username and password then check Role and open next window 
		 * according to the role
		 */
		String id = getId();
		String password = getPassword();
		if (!id.matches("[0-9]+") || id.length()!=9)
			lblError.setText("ID must contain only 9 digits");
		else if (password!="" && password!=null )
			lblError.setText("Password can not be empty");
		else {
			User user = new User(id,password);
			Message msg = new Message (Message.ActionType.WORKERLOGIN,user);
			ClientUI.chat.accept(msg);
			String openPage="",pageTitle="";
			if (id.equals("1")) {
				openPage="/gui/VisitorHomePage.fxml";
				pageTitle="Visitor home page"; }
			else if (id.equals("2")) {
				openPage="/gui/ParkManagerHomePage.fxml";
				pageTitle="Park Manager home page";
				
			}
			else if (id.equals("3")) {
				openPage="/gui/DepartmentManagerHomePage.fxml";
				pageTitle="Department Manager home page";
				
			}
			else if (id.equals("4")) {
				openPage="/gui/ServicesHomePage.fxml";
				pageTitle="Services home page";
				
			}
	
			
			FXMLLoader loader = new FXMLLoader();
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource(openPage).openStream());		
			
		
			Scene scene = new Scene(root);			
			//scene.getStylesheets().add(getClass().getResource(openPage).toExternalForm());
			primaryStage.setTitle(pageTitle);
	
			primaryStage.setScene(scene);		
			primaryStage.show();
	
			//ClientUI.chat.accept("refresh");
	
	        
	
			}
	}

	
	/**
	   * This method exits the client when exit button pressed
	   *
	   *@param event
	   
	   */	
	public void goBackBtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/LoginWithoutPassword.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/LoginWithoutPassword.css").toExternalForm());
		primaryStage.setTitle("Visitor Login page");

		primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	

	
}
