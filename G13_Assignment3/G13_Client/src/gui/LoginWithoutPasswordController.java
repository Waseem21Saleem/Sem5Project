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
import common.OpenGUI;
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




public  class LoginWithoutPasswordController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	
	@FXML
	private Button btnBack = null;
	 
	@FXML
	private Button btnLogin = null;
	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtId;
	
	private String getId() {
		return txtId.getText();
	}

	
	/**
	   * This method runs the order form fx 
	   * and adds the client info into the database when "open order manager" button pressed
	   *@param event , the "open order manager" button
	   
	   */
	
	public void Login(ActionEvent event) throws Exception {
		/* Check if this id has editable order then open this window
		 */
		if (!getId().matches("[0-9]+") || getId().length()!=9)
			lblError.setText("ID must contain only 9 digits");
		else {
			User user = new User(getId());
			Message msg = new Message (Message.ActionType.USERLOGIN,user);
			ClientUI.chat.accept(msg);
			if (ChatClient.error!="")
				lblError.setText(ChatClient.error);
			else {
			
				msg = new Message (Message.ActionType.PARKNAMES,"");
				ClientUI.chat.accept(msg);
				ChatClient.openGUI.goToGUI(event, "/gui/VisitorHomePage.fxml","","Visitor home page");

			}
	        
			}
		
	}
	public void GoToLoginPage(ActionEvent event) throws Exception {
		/* Check if this id has editable order then open this window
		 */
		ChatClient.openGUI.goToGUI(event, "/gui/Login.fxml","/gui/Login.css","Worker login page");		
	}
	
	

	
	/**
	   * This method exits the client when exit button pressed
	   *
	   *@param event
	   
	   */	
	public void goBackBtn(ActionEvent event) throws Exception {
		ChatClient.openGUI.goToGUI(event, "/gui/MainFrame.fxml","/gui/MainFrame.css","Home page");		
		
	}
	
	

	
}

