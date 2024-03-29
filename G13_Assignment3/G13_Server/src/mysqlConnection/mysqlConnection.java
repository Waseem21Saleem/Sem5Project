package mysqlConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.*; 
import java.lang.*; 
import java.util.*;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPage;

import common.MyFile;
import emailController.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.CurClient;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.Report;
import logic.Request;
import logic.UsageVisitingReport;
import logic.User;
import logic.WaitingListEntry;

/**
 * The mysqlConnection class establishes a connection to a MySQL database and provides methods for verifying user logins
 * and other functions.
 */
public class mysqlConnection {
	
	
	private static Connection conn;

	/**
     * Constructs a mysqlConnection object and connects to the specified database.
     *
     * @param dbPath      the path to the database
     * @param dbUsername  the database username
     * @param dbPassword  the database password
     */
	public mysqlConnection(String dbPath,String dbUsername, String dbPassword)
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        }
        
		try {
			conn = DriverManager.getConnection(dbPath,dbUsername,dbPassword);
	        System.out.println("SQL connection succeed");

		} catch (SQLException ex) {
			 System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         System.out.println("SQL connection failed");

		}

   	}
	
	
	 /**
     * Returns the connection to the MySQL database.
     *
     * @return the database connection
     */
	public Connection getConn() {
		return conn;
	}


	/**
     * Verifies the login credentials of a worker.
     *
     * @param user the User object containing login details
     * @return a Message object indicating login success or failure
     */
	public Message verifyWorkerLogin(User user)
	{
		Message msg;
		String error;
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.users WHERE UserId=? AND Password=?");
			preparedStatement.setString(1, user.getId()); // 1-indexed parameter position
			preparedStatement.setString(2, user.getPassword()); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			if (rs.next()) {
            	
            	if (rs.getBoolean("IsLogged"))
            	{	error = "This user is already logged in.";
            		msg = new Message (Message.ActionType.ERROR,error);
            		return msg;
            	}
            	else {
            		flipIsLogged(user);  //Update isLogged to true
            		user.setFullName(rs.getString("FullName"));
		            user.setEmail(rs.getString("Email"));
		            user.setPhoneNumber(rs.getString("PhoneNumber"));
		            user.setLogged(true);
		            user.setUserPermission(rs.getString("UserPermission"));
		            user.setParkName(rs.getString("ParkName"));
            	}}
            else {
            	error = "Invalid ID or password";
            	msg = new Message (Message.ActionType.ERROR,error);
        		return msg;
            }
            		
            

	            
            
			rs.close();
			preparedStatement.close();
			
			
		} catch (SQLException e) { }
		msg = new Message (Message.ActionType.LOGINSUCCESS,user);
        return msg;	}


	/**
     * Verifies the login credentials of a visitor and creates a new user record if not already present.
     *
     * @param user the User object containing login details
     * @return a Message object indicating login success or failure
     */
	public Message verifyVisitorLogin(User user)
	{
		Message msg;
		String error;
		try 
		{	
			
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.users WHERE UserId=?");
			preparedStatement.setString(1, user.getId()); // 1-indexed parameter position
			
			try {
				// Execute the query
				ResultSet rs = preparedStatement.executeQuery();
				 // Process the result set
				// Process result set
	
	            if (rs.next()) {
	                	
	                	if (rs.getBoolean("IsLogged"))
	                	{	error = "This user is already logged in.";
	                		msg = new Message (Message.ActionType.ERROR,error);
	                		return msg;
	                	}
	                	else {
	                	
	                	flipIsLogged(user);  //Update isLogged to true
	                	user.setPassword(rs.getString("Password"));
	                	user.setFullName(rs.getString("FullName"));
	                    user.setEmail(rs.getString("Email"));
	                    user.setPhoneNumber(rs.getString("PhoneNumber"));
	                    user.setUserPermission(rs.getString("UserPermission"));
	                    user.setLogged(true);
	                    
	        			
	                    
	                	}
                } else {
                	try {

                		// SQL INSERT statement
                        String sql = "INSERT INTO g13.users (UserId, IsLogged, UserPermission) VALUES (?, ?, ?)";

                        PreparedStatement pstmt = conn.prepareStatement(sql);

                       // Set values for placeholders (?, ?, ?)
                       pstmt.setString(1, user.getId());
                       pstmt.setBoolean(2, true);
                       pstmt.setString(3, "VISITOR");

                       // Execute the INSERT statement
                       pstmt.executeUpdate();

                      
                   } catch (SQLException e) {
                       error = "Error: can't Execute the given prepared statement";
                       msg = new Message (Message.ActionType.EXECUTEERROR,error);
                       return msg;
                   }
                }
	            rs.close();    
            }
			catch (SQLException e) {
                error = "Error: can't Execute the given prepared statement";
                msg = new Message (Message.ActionType.EXECUTEERROR,error);
                return msg;
            }
			
			preparedStatement.close();
			
		} catch (SQLException e) {
			error = "Error: can't Execute the given prepared statement";
            msg = new Message (Message.ActionType.EXECUTEERROR,error);
            return msg;}
			
		
		msg = new Message (Message.ActionType.LOGINSUCCESS,user);
        return msg;
	}
	

	/**
 * This method flips the IsLogged status of a user in the database
 *
 *
 * @param user the User object whose IsLogged status is to be updated
 */
	public void flipIsLogged(User user)
	{
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.users SET IsLogged=? WHERE UserId = ?");
			 	if (user.isLogged())
			 		ps.setBoolean(1,false);
			 	else
			 		ps.setBoolean(1,true);
	            ps.setString(2,user.getId());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
	}
	
	
	/**
	 * Retrieves the list of park names from the database.
	 *
	 * @return a Message object containing the list of park names
	 */
	public Message getParks()
	{
		Message msg;
		ArrayList<String> parksList= new ArrayList<String>();
		try 
		{	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ParkName FROM g13.parks");
	 		while(rs.next())
	 		{
	 			
	 		
	 			parksList.add(rs.getString("ParkName"));
				
			} 
	 		
			rs.close();
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.PARKNAMES,parksList);
		return msg;
		
	}
	
	/**
	 * Retrieves the list of order numbers for a specific user from the database.
	 *
	 * @param user the User object for whom the order numbers are retrieved
	 * @return a Message object containing the list of order numbers
	 */
	public Message getOrdersNumbers(User user)
	{	
		Message msg;
		ArrayList<String> ordersList= new ArrayList<String>();
		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT OrderNumber FROM g13.orders WHERE VisitorId = ? AND OrderStatus = ?");
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, "processing");
			ResultSet rs = preparedStatement.executeQuery();
	 		while(rs.next())
	 		{
	 			
	 		
	 			ordersList.add(rs.getString("OrderNumber"));
				
			} 
	 		
			rs.close();
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.ORDERSNUMBERS,ordersList);
		return msg;
		
	}
	


	/**
 * Retrieves order details from the database based on the provided order number.
 *
 * @param order the Order object containing the order number
 * @return a Message object containing the order details
 */
	public static Message getOrderInfo(Order order)
	{
		Message msg;

		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE OrderNumber=?");
			preparedStatement.setString(1, order.getOrderNum()); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
          rs.next();
	        //order = new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),rs.getString("TimeOfVisit"),rs.getString("NumberOfVisitors"),rs.getString("TelephoneNumber"),rs.getString("Email"))  ; 
          order.setParkName(rs.getString("ParkName"));
          order.setVisitorId(rs.getString("VisitorId"));
          order.setDate(rs.getString("Date"));
          order.setTime(rs.getString("Time"));
          order.setAmountOfVisitors(rs.getString("NumberOfVisitors"));
          order.setTelephone(rs.getString("PhoneNumber"));
          order.setEmail(rs.getString("Email"));
          order.setVisitorType(rs.getString("VisitorType"));
          order.setExitTime(rs.getString("ExitTime"));

	            
          
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.ORDERINFO,order);
		return msg;
	}
	
	
	/**
 * Updates order information in the database based on the provided Order object.
 *
 * @param order the Order object containing updated information
 */
	public void updateOrderInfo(Order order)
	{
		PreparedStatement ps;
		try {

			 	ps = conn.prepareStatement("UPDATE g13.orders SET ParkName=?,Date=?,Time=?,NumberOfVisitors=?,Email=?,PhoneNumber=?,ExitTime=?,PayStatus=?,TotalCost=? WHERE OrderNumber = ?");
			 	ps.setString(1,order.getParkName());
	            ps.setString(2,order.getDate());
	            ps.setString(3,order.getTime());
	            ps.setString(4,order.getAmountOfVisitors());
	            ps.setString(5,order.getEmail());
	            ps.setString(6,order.getTelephone());
	            ps.setString(7,order.getExitTime());
	            ps.setString(8,order.getPayStatus());
	            ps.setString(9,order.getTotalCost());
	            ps.setString(10,order.getOrderNum());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {System.out.println("Error");	}
	}
	


	/**
 * Cancels an order in the database and updates its status to "cancelled manually".
 *
 * @param order the Order object to be cancelled
 * @return a Message object indicating the cancellation status
 */
	public Message deleteOrder(Order order) {
		Message msg ;
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.orders SET OrderStatus=? WHERE OrderNumber = ?");
			 	ps.setString(1,"cancelled manually");
	            ps.setString(2,order.getOrderNum());
	            ps.executeUpdate();
	            checkWaitingList(order);


	    
	           
	            
		} catch (SQLException e) {	}
        msg = new Message (Message.ActionType.ERROR,"Cancelled order successfully");
		return msg;
		
		
	}
	
	/**
 * Inserts a new order into the database based on the provided Order object.
 *
 * @param order the Order object containing the new order details
 */
	public static void insertOrder(Order order) {
		try {
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.orders (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, PhoneNumber, Email, VisitorType, ExitTime, OrderStatus, PayStatus, TotalCost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (order.getOrderNum().equals("") || order.getOrderNum().equals(null))
            	order.setOrderNum(Integer.toString(getRandomOrderNumber()));
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,order.getOrderNum() );
           pstmt.setString(2, order.getParkName());
           pstmt.setString(3, order.getVisitorId());
           pstmt.setString(4, order.getDate());
           pstmt.setString(5, order.getTime());
           pstmt.setString(6, order.getAmountOfVisitors());
           pstmt.setString(7, order.getTelephone());
           pstmt.setString(8, order.getEmail());
           pstmt.setString(9, order.getVisitorType());
           pstmt.setString(10, order.getExitTime());
           pstmt.setString(11, order.getOrderStatus());
           pstmt.setString(12, order.getPayStatus());
           pstmt.setString(13, order.getTotalCost());
                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
	}
	
	/**
	 * Adds an order to the waiting list in the database based on the provided Order object.
	 *
	 * @param order the Order object to be added to the waiting list
	 */
	public void addOrderToWaitingList(Order order) {
		try {
			
			// Original time string
	        String timeString = order.getTime();
	        Message msg2 = getParkInfo(order.getParkName());
	        Park park = (Park) ((Message) msg2).getContent();
	        // Number of hours to add
	        int maxStay = Integer.parseInt(park.getMaxStay());
	        // Parse the time string into LocalTime
	        LocalTime time = LocalTime.parse(timeString);
	        // Add x hours
	        LocalTime newTime = time.plusHours(maxStay);
	        // Format the new time back into a string
	        String result = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));
			order.setExitTime(result);
			int placement=getPlacement(order);
			// SQL INSERT statement
            String sql = "INSERT INTO g13.waitinglist (Placement,OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, PhoneNumber, Email, VisitorType, ExitTime, OrderStatus, PayStatus, TotalCost) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
            if (order.getOrderNum()=="" || order.getOrderNum()==null)
            	order.setOrderNum(Integer.toString(getRandomOrderNumber()));
         // Prepare a statement with a placeholder
			
            //System.out.println(placement+" "+order.getOrderNum()+" "+order.getParkName()+order.getVisitorId()+order.getDate()+order.getTime()+);
           
            // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,String.valueOf(placement) );
           pstmt.setString(2,order.getOrderNum() );
           pstmt.setString(3, order.getParkName());
           pstmt.setString(4, order.getVisitorId());
           pstmt.setString(5, order.getDate());
           pstmt.setString(6, order.getTime());
           pstmt.setString(7, order.getAmountOfVisitors());
           pstmt.setString(8, order.getTelephone());
           pstmt.setString(9, order.getEmail());
           pstmt.setString(10, order.getVisitorType());
           pstmt.setString(11, order.getExitTime());
           pstmt.setString(12, order.getOrderStatus());
           pstmt.setString(13, order.getPayStatus());
           pstmt.setString(14, order.getTotalCost());
                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
	}
	
	
	/**
	 * Calculates the placement of an order in the waiting list based on park, date, and time criteria.
	 *
	 * @param order the Order object for which placement is calculated
	 * @return the placement position of the order in the waiting list
	 */
	public int getPlacement(Order order) {
		int placement=1;
		
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			 if (rs.next()) {
				 placement += rs.getInt(1); // Get the value of the first column in the result set
	            }
			rs.close();
			preparedStatement.close();
			// Prepare a statement with a placeholder
			preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getExitTime());
			preparedStatement.setString(3, order.getExitTime());
			preparedStatement.setString(4, order.getDate());
			// Execute the query
			rs = preparedStatement.executeQuery();
			 // Process the result set
			 if (rs.next()) {
				 placement += rs.getInt(1); // Get the value of the first column in the result set
	            }
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		
		
		return placement;
		
		
	}
	
	/**
	 * This method checks the waiting list for any orders matching the given order's park, time, exit time, and date. 
	 * If any matching orders are found, they are processed to make reservations and removed from the waiting list. 
	 * Additionally, notification emails are sent to the respective visitors.
	 *
	 * @param order The order for which the waiting list needs to be checked.
	 */
	public static void checkWaitingList (Order order) {
		Message msg;
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			 while (rs.next()) {
				Order checkOrder= new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),
            		    rs.getString("VisitorId"),rs.getString("VisitorType")
            		    ,rs.getString("Date"),rs.getString("Time")
            		    ,rs.getString("NumberOfVisitors")
            		    ,rs.getString("PhoneNumber")
            		    ,rs.getString("Email") 
            		    ,rs.getString("ExitTime")
            		    ,rs.getString("TotalCost")
            		    ,rs.getString("PayStatus")
            		    ,rs.getString("OrderStatus"));
				msg=insertReservation(checkOrder);
				if (msg.getActionType().equals(Message.ActionType.RESERVATION)) {
					deleteOrderFromWaitingList(checkOrder);
                    EmailSender.sendMessage(checkOrder.getEmail(),checkOrder.getOrderNum(),checkOrder.getParkName(),checkOrder.getDate(),checkOrder.getTime(),checkOrder.getAmountOfVisitors(),"WaitingList");

				}
	        } 
			rs.close();
			preparedStatement.close();
			// Prepare a statement with a placeholder
			preparedStatement = conn.prepareStatement("SELECT * FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getExitTime());
			preparedStatement.setString(3, order.getExitTime());
			preparedStatement.setString(4, order.getDate());
			// Execute the query
			rs = preparedStatement.executeQuery();
			 // Process the result set
			 while (rs.next()) {
				Order checkOrder= new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),
           		    rs.getString("VisitorId"),rs.getString("VisitorType")
           		    ,rs.getString("Date"),rs.getString("Time")
           		    ,rs.getString("NumberOfVisitors")
           		    ,rs.getString("PhoneNumber")
           		    ,rs.getString("Email") 
           		    ,rs.getString("ExitTime")
           		    ,rs.getString("TotalCost")
           		    ,rs.getString("PayStatus")
           		    ,rs.getString("OrderStatus"));
				msg=insertReservation(checkOrder);
				if (msg.getActionType().equals(Message.ActionType.RESERVATION)) {
					deleteOrderFromWaitingList(checkOrder);
                    EmailSender.sendMessage(checkOrder.getEmail(),checkOrder.getOrderNum(),checkOrder.getParkName(),checkOrder.getDate(),checkOrder.getTime(),checkOrder.getAmountOfVisitors(),"WaitingList");

				}
	        }
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
	}
	
	
	/**
	 * Deletes the specified order from the waiting list in the database.
	 *
	 * @param order The order to be deleted from the waiting list.
	 */
	public static void deleteOrderFromWaitingList(Order order) {
		 // SQL DELETE statement
        String sql = "DELETE FROM g13.waitinglist WHERE OrderNumber = ?";
        
        try {
                PreparedStatement pstmt = conn.prepareStatement(sql) ;
                pstmt.setString(1, order.getOrderNum());

               // Execute the DELETE statement
               pstmt.executeUpdate();
               
           } catch (SQLException e) {
               e.printStackTrace();
           }
	}
	
	
	/**
	 * Retrieves the waiting list entries from the database matching the specified order's park, time, exit time, and date.
	 * Constructs a Message object containing the waiting list table entries.
	 *
	 * @param order The order for which the waiting list entries need to be retrieved.
	 * @return A Message object containing the waiting list table entries.
	 */
	public Message getWaitingListTable(Order order) {
		Message msg;
		 // Create an ArrayList to store WaitingListEntry objects
        ArrayList<WaitingListEntry> waitingListEntries = new ArrayList<>();
		try {

			PreparedStatement preparedStatement = conn.prepareStatement("SELECT Placement, Time, ExitTime, Date, NumberOfVisitors FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	waitingListEntries.add(new WaitingListEntry(
            		    rs.getString("Placement"),
            		    rs.getString("Time"),
            		    rs.getString("ExitTime"),
            		    rs.getString("Date"),
            		    rs.getString("NumberOfVisitors")
            		));
            	
           
	        }
            preparedStatement = conn.prepareStatement("SELECT Placement, Time, ExitTime, Date, NumberOfVisitors FROM g13.waitinglist WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? ");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getExitTime());
			preparedStatement.setString(3, order.getExitTime());
			preparedStatement.setString(4, order.getDate());
			// Execute the query
			rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	waitingListEntries.add(new WaitingListEntry(
            		    rs.getString("Placement"),
            		    rs.getString("Time"),
            		    rs.getString("ExitTime"),
            		    rs.getString("Date"),
            		    rs.getString("NumberOfVisitors")
            		));
            	
           
	        }
            } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    // Create and return a Message object containing the waiting list table entries
		msg = new Message (Message.ActionType.WAITINGLISTTABLE,waitingListEntries);
		return msg;
		
		
	}
	
	
	/**
	 * Retrieves alternative dates for reservation based on the provided order's park, time, exit time, and date.
	 * The function attempts to find alternative dates within a range of 5 iterations.
	 * If a suitable alternative date is found, it is added to the list of alternatives.
	 *
	 * @param order The order for which alternative dates need to be determined.
	 * @return A Message object containing the list of alternative Order objects.
	 */
	public Message getAlternativeDate (Order order) {
		//this function to make reservation
		Order tempOrder = order;
		// Store the current time from the order
		String curTime= order.getTime();
		Message msg;
		// ArrayList to store alternative orders
		ArrayList<Order> alternative = new ArrayList<Order>();
		int i=0;
		while (i<5)// Iterating over 5 alternatives
		{
			try 
			{	
				String timeStr = order.getTime(); // Example time string
		        // Define the formatter for hh:mm format
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		        // Parse the time string into a LocalTime object
		        LocalTime time = LocalTime.parse(timeStr, formatter);
		        // Define the LocalTime object for 15:00
		        LocalTime maxTime = LocalTime.of(15, 0);
		        // Check if the parsed time is before 15:00
		        if (time.isBefore(maxTime)) {
		        	// Add one hour to the time
		            LocalTime modifiedTime = time.plusHours(1);
		            // Format the modified time back into the string
		            String modifiedTimeStr = modifiedTime.format(formatter);
		            order.setTime(modifiedTimeStr);
		        } else { //Add 1 day to the date
		        	String dateString = order.getDate(); // Original date string
		            // Define the formatter for dd-MM-yy format
		            formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
		            // Parse the date string into a LocalDate object
		            LocalDate date = LocalDate.parse(dateString, formatter);
		            // Add one day to the date
		            LocalDate modifiedDate = date.plusDays(1);
		            // Format the modified date back into the string
		            String modifiedDateString = modifiedDate.format(formatter);
		            order.setDate(modifiedDateString);
		            order.setTime(curTime);
		            
		        }
				// Prepare a statement with a placeholder
				PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
				
				preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
				preparedStatement.setString(2, order.getTime());
				preparedStatement.setString(3, order.getTime());
				preparedStatement.setString(4, order.getDate());
				preparedStatement.setString(5, "cancelled automatically");
				preparedStatement.setString(6, "cancelled manually");
				// Execute the query
				ResultSet rs = preparedStatement.executeQuery();
				 // Process the result set
				// Process results
				int currentAmountOfVisitors = Integer.parseInt(order.getAmountOfVisitors());
	            while (rs.next()) {
	            	currentAmountOfVisitors += Integer.parseInt(rs.getString("NumberOfVisitors"));
	            }
			    rs.close();
				preparedStatement.close();
				// Prepare a statement with a placeholder
				preparedStatement = conn.prepareStatement("SELECT ReservedCapacity FROM g13.parks WHERE ParkName = ?");
				preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
				// Execute the query
				rs = preparedStatement.executeQuery();
				rs.next();
				int maxCapacity = Integer.parseInt(rs.getString("ReservedCapacity"));
				rs.close();
				preparedStatement.close();
				
				if (currentAmountOfVisitors<=maxCapacity)
				{
					// Original time string
			        String timeString = order.getTime();
			        Message msg2 = getParkInfo(order.getParkName());
			        Park park = (Park) ((Message) msg2).getContent();
			        // Number of hours to add
			        int maxStay = Integer.parseInt(park.getMaxStay());
			        // Parse the time string into LocalTime
			        time = LocalTime.parse(timeString);
			        // Add x hours
			        LocalTime newTime = time.plusHours(maxStay);
			        // Format the new time back into a string
			        String result = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));
					order.setExitTime(result);
					// Prepare a statement with a placeholder
					preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
					preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
					preparedStatement.setString(2, order.getExitTime());
					preparedStatement.setString(3, order.getExitTime());
					preparedStatement.setString(4, order.getDate());
					preparedStatement.setString(5, "cancelled automatically");
					preparedStatement.setString(6, "cancelled manually");
					// Execute the query
					rs = preparedStatement.executeQuery();
					 // Process the result set
					// Process results
					int exitTimeAmountOfVisitors = Integer.parseInt(order.getAmountOfVisitors());
		            while (rs.next()) {
		            	exitTimeAmountOfVisitors += Integer.parseInt(rs.getString("NumberOfVisitors"));
		            }
				    rs.close();
					preparedStatement.close();
					if (exitTimeAmountOfVisitors<=maxCapacity) {
						if (checkAlternativeOrder(order,alternative)) {
							alternative.add(order);
							i++;
							order=new Order(tempOrder.getParkName(),tempOrder.getOrderNum(),tempOrder.getVisitorId(),tempOrder.getVisitorType(),tempOrder.getDate(),curTime,tempOrder.getAmountOfVisitors(),tempOrder.getTelephone(),tempOrder.getEmail(),tempOrder.getExitTime(),tempOrder.getTotalCost(),tempOrder.getPayStatus(),tempOrder.getOrderStatus());
							}
					}
					
					
				}
				
				
				
			} 
			catch (SQLException e) 
				{e.printStackTrace();}
		}
	    // Create a Message object containing the list of alternative orders
		msg = new Message (Message.ActionType.ALTERNATIVEDATE,alternative);
		return msg;
	}
	
	/**
	 * Checks if the provided order's date and time already exist in the list of alternative orders.
	 * This method ensures that duplicate alternative orders are not added.
	 *
	 * @param order      The order to be checked for duplication.
	 * @param alternative The list of alternative orders to compare against.
	 * @return true if the order does not exist in the list of alternative orders, otherwise false.
	 */
	private Boolean checkAlternativeOrder(Order order,ArrayList<Order> alternative) {
		for (int i=0;i<alternative.size();i++)
		{
			if (order.getDate().equals(alternative.get(i).getDate())&& order.getTime().equals(alternative.get(i).getTime()))
				return false;
		}
		return true;
	}
	
	/**
	 * Retrieves the number of available places in the specified park.
	 * The available places are calculated based on the current occupancy of the park.
	 *
	 * @param parkName The name of the park for which available places are to be determined.
	 * @return A Message object containing the number of available places in the park.
	 */
	public Message getAvailablePlaces ( String parkName) {
		Message msg;
		String available="";
		try 
		{	int count1=0;
			int count2=0;
			int max=0;
			
			
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND OrderStatus = ?");
			preparedStatement.setString(1, parkName); // 1-indexed parameter position
			preparedStatement.setString(2, "inside");

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			 while (rs.next()) {
				 count1 += rs.getInt(1); // Get the value of the first column in the result set
	            }
	       
			rs.close();
			preparedStatement.close();
			Message parkInfoMsg= getParkInfo(parkName);
	        Park park = (Park) ((Message) parkInfoMsg).getContent();
	        // Number of hours to add
	        int maxStay = Integer.parseInt(park.getMaxStay());
	        int totalCapacity=Integer.parseInt(park.getTotalCapacity());
	     
			// Prepare a statement with a placeholder
			preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
			preparedStatement.setString(1, parkName); // 1-indexed parameter position
			preparedStatement.setString(2, RunnableSql.getCurrentTimePlusX(maxStay));
			preparedStatement.setString(3, RunnableSql.getCurrentTimePlusX(maxStay));
			preparedStatement.setString(4, RunnableSql.getCurrentDatePlusX(0));
			preparedStatement.setString(5, "cancelled automatically");
			preparedStatement.setString(6, "cancelled manually");

			// Execute the query
			rs = preparedStatement.executeQuery();
			 // Process the result set
			 while (rs.next()) {
				 count2 += rs.getInt(1); // Get the value of the first column in the result set
	            }
	       
			rs.close();
			preparedStatement.close();
			max=Math.max(count1, count2);
			available=String.valueOf(totalCapacity-max);

			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.ERROR,available);
		return msg;
	}
	
	
	/**
	 * Adds an unplanned order to the database.
	 * This method inserts a new order into the database with the provided order details.
	 * The order number, date, time, and exit time are automatically generated.
	 * 
	 * @param order The unplanned order to be added to the database.
	 */
	public void addUnplannedOrder(Order order) {
		try {
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.orders (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, VisitorType, ExitTime, OrderStatus, PayStatus, TotalCost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            order.setOrderNum(Integer.toString(getRandomOrderNumber()));
            order.setDate(RunnableSql.getCurrentDatePlusX(0));
            order.setTime(RunnableSql.getCurrentTimeMinusX(0));

	        Message msg2 = getParkInfo(order.getParkName());
	        Park park = (Park) ((Message) msg2).getContent();
	        // Number of hours to add
	        int maxStay = Integer.parseInt(park.getMaxStay());
	        
	        String exitTime = RunnableSql.getCurrentTimePlusX(maxStay);
	        order.setExitTime(exitTime);
            
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,order.getOrderNum() );
           pstmt.setString(2, order.getParkName());
           pstmt.setString(3, order.getVisitorId());
           pstmt.setString(4, order.getDate());
           pstmt.setString(5, order.getTime());
           pstmt.setString(6, order.getAmountOfVisitors());
           pstmt.setString(7, order.getVisitorType());
           pstmt.setString(8, order.getExitTime());
           pstmt.setString(9, "inside");
           pstmt.setString(10, order.getPayStatus());
           pstmt.setString(11, order.getTotalCost());
                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
	}
	
	
	/**
	 * Approves the exit of a visitor from the park based on the provided order number.
	 * If the order number is correct and the visitor is currently inside the park, the exit time is updated
	 * to the current time and the order status is set to 'completed'.
	 * 
	 * @param orderNumber The order number of the visitor whose exit is to be approved.
	 * @return A Message object indicating the status of the exit approval process.
	 *         If the order number is incorrect or the visitor is not currently inside the park, an error message is returned.
	 */
	public Message approveExit (String orderNumber) {
		Message msg=new Message (Message.ActionType.ERROR,"Order number is incorrect or not currently inside the park");
		PreparedStatement ps;
		try {

			 	ps = conn.prepareStatement("UPDATE g13.orders SET ExitTime=?,OrderStatus=? WHERE OrderNumber = ?");
			 	ps.setString(1,RunnableSql.getCurrentTimeMinusX(0));
	            ps.setString(2,"completed");
	            ps.setString(3,orderNumber);
	            int rowsAffected = ps.executeUpdate();
	            // Check if any rows were affected
	            if (rowsAffected > 0) 
	                // Update was successful
	            	 msg = new Message (Message.ActionType.ERROR,"Exit approved for order number "+orderNumber);
	            ps.close();
	            
		} catch (SQLException e) {System.out.println("Error");	}
		return msg;
		
	}


	/**
	 * Checks if a reservation can be made for the provided order.
	 * If the number of visitors for the specified park, date, and time exceeds the maximum capacity,
	 * the order is added to the waiting list. Otherwise, the reservation is saved successfully.
	 * 
	 * @param order The order for which reservation availability is to be checked.
	 * @return A Message object indicating the outcome of the reservation check.
	 *         If the order needs to be added to the waiting list, a WAITINGLIST message is returned.
	 *         If the reservation can be made successfully, a RESERVATION message is returned.
	 */
	public static Message checkReservation(Order order)
	{	//this function to make reservation
		Message msg;
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			preparedStatement.setString(5, "cancelled automatically");
			preparedStatement.setString(6, "cancelled manually");
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			// Process results
			int currentAmountOfVisitors = Integer.parseInt(order.getAmountOfVisitors());
            while (rs.next()) {
            	currentAmountOfVisitors += Integer.parseInt(rs.getString("NumberOfVisitors"));
            }
		    rs.close();
			preparedStatement.close();
			// Prepare a statement with a placeholder
			Message parkInfoMsg= getParkInfo(order.getParkName());
	        Park park = (Park) ((Message) parkInfoMsg).getContent();
	        // Number of hours to add
	        int maxStay = Integer.parseInt(park.getMaxStay());
	        int maxCapacity=Integer.parseInt(park.getReservedCapacity());
			
			if (currentAmountOfVisitors>maxCapacity)
			{
				msg = new Message (Message.ActionType.WAITINGLIST,order);
				return msg;
			}
			// Original time string
	        String timeString = order.getTime();
	        // Parse the time string into LocalTime
	        LocalTime time = LocalTime.parse(timeString);
	        // Add x hours
	        LocalTime newTime = time.plusHours(maxStay);
	        // Format the new time back into a string
	        String result = newTime.format(DateTimeFormatter.ofPattern("HH:mm"));
			order.setExitTime(result);
			// Prepare a statement with a placeholder
			preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getExitTime());
			preparedStatement.setString(3, order.getExitTime());
			preparedStatement.setString(4, order.getDate());
			preparedStatement.setString(5, "cancelled automatically");
			preparedStatement.setString(6, "cancelled manually");
			// Execute the query
			rs = preparedStatement.executeQuery();
			 // Process the result set
			// Process results
			int exitTimeAmountOfVisitors = Integer.parseInt(order.getAmountOfVisitors());
            while (rs.next()) {
            	exitTimeAmountOfVisitors += Integer.parseInt(rs.getString("NumberOfVisitors"));
            }
		    rs.close();
			preparedStatement.close();
	        // Check if the number of visitors for the exit time exceeds the maximum capacity

			if (exitTimeAmountOfVisitors>maxCapacity)
			{
				// Add the order to the waiting list
				msg = new Message (Message.ActionType.WAITINGLIST,order);
				return msg;
			}
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		 // Reservation saved successfully
		msg = new Message (Message.ActionType.RESERVATION,"Reservation saved successfully");
		return msg;
	}
	
	
	/**
	 * Inserts a reservation for the provided order if it meets the criteria.
	 * 
	 * This method first checks the reservation status for the order using the checkReservation method.
	 * If the reservation is allowed (i.e., the order does not need to be added to the waiting list),
	 * the order details are inserted into the database using the insertOrder method.
	 * 
	 * @param order The order for which the reservation is to be inserted.
	 * @return A Message object indicating the outcome of the reservation insertion.
	 *         If the reservation is successful, the returned message will be the same as the one returned by the checkReservation method.
	 *         If the reservation is not allowed and the order needs to be added to the waiting list, the returned message will be from the checkReservation method.
	 */
	public static Message insertReservation(Order order) {
		 // Check reservation status for the order
		Message msg = checkReservation(order);
		// If the reservation is allowed, insert the order
		if (msg.getActionType().equals(Message.ActionType.RESERVATION))
			insertOrder(order);
		return msg;
	}
	
	/**
	 * Updates an existing reservation for the provided order if it meets the criteria.
	 * 
	 * This method first checks the reservation status for the order using the checkReservation method.
	 * If the reservation is allowed (i.e., the order does not need to be added to the waiting list),
	 * the order details are updated in the database using the updateOrderInfo method.
	 * 
	 * @param order The order for which the reservation is to be updated.
	 * @return A Message object indicating the outcome of the reservation update.
	 *         If the update is successful, the returned message will be the same as the one returned by the checkReservation method.
	 *         If the update is not allowed and the order needs to be added to the waiting list, the returned message will be from the checkReservation method.
	 */
	public Message updateReservation(Order order) {
	    // Check reservation status for the order
		Message msg = checkReservation(order);
	    // If the reservation is allowed, update the order
		if (msg.getActionType().equals(Message.ActionType.RESERVATION))
			updateOrderInfo(order);
		return msg;
	}
	
	
	/**
	 * Retrieves information about a park from the database based on the provided park name.
	 * 
	 * This method queries the database to fetch details such as reserved capacity, total capacity,
	 * and maximum stay duration for the specified park. It then encapsulates this information
	 * into a Park object and returns it within a Message object with ActionType PARKINFO.
	 * 
	 * @param parkName The name of the park for which information is to be retrieved.
	 * @return A Message object containing information about the specified park.
	 *         If the park is found in the database, the returned message will contain the park details.
	 *         If the park is not found or an error occurs during database access, an appropriate error message will be returned.
	 */
	public static Message getParkInfo(String parkName) {
		Message msg;
		Park park = new Park(parkName);
		try {
	        // Prepare SQL statement to retrieve park information
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, park.getParkName());
			 // 1-indexed parameter position
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
	        // Set park details from the result set
			park.setReservedCapacity(rs.getString("ReservedCapacity"));
			park.setTotalCapacity(rs.getString("TotalCapacity"));
			park.setMaxStay(rs.getString("MaxStay"));
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // Create a message containing park information and return it
		msg = new Message (Message.ActionType.PARKINFO,park);
		return msg;
	}
	
	
	/**
	 * Updates park information in the database based on the provided request.
	 * This method executes an SQL update statement to modify the reserved capacity,
	 * total capacity, and maximum stay duration for a park identified by its name.
	 * 
	 * @param request the request containing updated park information
	 *                including reserved capacity, total capacity, maximum stay duration,
	 *                and the name of the park to be updated
	 * 
	 *        
	 */
	public void updateParkInfo(Request request)
	{
		PreparedStatement ps;
		try {

			 	ps = conn.prepareStatement("UPDATE g13.parks SET ReservedCapacity=?,TotalCapacity=?,MaxStay=? WHERE ParkName = ?");
			 	ps.setString(1,request.getReservedCapacity());
			 	ps.setString(2,request.getTotalCapacity());
	            ps.setString(3,request.getMaxStay());
	            ps.setString(4,request.getParkName());
	            // Execute the update operation
	            ps.executeUpdate();
	            // Close the prepared statement to release resources
	            ps.close();
	            
		} catch (SQLException e) 
        // Handle any SQL exceptions
		{System.out.println("Error");	}
	}
	
	
	/**
	 * Generates a random order number.
	 * This method generates a random 8-digit order number and ensures uniqueness by
	 * checking if the generated number already exists in the database. If the generated
	 * number exists, it generates a new one until a unique number is found.
	 * 
	 * @return a random 8-digit order number
	 */
	public static int getRandomOrderNumber() {
		// Create an instance of Random
        Random random = new Random();
        // Generate a random number of 8 digits
        int randomNumber = random.nextInt(90000000) + 10000000;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE OrderNumber = ?");
			preparedStatement.setString(1, Integer.toString(randomNumber));
			ResultSet rs = preparedStatement.executeQuery();
	        // Check if the generated number already exists, if yes, generate a new one
			while (rs.next()) {
				rs.close();
				preparedStatement.close();
				randomNumber = random.nextInt(90000000) + 10000000;
				preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE OrderNumber = ?");
				preparedStatement.setString(1, Integer.toString(randomNumber));
				rs = preparedStatement.executeQuery();
			}
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return randomNumber;
	}
	
	/**
	 * Updates the role of a user to "GUIDE" in the database.
	 * This method updates the user's role to "GUIDE" if the user's current role is "VISITOR".
	 * 
	 * @param user the user whose role is to be updated
	 * @return a message indicating the success or failure of the role update operation
	 */
	public Message updateRoleToGuide(User user)
	{	Message msg = new Message (Message.ActionType.ERROR,"ID is not found or his role is not visitor");
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.users SET UserPermission=? WHERE UserId = ? AND UserPermission = ?");
			 	ps.setString(1,"GUIDE");
	            ps.setString(2,user.getId());
	            ps.setString(3,"VISITOR");
	            // Execute the update and get the number of rows affected
	            int rowsAffected = ps.executeUpdate();
	         // Check if any rows were affected
	            if (rowsAffected > 0) 
	                // Update was successful
	            	 msg = new Message (Message.ActionType.ERROR,"Updated role successfully");
	            ps.close();

	    
	           
	            
		} catch (SQLException e) {	}
		return msg;
	}
	
	
	/**
	 * Inserts a new request for a park into the database.
	 * This method inserts a new request into the 'requests' table in the database
	 * with the provided park's information and sets the status of the request to "waiting for approval".
	 * 
	 * @param park the park for which the request is being made
	 */
	public void insertRequest(Park park)
	{
		try {
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.requests (ParkName, ReservedCapacity, TotalCapacity, MaxStay, Status) VALUES (?, ?, ?, ?, ?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
          
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,park.getParkName() );
           pstmt.setString(2, park.getReservedCapacity());
           pstmt.setString(3, park.getTotalCapacity());
           pstmt.setString(4, park.getMaxStay());
           pstmt.setString(5, "waiting for approval");

                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
		
	}


	/**
	 * Updates the status of a request in the database.
	 * This method updates the status of a request in the 'requests' table in the database
	 * based on the provided request's information.
	 * 
	 * @param request the request whose status is to be updated
	 * @return a message indicating the success or failure of the status update operation
	 */
	public Message updateRequest(Request request) {
		Message msg = new Message (Message.ActionType.ERROR,"");
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.requests SET Status=? WHERE ParkName = ? AND ReservedCapacity = ? AND TotalCapacity = ? AND MaxStay = ?");
			 	ps.setString(1,request.getStatus());
	            ps.setString(2,request.getParkName());
	            ps.setString(3,request.getReservedCapacity());
	            ps.setString(4,request.getTotalCapacity());
	            ps.setString(5,request.getMaxStay());

	            // Execute the update and get the number of rows affected
	            int rowsAffected = ps.executeUpdate();
	         // Check if any rows were affected
	            if (rowsAffected > 0) 
	                // Update was successful
	            	 msg = new Message (Message.ActionType.ERROR,"Request status changed successfully");
	            ps.close();

	    
	           
	            
		} catch (SQLException e) {	}
		return msg;
		
	}

	
	/**
	 * Retrieves requests from the 'requests' table in the database with status "waiting for approval".
	 * This method queries the 'requests' table in the database to retrieve requests
	 * with the status "waiting for approval", and returns a message containing
	 * the list of requests.
	 * 
	 * @return a message containing the list of requests with status "waiting for approval"
	 */
	public Message getRequestsTable() {
		Message msg;
		 // Create an ArrayList to store WaitingListEntry objects
        ArrayList<Request> requests = new ArrayList<>();
		try {

			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.requests WHERE Status = ? ");
			preparedStatement.setString(1, "waiting for approval"); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	requests.add(new Request(
            		    rs.getString("ParkName"),
            		    rs.getString("ReservedCapacity"),
            		    rs.getString("TotalCapacity"),
            		    rs.getString("MaxStay")
            		    ));

           
	        }} catch (SQLException e) {
	            e.printStackTrace();
	        }
	    // Create a message containing the list of requests
		msg = new Message (Message.ActionType.REQUESTSTABLE,requests);
		return msg;
		
		
	}

	
	/**
	 * Creates a cancellation report for a park.
	 * This method creates a cancellation report for a park based on the provided report information.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a message indicating the success or failure of the report creation operation
	 */
	public Message CreateCancellationReport(Report report)
	{	Message msg=new Message(Message.ActionType.ERROR,"Report already exists");
		int manually=0,automatically=0;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.cancellationreport WHERE ParkName = ? AND Month = ? AND Year=? ");
			
			preparedStatement.setString(1, report.getParkName());
			preparedStatement.setString(2, report.getMonth()); // Use "%" to match any characters before the date pattern
			preparedStatement.setString(3, report.getYear());
			ResultSet rs  = preparedStatement.executeQuery();
			if (rs.next())
				return msg;
			rs.close();
			preparedStatement.close();
			String date = report.getMonth()+"-"+report.getYear();
			preparedStatement = conn.prepareStatement("SELECT OrderStatus FROM g13.orders WHERE ParkName = ? AND Date LIKE ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			// Execute the query
			rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	if (rs.getString("OrderStatus").equals("cancelled manually"))
            		manually++;
            	else if (rs.getString("OrderStatus").equals("cancelled automatically"))
            		automatically++;
            }
            rs.close();
			preparedStatement.close();
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.cancellationreport (ParkName, Month, Year, CancelledManually, CancelledAutomatically) VALUES (?, ?, ?, ?,?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
          
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,report.getParkName() );
           pstmt.setString(2, report.getMonth());
           pstmt.setString(3, report.getYear());
           pstmt.setString(4, String.valueOf(manually));
           pstmt.setString(5, String.valueOf(automatically));

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
		msg=new Message(Message.ActionType.ERROR,"Report successfully created");
		return msg;
		
	}
	
	
	/**
	 * Creates a total visitors report for a park.
	 * This method creates a total visitors report for a park based on the provided report information.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a message indicating the success or failure of the report creation operation
	 */
	public Message CreateTotalVisitorsReport(Report report)
	{	Message msg=new Message(Message.ActionType.ERROR,"Report already exists");
		int organized=0,others=0;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.totalvisitorsreport WHERE ParkName = ? AND Month = ? AND Year=? ");
			
			preparedStatement.setString(1, report.getParkName());
			preparedStatement.setString(2, report.getMonth()); // Use "%" to match any characters before the date pattern
			preparedStatement.setString(3, report.getYear());
			ResultSet rs  = preparedStatement.executeQuery();
			if (rs.next())
				return msg;
			rs.close();
			preparedStatement.close();
			String date = report.getMonth()+"-"+report.getYear();
			preparedStatement = conn.prepareStatement("SELECT VisitorType,OrderStatus,NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Date LIKE ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			// Execute the query
			rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	if (rs.getString("OrderStatus").equals("confirmed") || rs.getString("OrderStatus").equals("inside"))
            		if (rs.getString("VisitorType").equals("organized group"))
            			organized+=Integer.parseInt(rs.getString("NumberOfVisitors"));
            		else
            			others+=Integer.parseInt(rs.getString("NumberOfVisitors"));
            
            }
            rs.close();
			preparedStatement.close();
			
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.totalvisitorsreport (ParkName, Month, Year, TotalIndividuals, TotalGroups) VALUES (?, ?, ?, ?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
          
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,report.getParkName() );
           pstmt.setString(2, report.getMonth());
           pstmt.setString(3, report.getYear());
           pstmt.setString(4, String.valueOf(others));
           pstmt.setString(5, String.valueOf(organized));

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
		msg=new Message(Message.ActionType.ERROR,"Report successfully created");
		return msg;
		
	}
	
	
	/**
	 * Creates a visiting report for a specified visitor type and inserts it into the database.
	 * This method generates a visiting report for a specified visitor type and inserts the report
	 * data into the 'visitingreport' table in the database.
	 * 
	 * @param report the report containing park name, month, and year
	 * @param visitorType the type of visitor for whom the report is being created
	 */
	public void createVisitingReportForVisitorType(Report report,String visitorType) {
		try {


			// Step 1: Query orders for the specified month, year, and park name
	        String date = report.getMonth()+"-"+report.getYear();
	        String query;
	        if (visitorType.contains("organized")) {
	            query = "SELECT * FROM g13.orders WHERE ParkName = ? AND Date LIKE ? AND OrderStatus != ? AND OrderStatus != ? AND VisitorType = ?";
	        } else {
	            query = "SELECT * FROM g13.orders WHERE ParkName = ? AND Date LIKE ? AND OrderStatus != ? AND OrderStatus != ? AND VisitorType != ?";
	        }
	        PreparedStatement preparedStatement = conn.prepareStatement(query);

			preparedStatement.setString(1, report.getParkName());
			 // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			preparedStatement.setString(3, "cancelled automatically");
			preparedStatement.setString(4, "cancelled manually");
			preparedStatement.setString(5, "organized group");
		     
			// Query orders for the specified month, year, and park name
			ResultSet ordersResultSet = preparedStatement.executeQuery();
			ArrayList<UsageVisitingReport> usageList = new ArrayList<UsageVisitingReport>();
			// Step 2: Group orders by time range
			Map<String, List<Order>> ordersByTimeRange = new HashMap<>();
			for (int hour = 8; hour < 20; hour+=3) {
			    String startTime = String.format("%02d:00", hour);
			    String endTime = String.format("%02d:00", hour + 3);
			    String timeRange = startTime + "-" + endTime;
			    ordersByTimeRange.put(timeRange, null);
			 // Create UsageReport object and add it to the list
			    UsageVisitingReport usageReport = new UsageVisitingReport(timeRange, 0, 0);
			    usageList.add(usageReport);
			}

			while (ordersResultSet.next()) {
			    String time = ordersResultSet.getString("Time");
			    String timeRange = calculateTimeRangeVisitingReport(time); // Implement this method to calculate time range
			    Order order = createOrderFromResultSet(ordersResultSet); // Implement this method to create an Order object from the ResultSet
			    ordersByTimeRange.computeIfAbsent(timeRange, k -> new ArrayList<>()).add(order);
			}
			// Step 4-6: Calculate full and not full counts for each time range
			int j=0;

			for (String timeRange : ordersByTimeRange.keySet()) {

			    if (ordersByTimeRange.get(timeRange)!=null) {
				    List<Order> ordersInTimeRange = ordersByTimeRange.get(timeRange);
				    for (Order order : ordersInTimeRange) {
				    	usageList.get(j).setFullOrEnterCounter(usageList.get(j).getFullOrEnterCounter()+Integer.parseInt(order.getAmountOfVisitors()));
				    	String exitRange = calculateTimeRangeVisitingReport(order.getExitTime());
				    	int index=getIndexOfStringInArrayList(exitRange,usageList);
				    	if (index!=-1)
				    		usageList.get(index).setNotFullOrExitCounter(usageList.get(index).getNotFullOrExitCounter()+Integer.parseInt(order.getAmountOfVisitors()));
				    }
				    
			    }
			    j++;			    
			}


			int i=0;

			// Step 7: Insert usage report data into g13.usagereport table
			for (String timeRange : ordersByTimeRange.keySet()) {
				String reportId=generateReportId(report.getReportType());
			    // SQL INSERT statement
			    String sql = "INSERT INTO g13.visitingreport (ReportId,ParkName,Month,Year, TimeRange, EnterCount,ExitCount,VisitorType) VALUES (?,?,?,?,?, ?, ?,?)";
			    PreparedStatement pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1, reportId);
			    pstmt.setString(2, report.getParkName());
			    pstmt.setString(3, report.getMonth());
			    pstmt.setString(4, report.getYear());
			    pstmt.setString(5, timeRange);
			    pstmt.setString(6, String.valueOf(usageList.get(i).getFullOrEnterCounter()));
			    pstmt.setString(7, String.valueOf(usageList.get(i).getNotFullOrExitCounter() ));
			    pstmt.setString(8,  visitorType);


			    // Execute the INSERT statement
			    pstmt.executeUpdate();
			    i++;
			}

			 // Step 8: Update exit count in g13.visitingreport table
				for (i=0;i<usageList.size();i++){
			    // SQL INSERT statement
			    String sql = "UPDATE g13.visitingreport SET ExitCount=? WHERE ParkName=? AND Month=? AND Year=? AND TimeRange=? AND VisitorType=?";
			    PreparedStatement pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1, String.valueOf(usageList.get(i).getNotFullOrExitCounter() ));
			    pstmt.setString(2, report.getParkName());
			    pstmt.setString(3, report.getMonth());
			    pstmt.setString(4, report.getYear());
			    pstmt.setString(5, usageList.get(i).getTimeRange());
			    pstmt.setString(6, visitorType);


			    // Execute the INSERT statement
			    pstmt.executeUpdate();
			}

		 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates a visiting report for a park for both individual/family visitors and organized groups.
	 * This method creates a visiting report for a park based on the provided report information.
	 * It generates separate reports for individual/family visitors and organized groups.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a message indicating the success or failure of the report creation operation
	 */
	public Message createVisitingReport(Report report) {
		Message msg=new Message(Message.ActionType.ERROR,"Report already exists");
		try {
		PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.visitingreport WHERE ParkName = ? AND Month = ? AND Year=? ");
		
		preparedStatement.setString(1, report.getParkName());
		preparedStatement.setString(2, report.getMonth()); // Use "%" to match any characters before the date pattern
		preparedStatement.setString(3, report.getYear());
		ResultSet rs  = preparedStatement.executeQuery();
		if (rs.next())
			return msg;
		rs.close();
		preparedStatement.close();
		}
		catch (Exception e) {}
		createVisitingReportForVisitorType(report,"individuals and families");
		createVisitingReportForVisitorType(report,"organized group");
		msg=new Message(Message.ActionType.ERROR,"Report successfully created");
		return msg;
	}
	
	
	/**
	 * Creates a usage report for a park based on the provided report information.
	 * This method generates a usage report for a park based on the provided report information,
	 * calculating full and not full counts for each time range.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a message indicating the success or failure of the report creation operation
	 */
	public Message createUsageReport(Report report) {
		Message msg=new Message(Message.ActionType.ERROR,"Report already exists");
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.usagereport WHERE ParkName = ? AND Month = ? AND Year=? ");
			
			preparedStatement.setString(1, report.getParkName());
			preparedStatement.setString(2, report.getMonth()); // Use "%" to match any characters before the date pattern
			preparedStatement.setString(3, report.getYear());
			ResultSet rs  = preparedStatement.executeQuery();
			if (rs.next())
				return msg;
			rs.close();
			preparedStatement.close();
				
			// Step 1: Retrieve reserved capacity for the park
			Message parkInfoMsg = getParkInfo(report.getParkName());
	        Park park = (Park) ((Message) parkInfoMsg).getContent();
	        int reservedCapacity=Integer.parseInt(park.getReservedCapacity());

			// Step 2: Query orders for the specified month, year, and park name
	        String date = report.getMonth()+"-"+report.getYear();
			preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE ParkName = ? AND Date LIKE ? AND OrderStatus!=? AND OrderStatus!=?");
			
			preparedStatement.setString(1, report.getParkName());
			 // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			preparedStatement.setString(3, "cancelled automatically");
			preparedStatement.setString(4, "cancelled manually");
			// Query orders for the specified month, year, and park name
			ResultSet ordersResultSet = preparedStatement.executeQuery();
			ArrayList<UsageVisitingReport> usageList = new ArrayList<UsageVisitingReport>();
			// Step 3: Group orders by time range
			Map<String, List<Order>> ordersByTimeRange = new HashMap<>();
			for (int hour = 8; hour < 16; hour++) {
			    String startTime = String.format("%02d:00", hour);
			    String endTime = String.format("%02d:00", hour + 1);
			    String timeRange = startTime + "-" + endTime;
			    ordersByTimeRange.put(timeRange, null);
			}
			while (ordersResultSet.next()) {
			    String time = ordersResultSet.getString("Time");
			    String timeRange = calculateTimeRange(time); // Implement this method to calculate time range
			    Order order = createOrderFromResultSet(ordersResultSet); // Implement this method to create an Order object from the ResultSet
			    ordersByTimeRange.computeIfAbsent(timeRange, k -> new ArrayList<>()).add(order);
			}

			// Step 4-6: Calculate full and not full counts for each time range
			for (String timeRange : ordersByTimeRange.keySet()) {
			    int fullCounter = 0;
			    int notFullCounter = 0;
			    if (ordersByTimeRange.get(timeRange)!=null) {
				    List<Order> ordersInTimeRange = ordersByTimeRange.get(timeRange);
				    for (Order order : ordersInTimeRange) {
				        int totalVisitors = calculateTotalVisitors(order);
				        if (totalVisitors >= reservedCapacity) {
				            fullCounter++;
				        } else {
				            notFullCounter++;
				        }
				    }
			    }
			    // Create UsageReport object and add it to the list
			    UsageVisitingReport usageReport = new UsageVisitingReport(timeRange, fullCounter, notFullCounter);
			    usageList.add(usageReport);
			}
			

			int i=0;
			// Step 7: Insert usage report data into g13.usagereport table
			for (String timeRange : ordersByTimeRange.keySet()) {
				String reportId=generateReportId(report.getReportType());
			    // SQL INSERT statement
			    String sql = "INSERT INTO g13.usagereport (ReportId,ParkName,Month,Year, TimeRange, FullCounter,NotFullCounter) VALUES (?,?,?,?,?, ?, ?)";
			    PreparedStatement pstmt = conn.prepareStatement(sql);
			    pstmt.setString(1, reportId);
			    pstmt.setString(2, report.getParkName());
			    pstmt.setString(3, report.getMonth());
			    pstmt.setString(4, report.getYear());
			    pstmt.setString(5, timeRange);
			    pstmt.setString(6, String.valueOf(usageList.get(i).getFullOrEnterCounter() ));
			    pstmt.setString(7, String.valueOf(usageList.get(i).getNotFullOrExitCounter() ));

			    // Execute the INSERT statement
			    pstmt.executeUpdate();
			    i++;
			}
		 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg=new Message(Message.ActionType.ERROR,"Report successfully created");
		return msg;
		
	}
	
	
	/**
	 * Retrieves the index of a specified time range string in an ArrayList of UsageVisitingReport objects.
	 * 
	 * @param timeRange the time range string to search for
	 * @param usageList the ArrayList of UsageVisitingReport objects to search within
	 * @return the index of the specified time range string in the ArrayList; returns -1 if not found
	 */
	public static int getIndexOfStringInArrayList(String timeRange,ArrayList<UsageVisitingReport> usageList) {
		// Iterate over the ArrayList to find the index of the object with the specified timeRange
        int index = -1; // Initialize index to -1 (not found)
        for (int i = 0; i < usageList.size(); i++) {
            if (usageList.get(i).getTimeRange().equals(timeRange)) {
                index = i; // Set the index when the target timeRange is found
                break; // Exit the loop since we found the index
            }
        }
        return index;
	}
	
	/**
	 * Generates a unique report ID for the given report type.
	 * 
	 * @param reportType the type of the report (e.g., "UsageReport" or "VisitingReport")
	 * @return a unique report ID as a string
	 */
	public String generateReportId(String reportType) {
	    String table;
	    if (reportType.contains("Usage"))
	        table = "g13.usagereport";
	    else
	        table = "g13.visitingreport";
	    
	    Random random = new Random();
	    int randomNumber = random.nextInt(90000000) + 10000000;
	    
	    try {
	        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM " + table + " WHERE ReportId = ?");
	        preparedStatement.setString(1, Integer.toString(randomNumber));
	        ResultSet rs = preparedStatement.executeQuery();
	        // Check if the generated report ID already exists
	        while (rs.next()) {
	            rs.close();
	            preparedStatement.close();
	            // Generate a new random number
	            randomNumber = random.nextInt(90000000) + 10000000;
	            preparedStatement = conn.prepareStatement("SELECT * FROM " + table + " WHERE ReportId = ?");
	            preparedStatement.setString(1, Integer.toString(randomNumber));
	            rs = preparedStatement.executeQuery();
	        }
	        
	        rs.close();
	        preparedStatement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return String.valueOf(randomNumber);
	}


	/**
	 * Calculates the total number of visitors for each time range based on the provided order.
	 * 
	 * @param order the order containing information about the park, time, date, and number of visitors
	 * @return the total number of visitors for the given order's time range
	 */
	public int calculateTotalVisitors(Order order) {
        int totalVisitors = Integer.parseInt(order.getAmountOfVisitors());
        try {
	     // Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			preparedStatement.setString(5, "cancelled automatically");
			preparedStatement.setString(6, "cancelled manually");
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
	        while (rs.next())
	            totalVisitors += Integer.parseInt(rs.getString("NumberOfVisitors"));
	        rs.close();
	        preparedStatement.close(); 
        }
        catch (Exception e) {
        	
        }
       
        return totalVisitors;
    }


	 /**
	  * Calculates the time range based on the provided order time.
	  * 
	  * @param orderTime the time of the order in HH:mm format
	  * @return the calculated time range in HH:mm-HH:mm format
	  */
	 public static String calculateTimeRange(String orderTime) {
	    String[] timeParts = orderTime.split(":");
	    int hour = Integer.parseInt(timeParts[0]);
	    // Determine the time range based on the hour
	    if (hour >= 8 && hour < 9) {
	        return "08:00-09:00";
	    } else if (hour >= 9 && hour < 10) {
	        return "09:00-10:00";
	    } else if (hour >= 10 && hour < 11) {
	        return "10:00-11:00";
	    } else if (hour >= 11 && hour < 12) {
	        return "11:00-12:00";
	    } else if (hour >= 12 && hour < 13) {
	        return "12:00-13:00";
	    } else if (hour >= 13 && hour < 14) {
	        return "13:00-14:00";
	    } else if (hour >= 14 && hour < 15) {
	        return "14:00-15:00";
	    } else
	        return "15:00-16:00";
	  

	    
	}

	 /**
	  * Calculates the time range for visiting report based on the provided order time.
	  * 
	  * @param orderTime the time of the order in HH:mm format
	  * @return the calculated time range in HH:mm-HH:mm format
	  */
	 public static String calculateTimeRangeVisitingReport(String orderTime) {
		    String[] timeParts = orderTime.split(":");
		    int hour = Integer.parseInt(timeParts[0]);
		    // Determine the time range based on the hour
		    if (hour >= 8 && hour < 11) {
		        return "08:00-11:00";
		    } else if (hour >= 11 && hour < 14) {
		        return "11:00-14:00";
		    } else if (hour >= 14 && hour < 17) {
		        return "14:00-17:00";
		    } else 
		        return "17:00-20:00";
		    

		    
		}

	 /**
	  * Creates an Order object from the provided ResultSet.
	  * 
	  * @param resultSet the ResultSet containing order information
	  * @return the Order object created from the ResultSet
	  * @throws SQLException if a SQL exception occurs while accessing the ResultSet
	  */
	 public Order createOrderFromResultSet(ResultSet resultSet) throws SQLException {
	    Order order = new Order();
	    order.setOrderNum(resultSet.getString("OrderNumber"));
	    order.setParkName(resultSet.getString("ParkName"));
	    order.setVisitorId(resultSet.getString("VisitorId"));
	    order.setDate(resultSet.getString("Date"));
	    order.setTime(resultSet.getString("Time"));
	    order.setAmountOfVisitors(resultSet.getString("NumberOfVisitors"));
	    order.setTelephone(resultSet.getString("PhoneNumber"));
	    order.setEmail(resultSet.getString("Email"));
	    order.setVisitorType(resultSet.getString("VisitorType"));
	    order.setExitTime(resultSet.getString("ExitTime"));
	    order.setOrderStatus(resultSet.getString("OrderStatus"));
	    order.setPayStatus(resultSet.getString("PayStatus"));
	    order.setTotalCost(resultSet.getString("TotalCost"));
	    return order;
	}
	
	 
	 /**
	  * Retrieves the cancellation report details for the specified park, month, and year.
	  * 
	  * @param report the report containing park name, month, and year
	  * @return a Message object containing the cancellation report details, or an error message if the report is not found
	  */
	public Message getCancellationReport(Report report)
	{
		Message msg;
		ArrayList<String> details = new ArrayList<>();

		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.cancellationreport WHERE ParkName = ? AND Month = ? AND Year = ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, report.getMonth()); 
			preparedStatement.setString(3, report.getYear()); 

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			if (rs.next()) {
				details.add(rs.getString("CancelledManually"));
				details.add(rs.getString("CancelledAutomatically"));
			}
			else {
				msg = new Message (Message.ActionType.ERROR,"Report not found!");
				return msg;
			}
	            
          
			rs.close();
			preparedStatement.close();
			report.setDetails(details);
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.REPORTINFO,report);
		return msg;
	}
	
	
	/**
	 * Retrieves the usage report for the specified park, month, and year.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a Message object containing the usage report details, or an error message if the report is not found
	 */
	public Message getUsageReport(Report report)
	{
		Message msg;
		ArrayList<UsageVisitingReport> usageList = new ArrayList<UsageVisitingReport>();

		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.usagereport WHERE ParkName = ? AND Month = ? AND Year = ? ORDER BY TimeRange ASC");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, report.getMonth()); 
			preparedStatement.setString(3, report.getYear()); 

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			if (rs.next()) {
				UsageVisitingReport usageReport=new UsageVisitingReport(rs.getString("TimeRange"),Integer.parseInt(rs.getString("FullCounter")),Integer.parseInt(rs.getString("NotFullCounter")));
				usageList.add(usageReport);
			}
			else {
				msg = new Message (Message.ActionType.ERROR,"Report not found!");
				return msg;
			}
			while (rs.next()) {
				UsageVisitingReport usageReport=new UsageVisitingReport(rs.getString("TimeRange"),Integer.parseInt(rs.getString("FullCounter")),Integer.parseInt(rs.getString("NotFullCounter")));
				usageList.add(usageReport);
			}
	            
          
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.REPORTINFO,usageList);
		return msg;
	}
	
	
	/**
	 * Retrieves the visiting report for the specified park, month, and year.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a Message object containing the visiting report details, or an error message if the report is not found
	 */
	public Message getVisitingReport(Report report)
	{
		Message msg;
		ArrayList<UsageVisitingReport> individualsList = new ArrayList<UsageVisitingReport>();
		ArrayList<UsageVisitingReport> organizedList = new ArrayList<UsageVisitingReport>();
		ArrayList<ArrayList<UsageVisitingReport>> visitingArrayList= new ArrayList<ArrayList<UsageVisitingReport>>();


		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.visitingreport WHERE ParkName = ? AND Month = ? AND Year = ? ORDER BY TimeRange ASC");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, report.getMonth()); 
			preparedStatement.setString(3, report.getYear()); 

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			if (rs.next()) {
				UsageVisitingReport usageReport=new UsageVisitingReport(rs.getString("TimeRange"),Integer.parseInt(rs.getString("EnterCount")),Integer.parseInt(rs.getString("ExitCount")));
				if (rs.getString("VisitorType").contains("organized"))
					organizedList.add(usageReport);
				else
					individualsList.add(usageReport);
			}
			else {
				msg = new Message (Message.ActionType.ERROR,"Report not found!");
				return msg;
			}
			while (rs.next()) {
				UsageVisitingReport usageReport=new UsageVisitingReport(rs.getString("TimeRange"),Integer.parseInt(rs.getString("EnterCount")),Integer.parseInt(rs.getString("ExitCount")));
				if (rs.getString("VisitorType").contains("organized"))
					organizedList.add(usageReport);
				else
					individualsList.add(usageReport);
			}
	            
          
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		visitingArrayList.add(individualsList);
		visitingArrayList.add(organizedList);
		msg = new Message (Message.ActionType.VISITINGREPORT,visitingArrayList);
		return msg;
	}
	
	
	/**
	 * Retrieves the total visitors report for the specified park, month, and year.
	 * 
	 * @param report the report containing park name, month, and year
	 * @return a Message object containing the total visitors report details, or an error message if the report is not found
	 */
	public Message getTotalVisitorsReport(Report report)
	{ 
		Message msg;
		ArrayList<String> details = new ArrayList<>();

		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.totalvisitorsreport WHERE ParkName = ? AND Month = ? AND Year = ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, report.getMonth()); 
			preparedStatement.setString(3, report.getYear()); 
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			if (rs.next()) {
				details.add(rs.getString("TotalIndividuals"));
				details.add(rs.getString("TotalGroups"));
			}
			else {
				msg = new Message (Message.ActionType.ERROR,"Report not found!");
				return msg;
			}
	            
          
			rs.close();
			preparedStatement.close();
			report.setDetails(details);
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.REPORTINFO,report);
		return msg;
	}
	
	
	/**
	 * Generates an invoice in PDF format for the specified order number.
	 * 
	 * @param orderNumber the order number for which the invoice is generated
	 * @return a Message object containing the invoice PDF file, or an error message if the order is not found or if an error occurs during generation
	 */
	public Message generateInvoice(String orderNumber) {
	    Message msg;
	    MyFile myFile = new MyFile(orderNumber + ".pdf");
	    try {
	        // Check if the order exists in the database
	        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE OrderNumber = ?");
	        preparedStatement.setString(1, orderNumber);
	        ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {
	            // Set description for the PDF file
	            myFile.setDescription("Invoice for Order Number: " + orderNumber);

	            PDDocument document = createPDF(rs);
	            // Save the PDF file to a byte array
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            document.save(baos);
	            document.close();
	            byte[] pdfBytes = baos.toByteArray();

	            // Set the byte array and size in the MyFile object
	            myFile.initArray(pdfBytes.length);
	            myFile.setSize(pdfBytes.length);
	            myFile.setMybytearray(pdfBytes);

	            // Close the result set and statement
	            rs.close();
	            preparedStatement.close();
	        } else {
	            // If order not found, return an error message
	            msg = new Message(Message.ActionType.ERROR, "Order not found!");
	            return msg;
	        }
	    } catch (SQLException | IOException e) {
	        // Handle exceptions
	        e.printStackTrace();
	        msg = new Message(Message.ActionType.ERROR, "Error generating invoice: " + e.getMessage());
	        return msg;
	    }

	    // Return the MyFile object containing the PDF data
	    msg = new Message(Message.ActionType.GETINVOICE, myFile);
	    return msg;
	}
	
	
	/**
	 * Creates a PDF document for an invoice based on the data retrieved from a ResultSet.
	 * 
	 * @param rs the ResultSet containing the order details
	 * @return a PDDocument representing the invoice PDF document
	 * @throws IOException if an I/O error occurs
	 * @throws SQLException if a SQL error occurs
	 */
	private PDDocument createPDF(ResultSet rs) throws IOException, SQLException {
		// Create a new PDF document
	    PDDocument document = new PDDocument();
	    PDPage page = new PDPage();
	    document.addPage(page);

	    // Get the page dimensions
	    PDRectangle pageSize = page.getMediaBox();
	    float pageWidth = pageSize.getWidth();

	    // Create content stream for adding text to the PDF
	    PDPageContentStream contentStream = new PDPageContentStream(document, page);
	    contentStream.beginText();

	    // Set the font size and calculate the width of the title text
	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
	    float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Invoice #" + rs.getString("OrderNumber")) / 1000f * 16;

	    // Calculate the x-coordinate to position the title at the center of the page
	    float titleX = (pageWidth - titleWidth) / 2;

	    // Position the text at the calculated coordinates
	    contentStream.newLineAtOffset(titleX, 750); // Adjust the y-coordinate as needed

	    // Add the title with orderNumber
	    contentStream.showText("Invoice #" + rs.getString("OrderNumber"));

	    // Reset font size for other fields
	    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
	    contentStream.newLineAtOffset(0, -20); // Move down for the rest of the content

	    // Set initial Y-coordinate
	    float y = 730;

	    y -= 30; // Move down by 15 points
	    contentStream.newLineAtOffset(-150, -30);
	    // Write each field with a manual adjustment to move to the next line
	    contentStream.showText("Order Number: " + rs.getString("OrderNumber"));
	    y -= 30; // Move down by 15 points
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Park Name: " + rs.getString("ParkName"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Visitor ID: " + rs.getString("VisitorId"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Date: " + rs.getString("Date"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Time: " + rs.getString("Time"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Number of Visitors: " + rs.getString("NumberOfVisitors"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Phone Number: " + rs.getString("PhoneNumber"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Email: " + rs.getString("Email"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Visitor Type: " + rs.getString("VisitorType"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Exit Time: " + rs.getString("ExitTime"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Order Status: " + rs.getString("OrderStatus"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Pay Status: " + rs.getString("PayStatus"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.showText("Total Cost: " + rs.getString("TotalCost"));
	    y -= 30;
	    contentStream.newLineAtOffset(0, -30);

	    contentStream.endText();
	    contentStream.close();

	    return document;
	}


	/**
	 * This method adds the client info to the database
	 *
	 * @param ip    The client's IP address
	 * @param host  The client's host
	 */
	public void insertClientInfo(String ip,String host){
		
		 // SQL INSERT statement
        String sql = "INSERT INTO g13.clientusers (IP, Host, Connection) VALUES (?, ?, ?)";
        
        try {
             PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set values for placeholders (?, ?, ?)
            pstmt.setString(1, ip);
            pstmt.setString(2, host);
            pstmt.setString(3, "Connected");


            // Execute the INSERT statement
            pstmt.executeUpdate();
       
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
		 		
	}
	

	/**
	 * This method retrieves client information from the database
	 * and returns a list of CurClient objects.
	 *
	 * @return The list of CurClient objects representing clients
	 */
	public ArrayList<CurClient> getClients()
	{
		ArrayList<CurClient> clients = new ArrayList<CurClient>();

		try 
		{	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM g13.clientusers;");
	 		while(rs.next())
	 		{
	            // Create a CurClient object with data from the result set
	 			CurClient curClient = new CurClient(rs.getString("IP"),rs.getString("Host"),rs.getString("Connection"));
	            // Add the CurClient object to the list of clients
	 			clients.add(curClient);
				
			} 
	 		
			rs.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
	    // Return the list of clients
		return clients;
	}
	
	
	

	/**
	 * This method deletes client details from the database based on the IP address.
	 *
	 * @param ip The IP address of the client to delete
	 */
	public void deleteHostInfo (String ip) {
		 // SQL DELETE statement
        String sql = "DELETE FROM g13.clientusers WHERE IP = ?";
        
        try {
                PreparedStatement pstmt = conn.prepareStatement(sql) ;
                pstmt.setString(1, ip);

               // Execute the DELETE statement
               pstmt.executeUpdate();
               
           } catch (SQLException e) {
               e.printStackTrace();
           }
		
		
	}
	
	
	
}

