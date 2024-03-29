
package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import logic.Park;
import logic.User;
import ocsf.server.ConnectionToClient;



/**
 * This class controls the functionality of the Park Manager Change Capacity GUI.
 * It allows park managers to change the capacity settings for their parks.
 * <p>Author: Julian Abdo</p>
 */
public  class ParkManagerChangeCapacityController  implements Initializable {
	/** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static User object representing the user. */
    public static User user;

    /** Static variable representing the item index. */
    private static int itemIndex = 3;
	
 
	@FXML
	private Label lblError,lblParkName;
	
	@FXML
	private TextField txtReservedCapacity,txtMaxStay,txtTotalCapacity;
	
    /**
     * Gets the reserved capacity entered by the user.
     *
     * @return The reserved capacity.
     */
	private String getReservedCapacity() {
		return txtReservedCapacity.getText();
	}
	
    /**
     * Gets the total capacity entered by the user.
     *
     * @return The total capacity.
     */
	private String getTotalCapacity() {
		return txtTotalCapacity.getText();
	}
	
    /**
     * Gets the maximum stay time entered by the user.
     *
     * @return The maximum stay time.
     */
	private String getMaxTime() {
		return txtMaxStay.getText();
	}

    /**
     * Handles the action when the "Go Back" button is pressed.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during navigation.
     */
	public void goBack(ActionEvent event) throws Exception {
		ChatClient.openGUI.goToGUI(event, "/gui/ParkManagerHomePage.fxml","","Park Manager home page");
	}
	

    /**
     * Handles the action when the "Confirm" button is pressed.
     * Validates the input and sends a change request to the department manager.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the operation.
     */
	public void Confirm(ActionEvent event) throws Exception {
		if (!getReservedCapacity().matches("[0-9]+") || getReservedCapacity().length()>3 )
			lblError.setText("Reserved capacity must contain only digits and max is 999.\n");
		else if (!getTotalCapacity().matches("[0-9]+") || getTotalCapacity().length()>3 )
			lblError.setText("Total capacity must contain only digits and max is 999.\n");
		else if (Integer.parseInt(getTotalCapacity())<Integer.parseInt(getReservedCapacity()))
			lblError.setText("Total capacity must be equal or higher than reserved capacity.\n");
		else if (!getMaxTime().matches("[0-9]+") || getMaxTime().length()!=1 || getMaxTime().equals("0"))
			lblError.setText("MaxTime must contain only digits in range 1-9 hours.\n");
		else {
			Park park = new Park (lblParkName.getText(),getReservedCapacity(),getTotalCapacity(),getMaxTime());
			Message msg = new Message (Message.ActionType.NEWREQUEST,park);
			ClientUI.chat.accept(msg);
			lblError.setTextFill(Color.GREEN);
			lblError.setText("Change request sent to Department manager");
		}
	}
	
    /**
     * Initializes the controller with the current user and park information.
     *
     * @param arg0 The URL location of the FXML file.
     * @param arg1 The resource bundle.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;;
		Park park=ChatClient.park;
		lblParkName.setText(user.getParkName());
		txtMaxStay.setText(park.getMaxStay());
		txtReservedCapacity.setText(park.getReservedCapacity());
		txtTotalCapacity.setText(park.getTotalCapacity());
		
		
	} 
	
	
	

	
}
