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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Order;

public class DepartmentManagerApprovalController   {

	public static ClientController chat;
	@FXML
	private Button btnReject,btnAccept,btnBack;
	
	@FXML
	private TableView listApprove;
	/**
	   * This method loads the current order into the fx
	   *
	   *@param order
  
	   */	
	public void accept(ActionEvent event) throws Exception {
		  Order selectedOrder = (Order) listApprove.getSelectionModel().getSelectedItem();
	        if (selectedOrder != null) {
	            //selectedOrde("Accepted"); // Update status to "Accepted"
	            // Call a method to update the order status in your data source/database
	            // For example: OrderDAO.updateOrderStatus(selectedOrder.getId(), "Accepted");
	            listApprove.refresh(); // Refresh the table view
	        }
	}
	public void reject (ActionEvent event) throws Exception {
		Order selectedOrder = (Order) listApprove.getSelectionModel().getSelectedItem();
	        if (selectedOrder != null) {
	            //selectedOrder.// Update status to "Rejected"
	            // Call a method to update the order status in your data source/database
	            // For example: OrderDAO.updateOrderStatus(selectedOrder.getId(), "Rejected");
	            //listApprove.refresh(); // Refresh the table view
	        }
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
}

