package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import client.ChatClient;
import client.ClientController;
import client.ClientUI;
import common.ChatIF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.User;
import logic.WaitingListEntry;
import ocsf.server.ConnectionToClient;


/**
 * This class controls the functionality of the Waiting List GUI.
 * It allows users to view and enter the waiting list for a park.
 * <p>Author: Julian Abdo</p>
 */

public  class WaitingListController  implements Initializable  {
	/** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static Order object representing the order. */
    public static Order order = ChatClient.order;


	private static int itemIndex = 3;
	 
	@FXML
	private Button btnBack = null,btnEnterWaitingList= null;
	
	@FXML
	private Label lblError,labelParkName;
	
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn<WaitingListEntry, String> placeColumn,timeColumn,numberOfVisitorsColumn,exitTimeColumn,dateColumn;
	
	
    /**
     * Redirects the user back to the visitor home page.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during navigation.
     */
	public void goBackBtn(ActionEvent event) throws Exception {
			
			ChatClient.openGUI.goToGUI(event, "/gui/VisitorHomePage.fxml","","Visitor Home page");
			
		}
	
    /**
     * Enters the waiting list for the selected park.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the entry process.
     */
	public void enterWaitingList(ActionEvent event) throws Exception {
			order=ChatClient.order;
			Message msg=new Message (Message.ActionType.WAITINGLIST,order);
			ClientUI.chat.accept(msg);
			btnEnterWaitingList.setVisible(false);
			msg = new Message (Message.ActionType.WAITINGLISTTABLE,order);
			ClientUI.chat.accept(msg);
			updateWaitingListTable(ChatClient.waitingListEntries);
			lblError.setText("Successfully entered waiting list.");
			lblError.setTextFill(Color.GREEN);


			
		}
	
    /**
     * Gets alternative dates for making a reservation.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during the process.
     */
	public void getAlternativeDate(ActionEvent event) throws Exception {
			order=ChatClient.order;
			Message msg=new Message (Message.ActionType.ALTERNATIVEDATE,order);
			ClientUI.chat.accept(msg);
			
			ArrayList<Order> alt = ChatClient.alternativeOrders;
			String altOrders="";
			for (int i=0;i<5;i++)
				altOrders+=(i+1)+". Time- " + alt.get(i).getTime() + " Date- " + alt.get(i).getDate()+"\n";
			// Create the alert
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Alternative dates");
			alert.setHeaderText("Please choose the time that suits you:");
			alert.setContentText(altOrders);
			
			// Add buttons
			ButtonType buttonOption1 = new ButtonType("1");
			ButtonType buttonOption2 = new ButtonType("2");
			ButtonType buttonOption3 = new ButtonType("3");
			ButtonType buttonOption4 = new ButtonType("4");
			ButtonType buttonOption5 = new ButtonType("5");
			ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(buttonOption1,buttonOption2,buttonOption3,buttonOption4,buttonOption5, buttonTypeCancel);
	
			// Show the alert and wait for user response
			Optional<ButtonType> result = alert.showAndWait();
	
			// Check which button was clicked
			if (result.isPresent() && result.get() == buttonTypeCancel) {
				// User cancelled alternative date selection
			} else {
				order=alt.get(Integer.parseInt(result.get().getText())-1);
				msg = new Message (Message.ActionType.RESERVATION,order);
				ClientUI.chat.accept(msg);
                alert.close();
				Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
		        alert2.setTitle("Order Reservation");
		        alert2.setHeaderText("Reservation success");
		        alert2.setContentText("Thank you for reserving, can't wait to see you!");

		        // Add close button
		        ButtonType closeButton = new ButtonType("Close");
		        alert2.getButtonTypes().setAll(closeButton);
 
		        // Handle button action
		        alert2.showAndWait().ifPresent(response -> {
		            if (response == closeButton) {
		                alert2.close();
		    			ChatClient.openGUI.goToGUI(event, "/gui/VisitorHomePage.fxml","","Visitor Home page");
		                
		            }
		        });
			}
	}
	
	
	  /**
     * Updates the waiting list table with the provided entries.
     *
     * @param entries The waiting list entries to display.
     */
	public void updateWaitingListTable(ArrayList<WaitingListEntry> entries) {
        ObservableList<WaitingListEntry> data = FXCollections.observableArrayList(entries);
        tableView.setItems(data);
		// Assuming your TableColumn objects are named placeColumn, timeColumn, numberOfVisitorsColumn, exitTimeColumn, and dateColumn
		placeColumn.setCellValueFactory(new PropertyValueFactory<>("placement"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		numberOfVisitorsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfVisitors"));
		exitTimeColumn.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		
	}
	
    /**
     * Initializes the waiting list GUI.
     *
     * @param arg0 The URL location to resolve relative paths.
     * @param arg1 The resource bundle.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		labelParkName.setText(order.getParkName());
		updateWaitingListTable(ChatClient.waitingListEntries);

		
		
	}

}