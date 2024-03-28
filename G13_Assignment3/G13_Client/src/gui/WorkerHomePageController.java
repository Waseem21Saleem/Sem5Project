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



public  class WorkerHomePageController  implements Initializable    {
	public static ClientController chat;
	private static int itemIndex = 3;
	public static User user;


 
	@FXML
	private Label lblParkName,lblViewCurrCapacity,lblUnplannedVisitors,lblApproveExit;
	
	@FXML
	private Button btnCheckCapacity,btnUnplanned;
	
	@FXML
	private TextField txtEnterID,txtAmount,txtOrderNumber;
	
	private String getID() {
		return txtEnterID.getText();
	}
	private String getAmount() {
		return txtAmount.getText();
	}
	private String getOrderNum() {
		return txtOrderNumber.getText();
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;
		lblParkName.setText(user.getParkName());
		
	}
	
	public void Logout(ActionEvent event) throws Exception {
		

		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		
	}
	
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
	public void getAvailablePlaces(ActionEvent event) throws Exception  {
		Message msg = new Message (Message.ActionType.AVAILABLEPLACES,user.getParkName());
		ClientUI.chat.accept(msg);
		lblViewCurrCapacity.setText("There are " + ChatClient.error + " available spots right now.");

	}
	public void enterUnplanned(ActionEvent event) throws Exception  {
		lblUnplannedVisitors.setTextFill(Color.RED);
		Message msg;
		if (lblViewCurrCapacity.getText().isEmpty())
		{
			lblUnplannedVisitors.setText("Please check available places first. " );
			//lblUnplannedVisitors.setTextFill(Color.RED);
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
	
