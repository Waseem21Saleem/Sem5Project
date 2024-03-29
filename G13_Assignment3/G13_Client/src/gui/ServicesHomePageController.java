
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



/**
 * This class controls the functionality of the Services Home Page GUI.
 * It allows Services to change roles from visitor to guide, and log out.
 * <p>Author: Mohammed Khateeb</p>
 */
public  class ServicesHomePageController   {
	/** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static integer representing an item index. */
    private static int itemIndex = 3;

    /** Static User object representing the user. */
    public static User user = ChatClient.user;

	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtEnterID;
	
	   /**
     * Retrieves the entered ID from the text field.
     *
     * @return The entered ID.
     */
	private String getID() {
		return txtEnterID.getText();
	}
	 
    /**
     * Changes the role of a user based on the entered ID.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during role change.
     */
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
	
	
	  /**
     * Logs out the user and redirects to the login page.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during logout.
     */
	public void Logout(ActionEvent event) throws Exception {
		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		
	}
	
	
	

	
}
