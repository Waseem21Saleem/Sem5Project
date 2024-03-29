package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import common.ChatIF;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.User;
import ocsf.server.ConnectionToClient;

/**
 * WorkerHomePageController is a controller class responsible for controlling the worker's home page UI.
 * It implements the Initializable interface to initialize the controller's UI components.
 * This class also contains a static ClientController for chat functionality and a static User object representing the worker.
 * */

public  class WorkerHomePageController  implements Initializable    {
	/** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static integer representing the item index. */
    private static int itemIndex = 3;

    /** Static User object representing the worker. */
    public static User user;


 
	@FXML
	private Label lblParkName,lblViewCurrCapacity,lblUnplannedVisitors,lblApproveExit;
	
	@FXML
	private Button btnCheckCapacity,btnUnplanned;
	
	@FXML
	private TextField txtEnterID,txtAmount,txtOrderNumber;
	
    /**
     * Retrieves the entered visitor ID.
     *
     * @return The entered visitor ID.
     */
	private String getID() {
		return txtEnterID.getText();
	}
	

    /**
     * Retrieves the entered amount of visitors.
     *
     * @return The entered amount of visitors.
     */
	private String getAmount() {
		return txtAmount.getText();
	}
	
    /**
     * Retrieves the entered order number.
     *
     * @return The entered order number.
     */
	private String getOrderNum() {
		return txtOrderNumber.getText();
	}
	
	
    /**
     * Initializes the Worker Home Page GUI.
     *
     * @param arg0 The URL location to resolve relative paths.
     * @param arg1 The resource bundle.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;
		lblParkName.setText(user.getParkName());
		
	}
	
	   /**
     * Logs out the user from the system.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the logout process.
     */
	public void Logout(ActionEvent event) throws Exception {
		

		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		
	}
	
    /**
     * Approves the exit of visitors with the provided order number.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the approval process.
     */
	public void approveExit(ActionEvent event) throws Exception  {
		if (getOrderNum().isEmpty() ||!getOrderNum().matches("[0-9]+") || getOrderNum().length()!=8)
			lblApproveExit.setText("Order number must contain only 8 digits");

		else {
		Message msg = new Message (Message.ActionType.APPROVEEXIT,getOrderNum());
		ClientUI.chat.accept(msg);
		lblApproveExit.setText(ChatClient.error);
		if (ChatClient.error.contains("approved"))
			lblApproveExit.setTextFill(Color.GREEN);
		}
		getAvailablePlaces(event);
		

	}
	
    /**
     * Retrieves the bill for the order with the provided order number.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the retrieval process.
     */
	public void getBill(ActionEvent event) throws Exception  {
		Message msg = new Message (Message.ActionType.GETINVOICE,getOrderNum());
		ClientUI.chat.accept(msg);
		lblApproveExit.setText(ChatClient.error);
		if (ChatClient.error.contains("")) {
			lblApproveExit.setText("Invoice saved in C:/Users/{YourUser}/Desktop/Invoices/"+getOrderNum());
			lblApproveExit.setTextFill(Color.GREEN);
		}
		
	}
	
    /**
     * Retrieves the available places in the park.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the retrieval process.
     */
	public void getAvailablePlaces(ActionEvent event) throws Exception  {
		Message msg = new Message (Message.ActionType.AVAILABLEPLACES,user.getParkName());
		ClientUI.chat.accept(msg);
		lblViewCurrCapacity.setText("There are " + ChatClient.error + " available spots right now.");

	}
	
    /**
     * Handles the entry of unplanned visitors into the park.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the entry process.
     */
	public void enterUnplanned(ActionEvent event) throws Exception  {
		lblUnplannedVisitors.setTextFill(Color.RED);
		Message msg;
		if (lblViewCurrCapacity.getText().isEmpty())
		{
			lblUnplannedVisitors.setText("Please check available places first. " );
		}
		else
		{
			if (getID().isEmpty() || getAmount().isEmpty()) {
				lblUnplannedVisitors.setText("ID and amount are required fields." );
			}
				
			else if (!getID().matches("[0-9]+") || getID().length()!=9) {
				lblUnplannedVisitors.setText("ID must contain only 9 digits." );
			}
			else if (Integer.parseInt(getAmount())>Integer.parseInt(ChatClient.error)) {
					lblUnplannedVisitors.setText("Amount should be less or equal to available spots." );
				}
			else {
				Order order = new Order();
				User user2 = new User(getID());
				msg = new Message (Message.ActionType.USERLOGIN,user2);
				ClientUI.chat.accept(msg);
				user2=ChatClient.user;
				order.setParkName(user.getParkName());
				order.setVisitorId(getID());
				order.setAmountOfVisitors(getAmount());
				order.setVisitorType("individual");
				if (Integer.parseInt(getAmount())>5 && (!user2.getUserPermission().equals("GUIDE")))
					lblUnplannedVisitors.setText("You can't make a reservation for more than 5 visitors");
				else {
					if (Integer.parseInt(getAmount())>1 && (!user2.getUserPermission().equals("GUIDE"))) 
						order.setVisitorType("family group");
					else if (Integer.parseInt(getAmount())>1 && (user2.getUserPermission().equals("GUIDE"))) 
						order.setVisitorType("organized group");
					double arr [] = VisitorHomePageController.calculatePrice(order);
					order.setPayStatus("paid");
					order.setTotalCost(String.valueOf(arr[0]));
					msg = new Message (Message.ActionType.ADDUNPLANNED,order);
					ClientUI.chat.accept(msg);
					lblUnplannedVisitors.setText("Unplanned visitors entered successfully." );
					lblUnplannedVisitors.setTextFill(Color.GREEN);
					getAvailablePlaces( event);
	
					} 
				
			}
					
		}
			
		
	}
 
}
	
