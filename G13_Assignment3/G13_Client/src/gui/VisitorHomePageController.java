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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Message;
import logic.User;
import ocsf.server.ConnectionToClient;



public  class VisitorHomePageController implements Initializable   {
	public static ClientController chat;
	private static int itemIndex = 3;
	public static User user=ChatClient.user;

	

	@FXML
	private Label lblError;
	
	@FXML
	private DatePicker datepickDate;
	
	@FXML
	private TextField txtInputAmountVisitors,txtEmail,txtPhone,txtTimeHour;
	
	@FXML
	private ComboBox cmbSelectPark,cmbSelectDay,cmbSelectMonth,cmbTime;
	
	private String getAmountOfVisitors() {
		return txtInputAmountVisitors.getText();
	}
	
	/**
	   * This method runs the order form fx 
	   * and adds the client info into the database when "open order manager" button pressed
	   *@param event , the "open order manager" button
	   
	   */
	public void goEditOrder(ActionEvent event) throws Exception {
		/* Send a message to server to check username and password then check Role and open next window 
		 * according to the role
		 */
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/EditOrder.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/EditOrder.css").toExternalForm());
		primaryStage.setTitle("Orders Managment Tool");

		primaryStage.setScene(scene);		
		primaryStage.show();


        

		
	}
	
	
	public void makeReservation(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/ConfirmOrder.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/gui/SignUp.css").toExternalForm());
		primaryStage.setTitle("Confirm order page");

		primaryStage.setScene(scene);		
		primaryStage.show();
	}
	
	

	public void Logout(ActionEvent event) throws Exception {
		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/LoginWithoutPassword.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/LoginWithoutPassword.css").toExternalForm());
		primaryStage.setTitle("Visitor login page");

		primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	
	public void setParkComboBox() {
		  	this.cmbSelectPark.setValue("Select park");
		    this.cmbSelectPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
			
			// Add an event handler to the ComboBox
		    /*cmbSelectPark.setOnAction(event -> {
				
				String selectedValue=(String) cmbPark.getValue();
				ordersList = new ArrayList<String>();
				ordersList.add(selectedValue);
				ClientUI.chat.accept(ordersList);
				loadOrder(ChatClient.OrderInfo);
				


	            
	            lblSave.setText("");
	            
	        });*/
  }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		setParkComboBox();
		
		
		
	}
	
	

	
}
