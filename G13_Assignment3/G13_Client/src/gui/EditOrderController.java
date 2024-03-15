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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Order;

public class EditOrderController implements Initializable {

	private Order order;	
	
	@FXML
	private Label lblOrderNum,lblSave,lblError;
	
	@FXML
	private TextField txtVisitors;
		
	@FXML
	private Button btnSave,btnBack;
	
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
	public void loadOrder(ArrayList<String> orderInfo) {
		/*
		this.txtName.setText(orderInfo.get(0));
		
		this.lblOrderNum.setText(orderInfo.get(1));
		
		this.lblTime.setText(orderInfo.get(2));
		
		this.lblVisitors.setText(orderInfo.get(3));
		
		this.txtTel.setText(orderInfo.get(4));
	
		this.lblEmail.setText(orderInfo.get(5));*/
		
	}

	/**
	   * This method updates the comboBox
	   *
 
	   */	
	public void setParkComboBox() {
		  /*this.cmbPark.setValue("Order number");
			ordersList = new ArrayList<String>();
			ordersList.add("getOrders");

		    ClientUI.chat.accept(ordersList);
		    ordersList = ChatClient.Orderslist;
		    this.list = FXCollections.observableArrayList(ordersList);
		    this.cmbPark.setItems(list);
		    System.out.println(ordersList.toString());
			
			// Add an event handler to the ComboBox
			cmbPark.setOnAction(event -> {
				
				String selectedValue=(String) cmbPark.getValue();
				ordersList = new ArrayList<String>();
				ordersList.add(selectedValue);
				ClientUI.chat.accept(ordersList);
				loadOrder(ChatClient.OrderInfo);
				


	            
	            lblSave.setText("");
	            
	        });*/
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
		
		/*if (event.getSource() ==btnsave) { 
			String[] text = {"updateOrder",lblOrderNum.getText(),txtName.getText(),txtTel.getText()};
			ClientUI.chat.accept(text);
			//order.setParkName(txtName.getText());
			//Thread.sleep(2000);
			refreshComboBox((String) cmbPark.getValue());
			lblSave.setText("Updated successfully!");
			
			
			
		}*/
		
	}
	
	

	/**
	   * This method initializes the fx
	   *
	   *@param arg0
	   *@param arg1
	   
	   */	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		setParkComboBox();
		
		
		
	}
	
}
