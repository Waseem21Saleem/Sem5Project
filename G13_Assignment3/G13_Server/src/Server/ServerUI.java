package Server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Vector;
import gui.ServerInfoController;
import Server.EchoServer;

public class ServerUI extends Application {
	final public static int DEFAULT_PORT = 5555;
	public static EchoServer sv;


	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		ServerInfoController aFrame = new ServerInfoController(); // create ServerInfos
		aFrame.start(primaryStage);
	}
	
	@Override
	public void stop() throws Exception {
		closeServer();
		System.exit(0);
	}
	
	public static boolean closeServer() {
		try {
			if (sv!=null)
				sv.close();
			 
		} 
		catch(Exception ex) 
        {
          System.out.println("ERROR - Could not listen for clients!");
          return false;
        }
		return true;
	}
	

	
	

}
