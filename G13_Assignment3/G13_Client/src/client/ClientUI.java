package client;
import javafx.application.Application;

import javafx.stage.Stage;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Vector;
import gui.MainFrameController;
import gui.EditOrderController;
import client.ClientController;

public class ClientUI extends Application {
	public static ClientController chat; //only one instance
	
	public static void main( String args[] ) throws Exception
	   { 
		    launch(args);  
	   } // end main
	

	@Override
	public void stop() {
		System.exit(0);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		MainFrameController aFrame = new MainFrameController();
		aFrame.start(primaryStage);
			  		
		
		
	
	}
	

	
	
}
