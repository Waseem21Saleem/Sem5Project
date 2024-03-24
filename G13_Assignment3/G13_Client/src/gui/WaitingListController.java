package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
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
import logic.User;
import logic.WaitingListEntry;
import ocsf.server.ConnectionToClient;




public  class WaitingListController  implements Initializable  {
	public static ClientController chat;
	public static Order order=ChatClient.order;

	private static int itemIndex = 3;
	
	@FXML
	private Button btnBack = null,btnEnterWaitingList= null;
	
	@FXML
	private Label lblError,labelParkName;
	
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn<WaitingListEntry, String> placeColumn,timeColumn,numberOfVisitorsColumn,exitTimeColumn,dateColumn;
	
	
	public void goBackBtn(ActionEvent event) throws Exception {
			
			ChatClient.openGUI.goToGUI(event, "/gui/VisitorHomePage.fxml","","Visitor Home page");
			
		}
	
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
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	

		labelParkName.setText(order.getParkName());
		updateWaitingListTable(ChatClient.waitingListEntries);

		
		
	}

}