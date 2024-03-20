package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.Message;
import logic.User;
import ocsf.server.ConnectionToClient;



public  class WaitingListController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	
	@FXML
	private Button btnBack = null;
	
	@FXML
	private Label lblError,labelParkName;
	
public void goBackBtn(ActionEvent event) throws Exception {
		
		ChatClient.openGUI.goToGUI(event, "/gui/VisitorHomePage.fxml","","Visitor Home page");
		
	}

}