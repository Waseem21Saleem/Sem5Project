package gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.CurClient;
import logic.Order;
import mysqlConnection.mysqlConnection;
import ocsf.server.ConnectionToClient;

public class ServerInfoController implements Initializable {
	
	private EchoServer ev;
	@FXML
	private Label lblConnection;
	@FXML
	private Label lblIp;
	@FXML
	private Label lblHost;
	@FXML
	private Label lblServer;
	@FXML
	private Button btnclose=null;
	@FXML
	private Button btnConnect;
	@FXML
	private ComboBox cmbClient;
	
	static CurClient curClient;
	
	public static ArrayList<String> hostList= new ArrayList<String>();;
	
	ObservableList<String> list;
	@FXML
	private TextField portxt;
	
	private String getport() {
		return portxt.getText();			
	}
	
	/**
	   * This method loads the host names ( into the server GUI ComboBox )
	   * and loads the client info whenever a combobox selected value changes
	   
	   */
	public void refresh() {
		
		if (ev != null) {
			ArrayList<String> hostList = new ArrayList<String>();
			ev.mysql.getHostNames(hostList);

			System.out.println (hostList.toString());
			this.list = FXCollections.observableArrayList(hostList);
		}
		cmbClient.getItems().clear();
		cmbClient.setItems(list);
		cmbClient.setOnAction(event -> {
					
					String selectedValue = (String) cmbClient.getValue();
					if (cmbClient.getValue()!=null) {
						curClient =   new CurClient();
						ev.getHostInfo(selectedValue, curClient);
						this.lblIp.setText(curClient.getIp());
						this.lblHost.setText(curClient.getHost());
						if (curClient.getConnection())
							this.lblConnection.setText("Client connected");
						else
							this.lblConnection.setText("Client disconnected");
					} 
					else
					{
						this.lblIp.setText("");
						this.lblHost.setText("");
						this.lblConnection.setText("Client disconnected");
					}
				        });

	}
	
	public void ClientDisconnected( ) {
		this.lblConnection.setText("Client disconnected");

	}
	
	
	
	/**
	   * This method sets the curClient as the parameters it takes
	   *
	   *@param ip
	   *@param host
	   *@param connection
	   
	   */	
	public static void setCurClient (String ip,String host, boolean connection) {
		curClient = new CurClient(ip,host,connection);
		
	}
	
	/**
	   * This method closes the gui when close button pressed
	   *
	   *@param event, the Close button
	   
	   */	
	public void closeBtn(ActionEvent event) throws Exception {
		System.exit(0);
		
	}
	
	

	
	
	
	/**
	   * This method starts the current GUI
	   *
	   *@param arg0
	   *@param arg1

	   
	   */	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		//this.lblConnection.setText("Client not connected");
		 
	    }
	/**
	   * This method runs the main fx Gui
	   *
	   * @param primaryStage from the server
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
	   * This method connects the server to the selected port
	   *
	   * @param event, when the user presses Connect
	   */
	public void connectServer(ActionEvent event) throws Exception {
		String p;
		
		p=getport();
		if(p.trim().isEmpty()) {
			System.out.println("You must enter a port number");
					
		}
		else
		{
			if (ev == null || !ev.isListening()) {
				String tempport = p;
				ev = new EchoServer(Integer.parseInt(tempport), this);
				ev.listen();
				lblServer.setText("Online");
				btnConnect.setText("Disconnect");

			} else {
				ev.close();
				lblServer.setText("Offline");
				btnConnect.setText("Connect");
			}

			
		}
	}
	
	
}
