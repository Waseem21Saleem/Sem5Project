
package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Park;
import logic.Report;
import logic.User;
import ocsf.server.ConnectionToClient;



/**
 * This class controls the functionality of the Park Manager Home Page GUI.
 * It allows park managers to view reports and change park capacities.
 * <p>Author: Noor Khateeb</p>
 */
public  class ParkManagerHomePageController implements Initializable  {
	 /** Static ClientController for chat functionality. */
    public static ClientController chat;

    /** Static integer representing an item index. */
    private static int itemIndex = 3;

    /** Static User object representing the user. */
    public static User user;

 
	@FXML
	private Label lblError,labelChangeCapacity,lblSelectPark;
	
	@FXML
	private Hyperlink hyperClickHere;
	@FXML
	private ComboBox cmbReportType,cmbChooseMonth,cmbChooseYear,cmbTime,cmbSelectPark;
	@FXML
	private Button btnLogOut;
	
	
    /**
     * Redirects to the page for changing park capacity.
     *
     * @param event The action event triggered by the hyperlink.
     * @throws Exception If an error occurs during navigation.
     */
	public void goChangeCapacity(ActionEvent event) throws Exception {

		Message msg = new Message (Message.ActionType.PARKINFO,user.getParkName());
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/ParkManagerChangeCapacity.fxml","","Change capacity Tool");
		


        

		
	}
	
    /**
     * Creates a report based on the selected options.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during report generation.
     */
	public void createReportPage(ActionEvent event) throws Exception {
		lblError.setTextFill(Color.RED);
		Message msg=getMessage();
		ClientUI.chat.accept(msg);
		lblError.setText(ChatClient.error);
		if (lblError.getText().contains("successfully"))
			lblError.setTextFill(Color.GREEN);
	}
	
    /**
     * Constructs the message to request a report based on user selections.
     *
     * @return The message containing the report request.
     */
	public Message getMessage() {
		Message msg;
		Report report;
		String reportType = cmbReportType.getValue().toString();
		String parkName = "";
		if (user.getUserPermission().equals("PARK MANAGER"))
			parkName = user.getParkName();
		else
			parkName = cmbSelectPark.getValue().toString();
		report = new Report(reportType,parkName,cmbChooseMonth.getValue().toString(),cmbChooseYear.getValue().toString());
		if (reportType.equals("Total visitors report"))
			msg = new Message (Message.ActionType.TOTALVISITORSREPORT,report);
		else if (reportType.equals("Usage report"))
			msg = new Message (Message.ActionType.USAGEREPORT,report);
		else if (reportType.equals("Cancellation report"))
			msg = new Message (Message.ActionType.CANCELLATIONREPORT,report);
		else
			msg = new Message (Message.ActionType.VISITINGREPORT,report);
		return msg;
	}

    /**
     * Logs out the user and redirects to the appropriate page.
     *
     * @param event The action event triggered by the button.
     * @throws Exception If an error occurs during logout.
     */
	public void Logout(ActionEvent event) throws Exception {
		if (btnLogOut.getText().equals("Back"))
			ChatClient.openGUI.goToGUI(event, "/gui/DepartmentManagerHomePage.fxml","","Department manager home page");
		else {

		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		}
	}
	
	   /**
     * Initializes the controller with the current user and sets up combo box options.
     *
     * @param arg0 The URL location of the FXML file.
     * @param arg1 The resource bundle.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;
		List<String> reportTypes = new ArrayList<String>();
		this.cmbChooseMonth.setValue("Month");
		this.cmbChooseMonth.setItems(FXCollections.observableArrayList(ChatClient.months));
		this.cmbChooseYear.setValue("Year");
		this.cmbChooseYear.setItems(FXCollections.observableArrayList(ChatClient.years));
		this.cmbReportType.setValue("Report type");
		if (user.getUserPermission().equals("PARK MANAGER")) {
			reportTypes.add("Total visitors report");
			reportTypes.add("Usage report");
		}
		else
		{
			reportTypes.add("Cancellation report");
			reportTypes.add("Visits report");
			labelChangeCapacity.setVisible(false);
			hyperClickHere.setVisible(false);
			cmbSelectPark.setVisible(true);
			this.cmbSelectPark.setValue("Park name");
			this.cmbSelectPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
			lblSelectPark.setVisible(true);
			btnLogOut.setText("Back");
			
		}
	  	this.cmbReportType.setItems(FXCollections.observableArrayList(reportTypes));
		
	} 

	
}
