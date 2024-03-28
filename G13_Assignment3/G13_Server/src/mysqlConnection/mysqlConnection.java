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

import emailController.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import logic.CurClient;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.Report;
import logic.Request;
import logic.UsageVisitingReport;
import logic.User;
import logic.WaitingListEntry;


public class mysqlConnection {
	
	
	private static Connection conn;

	/**
	   * This Constructor connects to DataBase with Connection conn
	   *
	   
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
	
	public Connection getConn() {
		return conn;
	}
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
/*	public void updatePark(Park park)
	{
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.parks SET ReservedCapacity=?,TotalCapacity=?,MaxStay=? WHERE ParkName = ?");
			 	ps.setString(1,park.getReservedCapacity());
			 	ps.setString(2,park.getTotalCapacity());
	            ps.setString(3,park.getMaxStay());
	            ps.setString(4,park.getParkName());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
	}
	*/
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method deletes the client details from the database according to the ip
	   *
	   * @param ip, the client's ip
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

		msg = new Message (Message.ActionType.WAITINGLISTTABLE,waitingListEntries);
		return msg;
		
		
	}
	
	public Message getAlternativeDate (Order order) {
		//this function to make reservation
		Order tempOrder = order;
		String curTime= order.getTime();
		Message msg;
		ArrayList<Order> alternative = new ArrayList<Order>();
		int i=0;
		while (i<5)
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
							System.out.println(order.toString());
							order=new Order(tempOrder.getParkName(),tempOrder.getOrderNum(),tempOrder.getVisitorId(),tempOrder.getVisitorType(),tempOrder.getDate(),curTime,tempOrder.getAmountOfVisitors(),tempOrder.getTelephone(),tempOrder.getEmail(),tempOrder.getExitTime(),tempOrder.getTotalCost(),tempOrder.getPayStatus(),tempOrder.getOrderStatus());
							System.out.println(curTime);
							}
					}
					
					
				}
				
				
				
			} 
			catch (SQLException e) 
				{e.printStackTrace();}
		}
		msg = new Message (Message.ActionType.ALTERNATIVEDATE,alternative);
		return msg;
	}
	private Boolean checkAlternativeOrder(Order order,ArrayList<Order> alternative) {
		for (int i=0;i<alternative.size();i++)
		{
			if (order.getDate().equals(alternative.get(i).getDate())&& order.getTime().equals(alternative.get(i).getTime()))
				return false;
		}
		return true;
	}
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
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
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
			if (exitTimeAmountOfVisitors>maxCapacity)
			{
				msg = new Message (Message.ActionType.WAITINGLIST,order);
				return msg;
			}
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.RESERVATION,"Reservation saved successfully");
		return msg;
	}
	
	public static Message insertReservation(Order order) {
		Message msg = checkReservation(order);
		if (msg.getActionType().equals(Message.ActionType.RESERVATION))
			insertOrder(order);
		return msg;
	}
	
	public Message updateReservation(Order order) {
		Message msg = checkReservation(order);
		if (msg.getActionType().equals(Message.ActionType.RESERVATION))
			updateOrderInfo(order);
		return msg;
	}
	
	
	
	public static Message getParkInfo(String parkName) {
		Message msg;
		Park park = new Park(parkName);
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, park.getParkName());
			 // 1-indexed parameter position
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			park.setReservedCapacity(rs.getString("ReservedCapacity"));
			park.setTotalCapacity(rs.getString("TotalCapacity"));
			park.setMaxStay(rs.getString("MaxStay"));
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg = new Message (Message.ActionType.PARKINFO,park);
		return msg;
	}
	
	public void updateParkInfo(Request request)
	{
		PreparedStatement ps;
		try {

			 	ps = conn.prepareStatement("UPDATE g13.parks SET ReservedCapacity=?,TotalCapacity=?,MaxStay=? WHERE ParkName = ?");
			 	ps.setString(1,request.getReservedCapacity());
			 	ps.setString(2,request.getTotalCapacity());
	            ps.setString(3,request.getMaxStay());
	            ps.setString(4,request.getParkName());

	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {System.out.println("Error");	}
	}
	
	public static int getRandomOrderNumber() {
		// Create an instance of Random
        Random random = new Random();
        // Generate a random number of 8 digits
        int randomNumber = random.nextInt(90000000) + 10000000;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE OrderNumber = ?");
			preparedStatement.setString(1, Integer.toString(randomNumber));
			ResultSet rs = preparedStatement.executeQuery();
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
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
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
	   * This method deletes the client details from the database according to the ip
	   *
	   * @param ip, the client's ip
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

		msg = new Message (Message.ActionType.REQUESTSTABLE,requests);
		return msg;
		
		
	}

	public void CreateCancellationReport(Report report)
	{	
		int manually=0,automatically=0;
		try {
			String date = report.getMonth()+"-"+report.getYear();
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT OrderStatus FROM g13.orders WHERE ParkName = ? AND Date LIKE ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
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
		
	}
	
	public void CreateTotalVisitorsReport(Report report)
	{	
		int organized=0,others=0;
		try {
			String date = report.getMonth()+"-"+report.getYear();
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT VisitorType,OrderStatus,NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Date LIKE ?");
			preparedStatement.setString(1, report.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, "%" + date); // Use "%" to match any characters before the date pattern
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
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
		
	}
	
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
				    	System.out.println(exitRange);
				    	int index=getIndexOfStringInArrayList(exitRange,usageList);
				    	if (index!=-1)
				    		usageList.get(index).setNotFullOrExitCounter(usageList.get(index).getNotFullOrExitCounter()+Integer.parseInt(order.getAmountOfVisitors()));
				    System.out.println(order.getTime()+" "+order.getExitTime()+" "+ usageList.get(j).getFullOrEnterCounter()+" "+ usageList.get(j).getNotFullOrExitCounter()+" "+visitorType+" "+index);
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

			// Step 7: Insert usage report data into g13.usagereport table
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
	public void createVisitingReport(Report report) {
		createVisitingReportForVisitorType(report,"individuals and families");
		createVisitingReportForVisitorType(report,"organized group");

	}
	public void createUsageReport(Report report) {
		try {
			// Step 1: Retrieve reserved capacity for the park
			Message parkInfoMsg = getParkInfo(report.getParkName());
	        Park park = (Park) ((Message) parkInfoMsg).getContent();
	        int reservedCapacity=Integer.parseInt(park.getReservedCapacity());

			// Step 2: Query orders for the specified month, year, and park name
	        String date = report.getMonth()+"-"+report.getYear();
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.orders WHERE ParkName = ? AND Date LIKE ? AND OrderStatus!=? AND OrderStatus!=?");
			
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
		
	}
	
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
	        
	        while (rs.next()) {
	            rs.close();
	            preparedStatement.close();
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
	// Method to calculate total visitors for each time range
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
	// Method to calculate time range based on the order time
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
	// Method to calculate time range based on the order time
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

	// Method to create an Order object from the ResultSet
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
	   * This method adds the client info to the database
	   *
	   * @param ip, the client's ip
	   * @param host, the client's host
	   */
	public void insertClientInfo(String ip,String host){
		
		 // SQL INSERT statement
        String sql = "INSERT INTO g13.clientusers (IP, Host, Connection) VALUES (?, ?, ?)";
        
        try {
             PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set values for placeholders (?, ?, ?)
            pstmt.setString(1, ip);
            pstmt.setString(2, host);
            pstmt.setBoolean(3, true);


            // Execute the INSERT statement
            pstmt.executeUpdate();
       
           
        } catch (SQLException e) {
            e.printStackTrace();
        }
		 		
	}
	
	/**
	   * This method adds the HostNames to the hostList from the dataBase
	   *
	   * @param hostList, an empty ArrayList
	   */
	public ArrayList<String>  getHostNames(ArrayList<String> hostList)
	{
		
		try 
		{	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM g13.clientusers;");
	 		while(rs.next())
	 		{
	 			
	 		
	 			hostList.add(rs.getString("Host"));
	 			//StudentFormController.addValue(rs.getString("ParkName"));
				
			} 
	 		
			rs.close();
			return hostList;
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		return hostList;
	}
	
	/**
	   * This method sets the client details to curClient from the database according to the host name
	   *
	   * @param host, the client's hostname
	   * @param curClient, a new CurClient with no info in it
	   */
	public void getHostInfo(String host,CurClient curClient)
	{
		
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.clientusers WHERE Host=?");
			preparedStatement.setString(1, host); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
            rs.next();
	        //order = new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),rs.getString("TimeOfVisit"),rs.getString("NumberOfVisitors"),rs.getString("TelephoneNumber"),rs.getString("Email"))  ; 
            curClient.setIp(rs.getString("IP"));
            curClient.setHost(host);
            curClient.setConnection(rs.getBoolean("Connection"));

	            
            
			rs.close();
			preparedStatement.close();
			
			
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	/**
	   * This method deletes the client details from the database according to the ip
	   *
	   * @param ip, the client's ip
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

