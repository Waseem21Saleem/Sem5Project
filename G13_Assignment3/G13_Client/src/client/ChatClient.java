// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import client.*;
import common.ChatIF;
import common.MyFile;
import common.OpenGUI;
import gui.EditOrderController;
import gui.VisitorHomePageController;
import javafx.stage.Stage;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.Report;
import logic.Request;
import logic.UsageVisitingReport;
import logic.User;
import logic.WaitingListEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 /** The interface type variable for client UI. */
	  ChatIF clientUI;
	  
	  /** Flag indicating if a response is awaited. */
	  public static boolean awaitResponse = false;
	  
	  /** List of orders. */
	  public static ArrayList<String> Orderslist;
	  
	  /** List of order information. */
	  public static ArrayList<String> OrderInfo;
	  
	  /** List of park names. */
	  public static ArrayList<String> parkNames;
	  
	  /** List of years. */
	  public static List<String> years;
	  
	  /** List of months. */
	  public static List<String> months;
	  
	  /** List of waiting list entries. */
	  public static ArrayList<WaitingListEntry> waitingListEntries;
	  
	  /** List of alternative orders. */
	  public static ArrayList<Order> alternativeOrders;
	  
	  /** List of requests. */
	  public static ArrayList<Request> requests;
	  
	  /** List of usage visiting reports. */
	  public static ArrayList<UsageVisitingReport> usageVisitingList;
	  
	  /** List of visiting reports. */
	  public static ArrayList<ArrayList<UsageVisitingReport>> visitingArrayList;
	  
	  /** The user object. */
	  public static User user;
	  
	  /** The order object. */
	  public static Order order;
	  
	  /** Error message. */
	  public static String error;
	  
	  /** Instance of OpenGUI. */
	  public static OpenGUI openGUI;
	  
	  /** The park object. */
	  public static Park park;
	  
	  /** The report object. */
	  public static Report report;

  //Constructors ****************************************************
  
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   * @throws IOException If an I/O error occurs.
   */
	 
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	Orderslist = new ArrayList<String>();
	OrderInfo = new ArrayList<String>();
	parkNames = new ArrayList<String>();
	months = new ArrayList<String>();
	for ( int i=0;i<12;i++)
		months.add(""+(i+1));
	waitingListEntries = new ArrayList<>();
	alternativeOrders = new ArrayList<Order>();
	usageVisitingList = new ArrayList<UsageVisitingReport>();
	visitingArrayList = new ArrayList<ArrayList<UsageVisitingReport>>();
	requests= new ArrayList<>();
	user=new User();
	order=new Order();
	error="";
	openGUI = new OpenGUI();
	park= new Park();
	report= new Report();

    this.openConnection();
  }

  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  @SuppressWarnings("unchecked")
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
	          case PARKNAMES:
	        	  parkNames = (ArrayList<String>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case RESERVATION:
	        	  error=(String)((Message) msg).getContent();
	        	  break;
	          case WAITINGLIST:
	        	  order=(Order)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case WAITINGLISTTABLE:
	        	  waitingListEntries=(ArrayList<WaitingListEntry>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case ALTERNATIVEDATE:
	        	  alternativeOrders=(ArrayList<Order>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case ORDERSNUMBERS:
	        	  Orderslist=(ArrayList<String>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case ORDERINFO:
	        	  order=(Order)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case PARKINFO:
	        	  park=(Park)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case GETINVOICE:
	        	  saveFileOnClient((MyFile)((Message) msg).getContent());
	        	  error="";
	        	  break;
	          case REQUESTSTABLE:
	        	  requests=(ArrayList<Request>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case REPORTINFO:
	        	  if (((Message) msg).getContent() instanceof Report)
	        		  report=(Report)((Message) msg).getContent();
	        	  else
	        		  usageVisitingList=(ArrayList<UsageVisitingReport>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          case VISITINGREPORT:
	        	  visitingArrayList=(ArrayList<ArrayList<UsageVisitingReport>>)((Message) msg).getContent();
	        	  error="";
	        	  break;
	          default:
	              System.out.println("Unknown action");
	              break;
			  
			  }
	  }
  }
  

  
  /**
   * This method saves a file recieved from the server to the client
   *
   * @param file The file from the server.
   */
  private void saveFileOnClient(MyFile file) {
		try {
			String name=file.getFileName();
			int i = file.getFileName().lastIndexOf("\\");
			if (i > 0) {
				name = file.getFileName().substring(i + 1);
			}
	        String path = getFolderPath().toString()+"\\" + name; 
	        FileOutputStream fileOutputStream = new FileOutputStream(path);
	        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);


	        bufferedOutputStream.write(file.getMybytearray(), 0, file.getSize());
	        bufferedOutputStream.flush();
	        bufferedOutputStream.close();

	        System.out.println("File saved successfully: " + file.getFileName());
	    } catch (IOException e) {
	        System.out.println("Error saving the file: " + file.getFileName());
	        e.printStackTrace();
	    }

	}
  

  /**
   * This method creates a folder called Invoices in the desktop then returns the path of it
   *
   * @return The path of the folder.
   */
  private Path getFolderPath() {
	// Get the path to the desktop directory
      String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
      // Create a folder name
      String folderName = "Invoices";

      // Create a Path object for the new folder
      Path folderPath = Paths.get(desktopPath, folderName);

      try {
          // Create the folder
          Files.createDirectories(folderPath);
          System.out.println("Folder created successfully at: " + folderPath.toString());
      } catch (Exception e) {
          System.err.println("Failed to create folder: " + e.getMessage());
      }
      
      return folderPath;
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
   * This method handles all data coming from the UI.
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

  /**
   * This method handles all data coming from the UI.
   *
   * @param text The text from the UI.
   */
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
