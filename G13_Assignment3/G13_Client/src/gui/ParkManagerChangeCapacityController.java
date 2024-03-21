
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
import logic.Park;
import logic.User;
import ocsf.server.ConnectionToClient;




public  class ParkManagerChangeCapacityController  implements Initializable {
	public static ClientController chat;
	public static User user;

	private static int itemIndex = 3;
	

	@FXML
	private Label lblError,lblParkName;
	
	@FXML
	private TextField txtParkCapacity,txtMaxStay;
	
	private String getCapacity() {
		return txtParkCapacity.getText();
	}
	private String getMaxTime() {
		return txtMaxStay.getText();
	}

	
	public void goBack(ActionEvent event) throws Exception {
		ChatClient.openGUI.goToGUI(event, "/gui/ParkManagerHomePage.fxml","","Park Manager home page");
	}
	

	public void Confirm(ActionEvent event) throws Exception {
		if (!getCapacity().matches("[0-9]+") || getCapacity().length()>3 )
			lblError.setText("Capacity must contain only digits and max is 999.\n");
		else if (!getMaxTime().matches("[0-9]+") || getMaxTime().length()!=1 || getMaxTime().equals("0"))
			lblError.setText("MaxTime must contain only digits in range 1-9 hours.\n");
		else {
			Park park = new Park (lblParkName.getText(),getCapacity(),getMaxTime());
			Message msg = new Message (Message.ActionType.NEWREQUEST,park);
			ClientUI.chat.accept(msg);
			lblError.setTextFill(Color.GREEN);
			lblError.setText("Change request sent to Department manager");
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		user=ChatClient.user;;
		Park park=ChatClient.park;
		lblParkName.setText(user.getParkName());
		txtMaxStay.setText(park.getMaxStay());
		txtParkCapacity.setText(park.getCapacity());
		
		
	}
	
	

	
}
