
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Report;
import logic.User;
import ocsf.server.ConnectionToClient;




public  class DepartmentManagerHomePageController  implements Initializable {
	public static ClientController chat;
	private static int itemIndex = 3;
	public static User user;


	@FXML
	private Label lblError;
	
	/*@FXML
	private TextField txtInputAmountVisitors;*/
	
	@FXML
	private ComboBox cmbSelectPark,cmbReportType,cmbReportMonth,cmbReportYear;
	
	/*private String getAmountOfVisitors() {
		return txtInputAmountVisitors.getText();
	}*/
	
	/**
	   * This method runs the order form fx 
	   * and adds the client info into the database when "open order manager" button pressed
	   *@param event , the "open order manager" button
	   
	   */
	public void goApproveCapacity(ActionEvent event) throws Exception {
		/* Send a message to server to check username and password then check Role and open next window 
		 * according to the role
		 */
		Message msg = new Message (Message.ActionType.REQUESTSTABLE,"");
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/DepartmentManagerApproval.fxml","/gui/DepartmentManagerApproval.css","Department Managment Tool");



        

		
	}
	
	public void goCreateReport(ActionEvent event) throws Exception {
		ChatClient.openGUI.goToGUI(event, "/gui/ParkManagerHomePage.fxml","","Report creation page");

	}
	
	
	public void goViewReport(ActionEvent event) throws Exception {
		Message msg;
		String reportType = cmbReportType.getValue().toString();
		Report report = new Report (reportType,cmbSelectPark.getValue().toString(),cmbReportMonth.getValue().toString(),cmbReportYear.getValue().toString());
		msg = new Message (Message.ActionType.REPORTINFO,report);
		ClientUI.chat.accept(msg);
		if (ChatClient.error.equals(""))
			ChatClient.openGUI.goToGUI(event, "/gui/CancellationReport.fxml","","Report view page");
		else {
			lblError.setText(ChatClient.error);
			lblError.setTextFill(Color.RED);
			}
		}
	 

	public void Logout(ActionEvent event) throws Exception {
		Message msg = new Message (Message.ActionType.LOGOUT,user);
		ClientUI.chat.accept(msg);
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Visitor login page");

		
	}
	 
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;
		List<String> reportTypes = new ArrayList<String>();
		reportTypes.add("Total visitors report");
		reportTypes.add("Usage report");
		reportTypes.add("Cancellation report");
		reportTypes.add("Visits report");
		this.cmbReportType.setValue("Report type");
		this.cmbReportType.setItems(FXCollections.observableArrayList(reportTypes));
		this.cmbSelectPark.setValue("Park name");
		this.cmbSelectPark.setItems(FXCollections.observableArrayList(ChatClient.parkNames));
		this.cmbReportMonth.setValue("Month");
		this.cmbReportMonth.setItems(FXCollections.observableArrayList(ChatClient.months));
		this.cmbReportYear.setValue("Year");
		this.cmbReportYear.setItems(FXCollections.observableArrayList(ChatClient.years));
		
	}

	
}
