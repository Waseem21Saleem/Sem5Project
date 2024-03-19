// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import common.OpenGUI;
import gui.EditOrderController;
import gui.VisitorHomePageController;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.User;

import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  public static boolean awaitResponse = false;
  public static ArrayList<String> Orderslist;
  public static ArrayList<String> OrderInfo;
  public static User user;
  public static String error;
  public static OpenGUI openGUI;

  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
	 
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	Orderslist = new ArrayList<String>();
	OrderInfo = new ArrayList<String>();
	user=new User();
	error="";
	openGUI = new OpenGUI();

    this.openConnection();
  }

  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
	  System.out.println("--> handleMessageFromServer");
     
	  awaitResponse = false;
	  String st;
	  st=msg.toString();
	  System.out.println("Message received: " + st );
	  if (msg instanceof ArrayList) {
		  ArrayList<String> orders = (ArrayList<String>)msg;
		  if (orders.get(5).toString().contains("@")) {
			  OrderInfo=orders;
		  }
		  else {
			  Orderslist=orders;
		  }
		  
	  }
	  else if (msg instanceof Message) {
		  switch (((Message) msg).getActionType()) {
	  	    case LOGINSUCCESS:
	  	    	  user = (User) ((Message) msg).getContent();
	  	    	  error="";
	              break;
	          case ERROR:
	              error= (String)((Message) msg).getContent();
	              break;
	          default:
	              System.out.println("Unknown action");
	              break;
			  
			  }
	  }
  }
  

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(String message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      clientUI.display("Could not send message to server: Terminating client."+ e);
      quit();
    }
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(Message message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      clientUI.display("Could not send message to server: Terminating client."+ e);
      quit();
    }
  }
  
  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  
  public void handleMessageFromClientUI(ArrayList<String> message)  
  {
    try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(message);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      clientUI.display("Could not send message to server: Terminating client."+ e);
      quit();
    }
  }
  /*public void start(Stage primaryStage) throws Exception {
	  this.orderForm = new OrderFormController(); // create OrderForm
	  this.orderForm.start(primaryStage);
  }*/
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }

public void handleMessageFromClientUI(String[] text) {
	try
    {
    	openConnection();//in order to send more than one message
       	awaitResponse = true;
    	sendToServer(text);
		// wait for response
		while (awaitResponse) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
    catch(IOException e)
    {
    	e.printStackTrace();
      clientUI.display("Could not send message to server: Terminating client."+ e);
      quit();
    }
	
}
}
//End of ChatClient class
