package mysqlConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.*; 
import java.lang.*; 
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import logic.CurClient;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.Report;
import logic.Request;
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
	public void updatePark(Park park)
	{
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.parks SET Capacity=?,MaxStay=? WHERE ParkName = ?");
			 	ps.setString(1,park.getCapacity());
	            ps.setString(2,park.getMaxStay());
	            ps.setString(3,park.getParkName());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
	}
	
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
	public Message getOrderInfo(Order order)
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


	    
	           
	            
		} catch (SQLException e) {	}
        msg = new Message (Message.ActionType.ERROR,"Cancelled order successfully");
		return msg;
		
		
	}
	
	
	public void insertOrder(Order order) {
		try {
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.orders (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, PhoneNumber, Email, VisitorType, ExitTime, OrderStatus, PayStatus, TotalCost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
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
			int placement=getPlacement(order);
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
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		
		
		return placement;
		
		
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
            	
           
	        }} catch (SQLException e) {
	            e.printStackTrace();
	        }

		msg = new Message (Message.ActionType.WAITINGLISTTABLE,waitingListEntries);
		return msg;
		
		
	}
	/**
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
	   */
	public Message checkReservation(Order order)
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
			preparedStatement = conn.prepareStatement("SELECT Capacity FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			// Execute the query
			rs = preparedStatement.executeQuery();
			rs.next();
			int maxCapacity = Integer.parseInt(rs.getString("Capacity"));
			rs.close();
			preparedStatement.close();
			
			if (currentAmountOfVisitors>maxCapacity)
			{
				msg = new Message (Message.ActionType.WAITINGLIST,order);
				return msg;
			}
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
			insertOrder(order);
			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.RESERVATION,"Reservation saved successfully");
		return msg;
	}
	
	/**
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
	   */
	public Message checkUpdatedReservation(Order order)
	{	//this function to make reservation
		Message msg;
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time <= ? AND ExitTime >= ? AND Date = ? AND OrderStatus!=? AND OrderStatus!=? AND OrderNumber != ?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
			preparedStatement.setString(5, "cancelled automatically");
			preparedStatement.setString(6, "cancelled manually");
			preparedStatement.setString(7, order.getOrderNum());

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
			preparedStatement = conn.prepareStatement("SELECT Capacity FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			// Execute the query
			rs = preparedStatement.executeQuery();
			rs.next();
			int maxCapacity = Integer.parseInt(rs.getString("Capacity"));
			rs.close();
			preparedStatement.close();
			if (currentAmountOfVisitors>maxCapacity)
			{
				msg = new Message (Message.ActionType.WAITINGLIST,order);
				return msg;
			}
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
			updateOrderInfo(order);

			
			
		} catch (SQLException e) {e.printStackTrace();}
		msg = new Message (Message.ActionType.RESERVATION,"Reservation saved successfully");
		return msg;
	}

	
	
	public Message getParkInfo(String parkName) {
		Message msg;
		Park park = new Park(parkName);
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, park.getParkName());
			 // 1-indexed parameter position
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			park.setCapacity(rs.getString("Capacity"));
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

			 	ps = conn.prepareStatement("UPDATE g13.parks SET Capacity=?,MaxStay=? WHERE ParkName = ?");
			 	ps.setString(1,request.getCapacity());
	            ps.setString(2,request.getMaxStay());
	            ps.setString(3,request.getParkName());

	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {System.out.println("Error");	}
	}
	
	public int getRandomOrderNumber() {
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
            String sql = "INSERT INTO g13.requests (ParkName, Capacity, MaxStay, Status) VALUES (?, ?, ?, ?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);
          
           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1,park.getParkName() );
           pstmt.setString(2, park.getCapacity());
           pstmt.setString(3, park.getMaxStay());
           pstmt.setString(4, "waiting for approval");

                     

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
			 	ps = conn.prepareStatement("UPDATE g13.requests SET Status=? WHERE ParkName = ? AND Capacity = ? AND MaxStay = ?");
			 	ps.setString(1,request.getStatus());
	            ps.setString(2,request.getParkName());
	            ps.setString(3,request.getCapacity());
	            ps.setString(4,request.getMaxStay());

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
            		    rs.getString("Capacity"),
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

