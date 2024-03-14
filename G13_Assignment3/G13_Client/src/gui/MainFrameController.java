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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;


public  class MainFrameController   {
	public static ClientController chat;
	private static int itemIndex = 3;
	
	@FXML
	private Button btnExit = null;
	
	@FXML
	private Button btnSend = null;
	
	@FXML
	private TextField idtxt;
	
	private String getID() {
		return idtxt.getText();
	}
	
	/**
	   * This method runs the order form fx 
	   * and adds the client info into the database when "open order manager" button pressed
	   *@param event , the "open order manager" button
	   
	   */
	public void Send(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/OrderForm.fxml").openStream());		
		
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/OrderForm.css").toExternalForm());
		primaryStage.setTitle("Orders Managment Tool");

		primaryStage.setScene(scene);		
		primaryStage.show();

		ClientUI.chat.accept("refresh");

        

		
	}
	
	/**
	   * This method starts the fx
	   *
	   *@param primaryStage
	   
	   */	
	public void start(Stage primaryStage) throws Exception {	
		InetAddress localHost = InetAddress.getLocalHost();
        String clientIP = localHost.getHostAddress();
        String clientHostName = localHost.getHostName();

        ClientUI.chat.accept("ClientConnected "+clientIP+" "+clientHostName);	
		Parent root = FXMLLoader.load(getClass().getResource("/gui/MainFrame.fxml"));
				
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/MainFrame.css").toExternalForm());
		primaryStage.setTitle("Home Page");
		primaryStage.setScene(scene);
		
		primaryStage.show();



	}
	
	/**
	   * This method exits the client when exit button pressed
	   *
	   *@param event
	   
	   */	
	public void getExitBtn(ActionEvent event) throws Exception {
		InetAddress localHost = InetAddress.getLocalHost();
        String clientIP = localHost.getHostAddress();
        ClientUI.chat.accept("ClientDisonnected "+clientIP);
		System.exit(0);
		
	}
	

	
}