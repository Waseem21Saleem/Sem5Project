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

/**
 * This class controls the login process in the GUI.
 * It handles actions such as logging in and navigating back.
 * <p>Author: [Ahmad Abo Jabal]</p>
 */

public  class LoginController   {
	/** Static ClientController for chat functionality */
	public static ClientController chat;
	/** The index of the login item */
	private static int itemIndex = 3;
	
	@FXML
	private Button btnBack = null;
	 
	@FXML
	private Button btnLogin = null;
	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtId,txtPassword;
	
	/**
     * Retrieves the entered ID from the text field.
     * @return The entered ID.
     */
	private String getId() {
		return txtId.getText();
	}
	
	 /**
     * Retrieves the entered password from the text field.
     * @return The entered password.
     */
	private String getPassword() {
		return txtPassword.getText();
	}
	
	/**
     * Logs in the user.
     * @param event The ActionEvent triggered by clicking the login button.
     * @throws Exception If an error occurs during the login process.
     */
	public void Login(ActionEvent event) throws Exception {
		/* Send a message to server to check username and password then check Role and open next window 
		 * according to the role
		 */
		String id = getId();
		String password = getPassword();
		// Validating ID and password
		if ((!id.matches("[0-9]+") || id.length()!=9)||(password=="" || password==null ))
			if (!id.matches("[0-9]+") || id.length()!=9)
			lblError.setText("ID must contain only 9 digits");
			else
			lblError.setText("Password can not be empty");
		else {
			// Creating user object
			User user = new User(id,password);
			// Sending login request to the server
			Message msg = new Message (Message.ActionType.WORKERLOGIN,user);
			// Handling login response
			ClientUI.chat.accept(msg);
			if (ChatClient.error!="")
				lblError.setText(ChatClient.error);
			else {
				String openPage="",pageTitle="";
                // Determining which page to open based on user's permission

				switch (ChatClient.user.getUserPermission()){
				case "WORKER":
					openPage="/gui/WorkerHomePage.fxml";
					pageTitle="Worker home page";
					break;
				case "PARK MANAGER":
					openPage="/gui/ParkManagerHomePage.fxml";
					pageTitle="Park Manager home page";
					break;
				case "DEPARTMENT MANAGER":
                    // Sending request for park names for department manager
					msg = new Message (Message.ActionType.PARKNAMES,user);
					ClientUI.chat.accept(msg);
					openPage="/gui/DepartmentManagerHomePage.fxml";
					pageTitle="Department Manager home page";
					break;
					
				case "SERVICE":
					openPage="/gui/ServicesHomePage.fxml";
					pageTitle="Services home page";
					break;
				}
					 
                // Opening the appropriate page
				ChatClient.openGUI.goToGUI(event, openPage,"",pageTitle);

			
			}
			
			
	
	        
	
			}
	}

	
	/**
     * Navigates back to the visitor login page.
     * @param event The ActionEvent triggered by clicking the back button.
     * @throws Exception If an error occurs during navigation.
     */
	public void goBackBtn(ActionEvent event) throws Exception {
		
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor Login page");
		
	}
	

	
}