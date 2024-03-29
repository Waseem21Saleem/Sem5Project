// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import gui.ServerInfoController;
import logic.CurClient;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.Report;
import logic.Request;
import logic.User;
import mysqlConnection.RunnableSql;
import mysqlConnection.mysqlConnection;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */

public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  //final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * 
   */
	 /** List of hosts connected to the server */
	public static ArrayList<String> hostList = new  ArrayList<String>();
	
	/** Runnable SQL instance */
	private RunnableSql runnableSql;
	
	 /** MySQL connection */
	public mysqlConnection mysql;
	
	/** Server information controller */
	private ServerInfoController serv;
	
	/** Database path */
	private String dbPath;
	
	 /** Database username */
    private String dbUsername;

    /** Database password */
    private String dbPassword;

	 /**
	   * Constructs an instance of the EchoServer.
	   *
	   * @param port The port number to connect on.
	   * @param serv The ServerInfoController object associated with the server.
	   * @param dbPath The path to the database.
	   * @param dbUsername The username for accessing the database.
	   * @param dbPassword The password for accessing the database.
	   */
  public EchoServer(int port,ServerInfoController serv,String dbPath,String dbUsername,String dbPassword) 
  {
    super(port);
    this.serv=serv;
    this.dbPath=dbPath;
    this.dbUsername=dbUsername;
    this.dbPassword=dbPassword;
  }
  
  /**
   * Retrieves the path to the database.
   *
   * @return The database path.
   */
	private String getDbPath() {
		return dbPath;			
	}
	
	/**
	 * Retrieves the username for accessing the database.
	 *
	 * @return The database username.
	 */
	private String getDbUsername() {
		return dbUsername;			
	}
	
	/**
	 * Retrieves the password for accessing the database.
	 *
	 * @return The database password.
	 */
	private String getDbPassword() {
		return dbPassword;			
	}


  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient  (Object msg, ConnectionToClient client)
  {
	 
	 int flag=0;
	 User user;
	    System.out.println("Message received: " + msg + " from " + client);
	    if (msg instanceof String ) {
		    if(msg.toString().contains("ClientDisconnected")) {
		    			
		    			mysql.deleteHostInfo(client.getInetAddress().getHostAddress());
		    			
	
						}
	    }

	    else if (msg instanceof Message) {
    	    switch (((Message) msg).getActionType()) {
    	    case USERLOGIN: //visitor or guide login
    	    	user = (User) ((Message) msg).getContent();
    	    	msg=mysql.verifyVisitorLogin(user);
    	    	sendMsgToClient(msg,client);
                break;
    	    case WORKERLOGIN: //worker login
    	    	user = (User) ((Message) msg).getContent();
    	    	msg=mysql.verifyWorkerLogin(user);
    	    	sendMsgToClient(msg,client);
                break;
    	    case PARKNAMES:  //returns park names
    	    	msg=mysql.getParks();
    	    	sendMsgToClient(msg,client);
                break;
    	    case RESERVATION:  //checks reservation
    	    	msg=mysql.insertReservation((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break;
    	    case ORDERSNUMBERS: //returns the orders numbers to the specific user
    	    	msg=mysql.getOrdersNumbers((User) ((Message) msg).getContent());    	    	
    	    	sendMsgToClient(msg,client);
                break; 
    	    case ORDERINFO:  //returns order info
    	    	msg=mysql.getOrderInfo((Order) ((Message) msg).getContent());
    	    	
    	    	sendMsgToClient(msg,client);
                break; 
    	    case UPDATEORDER: //updates order
    	    	msg=mysql.updateReservation((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case DELETEORDER: //cancels order as a "cancelled manually"
    	    	msg=mysql.deleteOrder((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case WAITINGLIST: //adds order to waiting list
    	    	mysql.addOrderToWaitingList((Order) ((Message) msg).getContent());
                break; 
    	    case WAITINGLISTTABLE:  //returns waiting list table
    	    	msg=mysql.getWaitingListTable((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case ALTERNATIVEDATE:  //returns 5 alternative dates for available orders
	    	    msg=mysql.getAlternativeDate((Order) ((Message) msg).getContent());
		    	sendMsgToClient(msg,client);
	            break; 
    	    case AVAILABLEPLACES: //returns available places at the current time
    	    	msg=mysql.getAvailablePlaces((String) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break;
    	    case ADDUNPLANNED: //adds unplanned visitors to the park
    	    	mysql.addUnplannedOrder((Order) ((Message) msg).getContent());
    	    	break;
    	    case GETINVOICE: //creates pdf invoice
    	    	msg=mysql.generateInvoice((String) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case APPROVEEXIT:  //approve exit of a specific order number
    	    	msg=mysql.approveExit((String) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case PARKINFO:  //returns park info to client
    	    	msg=mysql.getParkInfo((String) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case NEWREQUEST:  //park manager created new request
    	    	mysql.insertRequest((Park) ((Message) msg).getContent());
    	    	break;
    	    case REQUESTSTABLE: //gets the requests table to the department manager
    	    	msg=mysql.getRequestsTable();
    	    	sendMsgToClient(msg,client);
                break; 
    	    case APPROVEREQUEST: //department approved park manager request
    	    	mysql.updateParkInfo((Request) ((Message) msg).getContent());
    	    	msg=mysql.updateRequest((Request) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case REJECTREQUEST: //department rejected park manager request
    	    	msg=mysql.updateRequest((Request) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case CANCELLATIONREPORT: //creates cancellation report
    	    	msg=mysql.CreateCancellationReport((Report) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case TOTALVISITORSREPORT://creates total visitor report
    	    	msg=mysql.CreateTotalVisitorsReport((Report) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case USAGEREPORT: //creates usage report
    	    	msg=mysql.createUsageReport((Report) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case VISITINGREPORT: //creates visiting report
    	    	msg=mysql.createVisitingReport((Report) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
    	    	break;
    	    case REPORTINFO:  //sends report info to client
    	    	Report report = (Report) ((Message) msg).getContent();
    	    	System.out.println(report.getReportType());
    	    	System.out.println(report.getParkName());
    	    	if (report.getReportType().equals("Cancellation report"))
    	    		msg=mysql.getCancellationReport(report);
    	    	else if (report.getReportType().equals("Total visitors report"))
    	    		msg=mysql.getTotalVisitorsReport(report);
    	    	else if (report.getReportType().equals("Usage report"))
    	    		msg=mysql.getUsageReport(report);
    	    	else 
    	    		msg=mysql.getVisitingReport(report);
    	    	sendMsgToClient(msg,client);
                break; 
    	    case CHANGEROLE: //change role from visitor to guide
    	    	msg=mysql.updateRoleToGuide((User) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
            case LOGOUT: //user logout
            	user = (User) ((Message) msg).getContent();
    	    	mysql.flipIsLogged(user);
                break;
            case CLIENTDISCONNECTED:  //client disconnects
            	mysql.deleteHostInfo(client.getInetAddress().getHostAddress());
        		serv.updateWaitingListTable(mysql.getClients());
        		break;
            default:
                System.out.println("Unknown action");
                break;
    	    
    	    
    	    
    	    
    	    }
    	    
    	}
	    
	    

	    if (flag!=1) {
			System.out.println("");
			this.sendMsgToClient("", client);
			//this.sendToAllClients("");
		} 
  }
   
  /**
   * Sends a message to a connected client.
   *
   * @param msg The message to send.
   * @param client The connection to the client.
   */
  public void sendMsgToClient (Object msg,ConnectionToClient client) {
	  
	  try {
			client.sendToClient(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {	
    System.out.println ("Server listening for connections on port " + getPort());
    mysql= new mysqlConnection(getDbPath(),getDbUsername(),getDbPassword());
    runnableSql = new RunnableSql();
    runnableSql.setConnection(mysql.getConn());
    // Create a Thread object and pass MyTaskRunnable to its constructor
    Thread thread = new Thread(runnableSql);

    // Start the thread
    thread.start();
    

  }

	
	@Override
	protected void clientConnected(ConnectionToClient client) {
	    // Handle client connection

		mysql.insertClientInfo(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName());
		serv.updateWaitingListTable(mysql.getClients());
	}
	
	@Override
	protected void clientDisconnected(ConnectionToClient client) {
	    // Handle client disconnection

		
	}
	
	

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()  {
    System.out.println ("Server has stopped listening for connections.");
  }  
}
//End of EchoServer class
