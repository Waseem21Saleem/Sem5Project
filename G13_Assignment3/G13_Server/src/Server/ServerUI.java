package Server;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Vector;
import gui.ServerInfoController;
import Server.EchoServer;

/**
 * ServerUI class represents the user interface for managing the server.
 * It extends the JavaFX Application class to provide GUI functionality.
 * 
 * This class allows starting and stopping the server, as well as managing the server's graphical interface.
 * */
public class ServerUI extends Application {
	/** Default port number for the server. */
    final public static int DEFAULT_PORT = 5555;

    /** Static instance of the EchoServer. */
    public static EchoServer sv;


	/**
     * The entry point for the server application.
     * 
     * @param args Command-line arguments.
     * @throws Exception if an error occurs during server initialization.
     */
	public static void main( String args[] ) throws Exception
	   {   
		 launch(args);
	  } // end main
	
	
	/**
     * Initializes the server UI.
     * 
     * @param primaryStage The primary stage for the server UI.
     * @throws Exception if an error occurs during initialization.
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub				  		
		ServerInfoController aFrame = new ServerInfoController(); // create ServerInfos
		aFrame.start(primaryStage);
	}
	
	/**
     * Stops the server when the application is closed.
     * 
     * @throws Exception if an error occurs while stopping the server.
     */
	@Override
	public void stop() throws Exception {
		closeServer();
		System.exit(0);
	}
	
	
	 /**
     * Closes the server and stops listening for client connections.
     * 
     * @return true if the server was successfully closed, otherwise false.
     */
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
