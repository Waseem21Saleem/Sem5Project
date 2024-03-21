package gui;


import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.Request;
import logic.User;
import logic.WaitingListEntry;
import mysqlConnection.mysqlConnection;

public class DepartmentManagerApprovalController implements Initializable  {


	public static ClientController chat;
	@FXML
	private Button btnReject,btnAccept,btnBack;
	@FXML
	private Label lblError;
	
	@FXML
	private TableView requestsTable;
	
	@FXML
	private TableColumn<Request, String> columnParkName,columnCapacity,columnMaxStay;
	
	/**
	   * This method loads the current order into the fx
	   *
	   *@param order
  
	   */	
	
	
	
	public void accept(ActionEvent event) throws Exception {
		
		Request  selectedRequest = (Request) requestsTable.getSelectionModel().getSelectedItem();
		 if (selectedRequest != null) {
				selectedRequest.setStatus("approved");
				Message msg=new Message (Message.ActionType.APPROVEREQUEST,selectedRequest);
				ClientUI.chat.accept(msg);
				requestsTable.getItems().remove(selectedRequest);// Remove the selected order from the table view
				lblError.setText("Request approved successfully");
				lblError.setTextFill(Color.GREEN);

		 }
		 else 
		 {
				lblError.setText("Please choose a request before approving");
			lblError.setTextFill(Color.RED);
		 }

        }
	        
	        
	       
	
	public void reject (ActionEvent event) throws Exception {
		Request  selectedRequest = (Request) requestsTable.getSelectionModel().getSelectedItem();
		 if (selectedRequest != null) {
				selectedRequest.setStatus("rejected");
				Message msg=new Message (Message.ActionType.REJECTREQUEST,selectedRequest);
				ClientUI.chat.accept(msg);
				requestsTable.getItems().remove(selectedRequest);// Remove the selected order from the table view
				lblError.setText("Request rejected successfully");
				lblError.setTextFill(Color.GREEN);

		 }
		 else {
				lblError.setText("Please choose a request before approving");
				lblError.setTextFill(Color.RED);
		 }

	        
	}
	
	public void updateWaitingListTable(ArrayList<Request> requests) {
        ObservableList<Request> data = FXCollections.observableArrayList(requests);
        requestsTable.setItems(data);
		// Assuming your TableColumn objects are named placeColumn, timeColumn, numberOfVisitorsColumn, exitTimeColumn, and dateColumn
        columnParkName.setCellValueFactory(new PropertyValueFactory<>("parkName"));
        columnCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        columnMaxStay.setCellValueFactory(new PropertyValueFactory<>("maxStay"));

		
	}
	public void goBackBtn(ActionEvent event) throws Exception {
			String fxml="",css="",title="";
			if (event.getSource()==btnBack) {
				fxml="/gui/DepartmentManagerHomePage.fxml";
				css="/gui/DepartmentManagerHomePage.fxml";
				title="Department Manager home page";
			
			}
			else if (event.getSource()==btnAccept) {
				fxml="/gui/DepartmentManagerHomePage.fxml";
				css="/gui/DepartmentManagerHomePage.fxml";
				title="Department Manager home page";
	
			}
			FXMLLoader loader = new FXMLLoader();
			((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource(fxml).openStream());		
			
		
			Scene scene = new Scene(root);			
			scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
			primaryStage.setTitle(title);
	
			primaryStage.setScene(scene);		
			primaryStage.show();
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		updateWaitingListTable(ChatClient.requests);
		
	}
}