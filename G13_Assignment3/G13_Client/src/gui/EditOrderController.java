package gui;


import java.net.InetAddress;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.User;

public class EditOrderController implements Initializable {

	private Order order;	
	public static User user=ChatClient.user;

	
	@FXML
	private Label lblOrderNum,lblSave,lblError;
	
	@FXML
	private TextField txtVisitors,txtEmail,txtPhone,txtTime;
		
	@FXML
	private Button btnSave,btnBack,btnDeleteOrder;
	@FXML
	private DatePicker datepickerDate;
	
	@FXML
	private ComboBox cmbPark,cmbOrder,cmbMonth,cmbDay,cmbTime;
	
	public ChatClient chatc;
	
	
	public static ArrayList<String> ordersList= new ArrayList<String>();;
	
	
	ObservableList<String> list;

	/**
	   * This method loads the current order into the fx
	   *
	   *@param order
  
	   */	
	public void loadOrder(Order order) {
		
		this.cmbPark.setValue(order.getParkName());
		// Parse the String to a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        LocalDate date = LocalDate.parse(order.getDate(), formatter);

				
		this.datepickerDate.setValue(date);
		this.txtTime.setText(order.getTime());
		this.txtVisitors.setText(order.getAmountOfVisitors());
		this.txtEmail.setText(order.getEmail());
		this.txtPhone.setText(order.getTelephone());
		
	}

	/**
	   * This method updates the comboBox
	   *
 
	   */	
	public void setOrderComboBox() {
		  	this.cmbOrder.setValue("Order number");
		  	this.cmbOrder.setItems(FXCollections.observableArrayList(ChatClient.Orderslist));
		  	this.cmbPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
			// Add an event handler to the ComboBox
		    cmbOrder.setOnAction(event -> {
				
				String selectedValue=(String) cmbOrder.getValue();
				Order order = new Order (selectedValue);
				Message  msg = new Message (Message.ActionType.ORDERINFO,order); 
				ClientUI.chat.accept(msg);
				loadOrder(ChatClient.order);
				


	            
	            lblSave.setText("");
	            
	        });
    }
    
    
	public void refreshComboBox(String selectedValue) {
		/*  ordersList = new ArrayList<String>();
		  ordersList.add(selectedValue);
		  ClientUI.chat.accept(ordersList);
		  loadOrder(ChatClient.OrderInfo);*/
	}
	

	
	
	/**
	   * This method closes the fx when close button pressed and deletes the clients info from the database
	   *
	   *@param event, the Close button
	   
	   */	
	public void goBackBtn(ActionEvent event) throws Exception {

		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/VisitorHomePage.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		//scene.getStylesheets().add(getClass().getResource("/gui/VisitorHomePage.css").toExternalForm());
		primaryStage.setTitle("Visitor HomePage");

		primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	
	
	/**
	   * This method updates the order info (name and telephone) into the database
	   *
	   *@param event, the Close button
	   
	   */	
	public void getSaveBtn(ActionEvent event) throws Exception {
		
		String visitorType="individual";
		String selectedOrder = cmbOrder.getValue().toString();
		String selectedPark = cmbPark.getValue().toString();
		String amountOfVisitors= txtVisitors.getText();
		String date=null;
		if (datepickerDate.getValue()!=null)
			 date = datepickerDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yy")).toString();
		if (checkInput()) {
			if (Integer.parseInt(amountOfVisitors)>1)
				if (Integer.parseInt(amountOfVisitors)<6)
					visitorType="small group";
				else
					visitorType="big group";
			Order order = new Order (selectedPark,selectedOrder,user.getId(),visitorType,date,txtTime.getText(),amountOfVisitors,txtPhone.getText(),txtEmail.getText());
			double arr [] = VisitorHomePageController.calculatePrice(order);
			// Create the alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Are you sure you want to reserve?");
			alert.setContentText("The price if you want to reserve now is ₪" + arr[1] + " you will save ₪"+ (arr[0]-arr[1])+" than going without reserving." );
			
			// Add buttons
			ButtonType buttonTypeAccept = new ButtonType("Accept");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);

			// Show the alert and wait for user response
			Optional<ButtonType> result = alert.showAndWait();
			System.out.println("TEST");
			// Check which button was clicked
			if (result.isPresent() && result.get() == buttonTypeAccept) {
				Message msg = new Message (Message.ActionType.UPDATEORDER,order);
				System.out.println(order.getAmountOfVisitors());
				ClientUI.chat.accept(msg);
				if (ChatClient.error!="") {
					lblError.setText(ChatClient.error);
					lblError.setTextFill(Color.GREEN);
				} else {
					ChatClient.openGUI.goToGUI(event, "/gui/WaitingList.fxml","","Waiting list");
					
				}
			} else {
			    // User clicked Cancel or closed the dialog
			    // Do nothing or handle cancellation
			}
			
		}
		
	}
	
	public Boolean checkInput () {
		lblError.setText("");
		String selectedPark = cmbPark.getValue().toString();
		String amountOfVisitors= txtVisitors.getText();
		String date=null;
		if (datepickerDate.getValue()!=null)
			 date = datepickerDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yy")).toString();
		if (selectedPark=="Select park" ||amountOfVisitors==""||date==null
				||txtTime.getText()==""||txtEmail.getText()==""||txtPhone.getText()=="")
			lblError.setText("Missing information");
		else if (!amountOfVisitors.matches("[0-9]+") || Integer.parseInt(amountOfVisitors)<1 || Integer.parseInt(amountOfVisitors)>15)
			lblError.setText("Amount of visitors should be in range of 1 to 15");
		else if (!VisitorHomePageController.isValidTime(txtTime.getText()))
			lblError.setText("Time must be inbetween 08:00-16:00");
		else if (!VisitorHomePageController.isValidEmail(txtEmail.getText()))
			lblError.setText("Email is not in a correct format");
		else if (!txtPhone.getText().matches("[0-9]+") || txtPhone.getText().length()!=10)
			lblError.setText("Phone number must contain 10 digits");
		else if (Integer.parseInt(amountOfVisitors)>5 && (!user.getUserPermission().equals("GUIDE")))
			lblError.setText("You can't make a reservation for more than 5 visitors");
		if (lblError.getText()=="")
			return true;
		return false;
	}
	
	

	/**
	   * This method initializes the fx
	   *
	   *@param arg0
	   *@param arg1
	   
	   */	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		setOrderComboBox();
		
		
		
	}
	
}
