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
import ocsf.server.ConnectionToClient;



/**
 * This class controls the main frame of the application.
 * It provides functionality to connect to the server and exit the application.
 * <p>Author: Waseem Saleem</p>
 */
public  class MainFrameController   {
    /** The client controller instance for client-server communication. */
	public static ClientController chat;
	private static int itemIndex = 3;
	
	@FXML
	private Button btnExit = null;
	 
	@FXML
	private Button btnConnect = null;
	@FXML
	private Label lblError;
	
	@FXML
	private TextField txtIP,txtPort;
	
    /**
     * Gets the server IP entered by the user.
     *
     * @return The server IP.
     */
	private String getIP() {
		return txtIP.getText();
	}
	
    /**
     * Gets the server port entered by the user.
     *
     * @return The server port.
     */
	private String getPort() {
		return txtPort.getText();
	} 


	 /**
     * Connects to the server.
     *
     * @param event The action event triggered by the connect button.
     * @throws Exception If an error occurs during connection.
     */
	public void Connect(ActionEvent event) throws Exception {

		try {
		
			ClientUI.chat = new ClientController(getIP(), Integer.parseInt(getPort()));
}
		catch (Exception exception){
			lblError.setText("There is no running server on the entered parameters");
		}
		ChatClient.openGUI.goToGUI(event, "/gui/LoginWithoutPassword.fxml","/gui/LoginWithoutPassword.css","Login page");
		ClientUI.chat.accept("refresh");

        

		
	}
	
    /**
     * Starts the application.
     *
     * @param primaryStage The primary stage of the application.
     * @throws Exception If an error occurs during application startup.
     */
	public void start(Stage primaryStage) throws Exception {	

		
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
	   *@param event The ActionEvent triggered by the exit button press.
	   *@throws Exception If an error occurs during the exit process
	   */	
	public void getExitBtn(ActionEvent event) throws Exception {
		
		System.exit(0);
		
	}
	
	
	
}
