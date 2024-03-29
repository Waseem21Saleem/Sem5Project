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
import javafx.scene.control.DateCell;
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
import java.time.LocalDate;
import java.time.LocalTime;


/**
 * This class controls the functionality of the Visitor Home Page GUI.
 * It allows visitors to make reservations for parks.
 * <p>Author: Waseem Saleem</p>
 */
public  class VisitorHomePageController implements Initializable   {
	/** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static integer representing an item index. */
    private static int itemIndex = 3;

    /** Static User object representing the user. */
    public static User user = ChatClient.user;

	

	@FXML
	private Label lblError;
	
	@FXML
	private DatePicker datepickDate;
	 
	@FXML
	private TextField txtInputAmountVisitors,txtEmail,txtPhone,txtTime;
	
	@FXML
	private ComboBox cmbSelectPark,cmbSelectDay,cmbSelectMonth,cmbTime;
	
    /**
     * The method retrieves the amount of visitors from the text field.
     *
     * @return The amount of visitors inputted.
     */
	private String getAmountOfVisitors() {
		return txtInputAmountVisitors.getText();
	}
	
    /**
     * Redirects to the Edit Order GUI.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during redirection.
     */
	public void goEditOrder(ActionEvent event) throws Exception {
		/* Send a message to server to check username and password then check Role and open next window 
		 * according to the role
		 */
		Message msg = new Message (Message.ActionType.ORDERSNUMBERS,user); 
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/EditOrder.fxml","/gui/EditOrder.css","Orders Managment Tool");		

		
	}
	
	
    /**
     * Makes a reservation based on the information provided.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the reservation process.
     */
	public void makeReservation(ActionEvent event) throws Exception {
		String visitorType="individual";
		String selectedPark = cmbSelectPark.getValue().toString();
		String amountOfVisitors= txtInputAmountVisitors.getText();
		String date=null;
		if (datepickDate.getValue()!=null)
			 date = datepickDate.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yy")).toString();
		if (selectedPark=="Select park" ||amountOfVisitors==""||date==null
				||txtTime.getText()==""||txtEmail.getText()==""||txtPhone.getText()=="")
			lblError.setText("Missing information");
		else if (!amountOfVisitors.matches("[0-9]+") || Integer.parseInt(amountOfVisitors)<1 || Integer.parseInt(amountOfVisitors)>15)
			lblError.setText("Amount of visitors should be in range of 1 to 15");
		else if (!isValidTime(date,txtTime.getText()))
			lblError.setText("Time must be inbetween 08:00 or higher than current time to 16:00 ");
		else if (!isValidEmail(txtEmail.getText()))
			lblError.setText("Email is not in a correct format");
		else if (!txtPhone.getText().matches("[0-9]+") || txtPhone.getText().length()!=10)
			lblError.setText("Phone number must contain 10 digits");
		else if (Integer.parseInt(amountOfVisitors)>5 && (!user.getUserPermission().equals("GUIDE")))
			lblError.setText("You can't make a reservation for more than 5 visitors");
		else {
			if (Integer.parseInt(amountOfVisitors)>1)
				if (Integer.parseInt(amountOfVisitors)<6&& (!user.getUserPermission().equals("GUIDE")))
					visitorType="family group";
				else
					visitorType="organized group";
			Order order = new Order (selectedPark,user.getId(),visitorType,date,txtTime.getText(),amountOfVisitors,txtPhone.getText(),txtEmail.getText());
			double arr [] = calculatePrice(order);
			// Create the alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Are you sure you want to reserve?");
			alert.setContentText("-The price if you want to reserve now is ₪" + arr[1] + " you will save ₪"+ (arr[0]-arr[1])+" than going without reserving.\n-The price if you want to pay now is ₪" + arr[2] + " you will save ₪"+ (arr[0]-arr[2])+" than going without reserving. ");
			
			// Add buttons
			ButtonType buttonPayLater = new ButtonType("Pay later");
			ButtonType buttonPayNow = new ButtonType("Pay now");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonPayLater,buttonPayNow, buttonTypeCancel);
			// Get the dialog pane
	        alert.getDialogPane().setPrefWidth(600); // Set preferred width
	        alert.getDialogPane().setPrefHeight(200); // Set preferred height
			// Show the alert and wait for user response
			Optional<ButtonType> result = alert.showAndWait();

			// Check which button was clicked
			if (result.isPresent() && result.get() == buttonTypeCancel) {
				
			} else {
				if (result.isPresent() && result.get() == buttonPayNow) {
					order.setPayStatus("paid");
					order.setTotalCost(String.valueOf(arr[2]));
				}
				else
					{order.setPayStatus("not paid");
					order.setTotalCost(String.valueOf(arr[1]));
					}
				order.setOrderStatus("processing");
				Message msg = new Message (Message.ActionType.RESERVATION,order);
				ClientUI.chat.accept(msg);
				if (ChatClient.error!="") {
					lblError.setText(ChatClient.error);
					lblError.setTextFill(Color.GREEN);
				} else {
					
					msg = new Message (Message.ActionType.WAITINGLISTTABLE,order);
					ClientUI.chat.accept(msg);
					ChatClient.openGUI.goToGUI(event, "/gui/WaitingList.fxml","","Waiting list");
					
				}
			}
			
		}
	}
	
    /**
     * Calculates the price for the given order.
     *
     * @param order The order for which the price is calculated.
     * @return An array containing the normal price, reservation price, and pay now price.
     */
	public static double [] calculatePrice ( Order order ) {
		double normalPrice=100;
		double reservationPrice=100;
		double payNowPrice=100;
		double arr []  = new double [3];
		int amountOfVisitors=Integer.parseInt(order.getAmountOfVisitors());
		if ( amountOfVisitors<6) {
			normalPrice*=amountOfVisitors;
			reservationPrice=normalPrice*(0.85);
			payNowPrice=reservationPrice;
		}
		else {
			normalPrice*=(amountOfVisitors*0.9);
			reservationPrice*=((amountOfVisitors-1)*0.75);
			payNowPrice=reservationPrice*0.88;
		}
		arr[0]=normalPrice;
		arr[1]=reservationPrice;
		arr[2]=payNowPrice;
			
		
		
		return arr;
	}
	
	
    /**
     * Logs out the current user.
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
     * Sets up the Park combo box.
     */
	public void setParkComboBox() {
		  	this.cmbSelectPark.setValue("Select park");
		    this.cmbSelectPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
			
  }
	
	   /**
     * Validates if the given email is in a correct format.
     *
     * @param email The email to be validated.
     * @return True if the email is in a correct format, false otherwise.
     */
	public static boolean isValidEmail(String email) {
        // Regular expression for email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
	
	
    /**
     * Validates if the given time is in the correct format and within the range of 08:00 to 16:00.
     *
     * @param timeStr The time to be validated.
     * @param date The date to be validated
     * @return True if the time is in the correct format and within the range, false otherwise.
     */
	public static boolean isValidTime(String date,String timeStr) {


		
        String timeRegex = "^(?:[01]\\d|2[0-3]):(?:[0-5]\\d)$";
        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(timeStr);

        if (!matcher.matches()) {
            return false; // Not in hh:mm format
        }

        // Parse hour and minute values
        String[] parts = timeStr.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        // Check if time falls between 08:00 and 16:00
        return (hour >= 8 && hour <= 16 && minute >= 0 && minute <= 59 );
    }
	
    /**
     * Initializes the Park combo box and sets the date picker's minimum date to tomorrow
     *  and maximum date to next year
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		setParkComboBox(); 
		// Set the minimum date to tomorrow and maximum to next year
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        datepickDate.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(tomorrow) < 0 || date.compareTo(tomorrow.plusYears(1)) > 0);
            }
        });

		
		 
	}
	
	

	
}
