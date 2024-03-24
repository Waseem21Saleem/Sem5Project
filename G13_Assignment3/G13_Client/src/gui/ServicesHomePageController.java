
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.User;
import ocsf.server.ConnectionToClient;




public  class ServicesHomePageController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	public static User user=ChatClient.user;

	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtEnterID;
	
	
	private String getID() {
		return txtEnterID.getText();
	}
	
	
	public void changeRole(ActionEvent event) throws Exception {
		if (!getID().matches("[0-9]+") || getID().length()!=9)
			lblError.setText("ID must contain only 9 digits");
		else {
			User changeUser = new User(getID());
			Message msg=new Message (Message.ActionType.CHANGEROLE,changeUser);
			ClientUI.chat.accept(msg);
			if (ChatClient.error.startsWith("Updated"))
			{
				lblError.setTextFill(Color.GREEN);
			}
			else
			{
				lblError.setTextFill(Color.RED);
			}
			lblError.setText(ChatClient.error);	
		}
	}
	
	

	public void Logout(ActionEvent event) throws Exception {
		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		
	}
	
	
	

	
}
