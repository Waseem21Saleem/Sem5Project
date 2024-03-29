package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Server.EchoServer;
import Server.ServerUI;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.CurClient;
import logic.Order;
import logic.Request;
import mysqlConnection.mysqlConnection;
import ocsf.server.ConnectionToClient;

/**
 * Controller class for managing server information GUI.
 */
public class ServerInfoController implements Initializable {
	
	private EchoServer ev;

	@FXML
	private Label lblServer,lblError,labelServerIP;
	@FXML
	private Button btnclose=null;
	@FXML
	private Button btnConnect;
	@FXML
	private ComboBox cmbClient;
	
	static CurClient curClient;
	
	/**
	 * List of host addresses.
	 */
	public static ArrayList<String> hostList= new ArrayList<String>();
	
	ObservableList<String> list;
	@FXML
	private TextField txtPort,txtDbPath,txtDbUser;
	@FXML
	private PasswordField txtDbPass;
	@FXML 
	private  TableView tableView;
	@FXML
	private  TableColumn columnIp;

	@FXML
	private  TableColumn columnHost;

	
    /**
     * Retrieves the port number entered by the user.
     * 
     * @return Port number as a string
     */
	private String getport() {
		return txtPort.getText();			
	}
	
    /**
     * Retrieves the database path entered by the user.
     * 
     * @return Database path as a string
     */
	private String getDbPath() {
		return txtDbPath.getText();			
	}
	
    /**
     * Retrieves the database username entered by the user.
     * 
     * @return Database username as a string
     */
	private String getDbUsername() {
		return txtDbUser.getText();			
	}
	
	   /**
     * Retrieves the database password entered by the user.
     * 
     * @return Database password as a string
     */
	private String getDbPassword() {
		return txtDbPass.getText();			
	}
	

	
	/**
     * Handles the event when a client gets disconnected.
     */
	public void ClientDisconnected( ) {

	}
	
	
	
	  /**
     * Sets the current client with the provided parameters.
     * 
     * @param ip         IP address of the client
     * @param host       Host name of the client
     * @param connection Connection status of the client
     */
	public static void setCurClient (String ip,String host, String connection) {
		curClient = new CurClient(ip,host,connection);
		
	}
	
	/**
     * Closes the GUI when the close button is pressed.
     * 
     * @param event Close button event
     * @throws Exception Exception when exiting
     */
	public void closeBtn(ActionEvent event) throws Exception {
		System.exit(0);
		
	}
	
	
    /**
     * Updates the waiting list table with the provided list of clients.
     * 
     * @param clients List of clients to update the table
     */
	public void updateWaitingListTable(ArrayList<CurClient> clients) {
		
        ObservableList<CurClient> data = FXCollections.observableArrayList(clients);
        tableView.setItems(data);
		// Assuming your TableColumn objects are named placeColumn, timeColumn, numberOfVisitorsColumn, exitTimeColumn, and dateColumn
        columnIp.setCellValueFactory(new PropertyValueFactory<>("ip"));
        columnHost.setCellValueFactory(new PropertyValueFactory<>("host"));

		
	}
	
	
    /**
     * Initializes the server information GUI.
     * 
     * @param arg0 URL location
     * @param arg1 ResourceBundle resources
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		//this.lblConnection.setText("Client not connected");
		 
	    }


    /**
     * Starts the server information GUI.
     * 
     * @param primaryStage Primary stage of the application
     * @throws Exception Exception when starting GUI
     */
	public void start(Stage primaryStage) throws Exception {	

		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerInfo.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerInfo.css").toExternalForm());
		primaryStage.setTitle("Server page");
		primaryStage.setScene(scene);
		primaryStage.show();
	
	}
	

    /**
     * Connects the server to the selected port and database.
     * 
     * @param event Event when the user presses the Connect button
     * @throws Exception Exception when connecting server
     */
	public void connectServer(ActionEvent event) throws Exception {
		String p,dbPath,dbUsername,dbPassword;
		InetAddress localHost = InetAddress.getLocalHost();
        String ServerIP = localHost.getHostAddress();
		labelServerIP.setText(ServerIP);
		p=getport();
		dbPath=getDbPath();
		dbUsername=getDbUsername();
		dbPassword=getDbPassword();
	
		if(p.trim().isEmpty()) {
			lblError.setText("You must enter a port number");
					
		}
		else
			if(dbPath.trim().isEmpty()) {
				lblError.setText("You must enter database path");
						
			}
			else
				if(dbUsername.trim().isEmpty()) {
					lblError.setText("You must enter database username");
							
				}
				else
					if(dbPassword.trim().isEmpty()) {
						lblError.setText("You must enter database password");
								
					}
			else
		{  
			if (ev == null || !ev.isListening()) {
				String tempport = p;
				ev = new EchoServer(Integer.parseInt(tempport), this,dbPath,dbUsername,dbPassword);
				ev.listen();
				lblServer.setText("Online");
				btnConnect.setText("Disconnect");
				lblError.setText("");

			} else {
				ev.close();
				lblServer.setText("Offline");
				btnConnect.setText("Connect");
				labelServerIP.setText("");

			}

			
		}
	}
	
	
}
