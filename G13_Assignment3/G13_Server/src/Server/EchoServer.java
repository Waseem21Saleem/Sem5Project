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
import logic.User;
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
	public static ArrayList<String> hostList = new  ArrayList<String>();

	public mysqlConnection mysql;
	private ServerInfoController serv;
	private String dbPath,dbUsername,dbPassword;

  public EchoServer(int port,ServerInfoController serv,String dbPath,String dbUsername,String dbPassword) 
  {
    super(port);
    this.serv=serv;
    this.dbPath=dbPath;
    this.dbUsername=dbUsername;
    this.dbPassword=dbPassword;
  }
  
	private String getDbPath() {
		return dbPath;			
	}
	private String getDbUsername() {
		return dbUsername;			
	}
	private String getDbPassword() {
		return dbPassword;			
	}


  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * @param 
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
    	    case USERLOGIN:
    	    	user = (User) ((Message) msg).getContent();
    	    	msg=mysql.verifyVisitorLogin(user);
    	    	sendMsgToClient(msg,client);
                break;
    	    case WORKERLOGIN:
    	    	user = (User) ((Message) msg).getContent();
    	    	msg=mysql.verifyWorkerLogin(user);
    	    	sendMsgToClient(msg,client);
                break;
    	    	
    	    case PARKNAMES:
    	    	msg=mysql.getParks();
    	    	sendMsgToClient(msg,client);
                break;
    	    case RESERVATION:
    	    	msg=mysql.checkReservation((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break;
    	    case ORDERSNUMBERS:
    	    	msg=mysql.getOrdersNumbers((User) ((Message) msg).getContent());    	    	
    	    	sendMsgToClient(msg,client);
                break; 
    	    case ORDERINFO:
    	    	msg=mysql.getOrderInfo((Order) ((Message) msg).getContent());
    	    	
    	    	sendMsgToClient(msg,client);
                break; 
    	    case UPDATEORDER:
    	    	msg=mysql.checkUpdatedReservation((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case DELETEORDER:
    	    	msg=mysql.deleteOrder((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case WAITINGLIST:
    	    	mysql.addOrderToWaitingList((Order) ((Message) msg).getContent());
                break; 
    	    case WAITINGLISTTABLE:
    	    	msg=mysql.getWaitingListTable((Order) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
    	    case CHANGEROLE:
    	    	msg=mysql.updateRoleToGuide((User) ((Message) msg).getContent());
    	    	sendMsgToClient(msg,client);
                break; 
            case LOGOUT:
            	user = (User) ((Message) msg).getContent();
    	    	mysql.flipIsLogged(user);
                break;
            default:
                System.out.println("Unknown action");
                break;
    	    
    	    
    	    
    	    
    	    }
    	    
    	}
	    
	    

	    if (flag!=1) {
			System.out.println("");
			this.sendToAllClients("");
		} 
  }
   
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
    

  }

	
	@Override
	protected void clientConnected(ConnectionToClient client) {

		mysql.insertClientInfo(client.getInetAddress().getHostAddress(), client.getInetAddress().getHostName());
	}
	
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		
		
	}
	
	
	public void refreshHostList () {
		hostList = mysql.getHostNames(hostList);
	}
	
	public void getHostInfo (String selectedValue, CurClient curClient) {
		mysql.getHostInfo(selectedValue,curClient);

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
