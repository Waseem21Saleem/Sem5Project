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

import logic.CurClient;
import logic.Message;
import logic.Order;
import logic.Park;
import logic.User;


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
	public ArrayList<String> getOrders(String visitorId)
	{
		ArrayList<String> ordersList= new ArrayList<String>();
		
		try 
		{	
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT OrderNumber FROM g13.orders WHERE VisitorId = ?");
			preparedStatement.setString(1, visitorId); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			// Process result set
	 		while(rs.next())
	 		{
	 			
	 		
	 			ordersList.add(rs.getString("OrderNumber"));
	 			//StudentFormController.addValue(rs.getString("ParkName"));
				
			} 
	 		
			rs.close();
			
		} catch (SQLException e) {e.printStackTrace();}
		return ordersList;
		
	}
	
	/**
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
	   */
	public Order getOrderInfo(Order order)
	{

		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.order WHERE OrderNumber=?");
			preparedStatement.setString(1, order.getOrderNum()); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
          rs.next();
	        //order = new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),rs.getString("TimeOfVisit"),rs.getString("NumberOfVisitors"),rs.getString("TelephoneNumber"),rs.getString("Email"))  ; 
          order.setParkName(rs.getString("ParkName"));
          order.setDate(rs.getString("Date"));
          order.setTime(rs.getString("Time"));
          order.setAmountOfVisitors(rs.getString("NumberOfVisitors"));
          order.setTelephone(rs.getString("PhoneNumber"));
          order.setEmail(rs.getString("Email"));
	            
          
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		return order;
	}
	
	
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
	public Order updateOrderInfo(Order order)
	{
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.orders SET ParkName=?,Date=?,Time=?,NumberOfVisitors=? WHERE OrderNumber = ?");
			 	ps.setString(1,order.getParkName());
	            ps.setString(2,order.getDate());
	            ps.setString(3,order.getTime());
	            ps.setString(3,order.getAmountOfVisitors());
	            ps.setString(3,order.getOrderNum());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
		return order;
	}
	
	/**
	   * This method deletes the client details from the database according to the ip
	   *
	   * @param ip, the client's ip
	   */
	public void deleteOrder(Order order) {
		 // SQL DELETE statement
      String sql = "DELETE FROM g13.orders WHERE OrderNumber = ?";
      
      try {
              PreparedStatement pstmt = conn.prepareStatement(sql) ;
              pstmt.setString(1, order.getOrderNum());

             // Execute the DELETE statement
             pstmt.executeUpdate();
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
		
		
	}
	
	
	public void insertOrder(Order order) {
		try {
    		// SQL INSERT statement
            String sql = "INSERT INTO g13.orders (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, PhoneNumber, Email, VisitorType, ExitTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
          
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
                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
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
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT NumberOfVisitors FROM g13.orders WHERE ParkName = ? AND Time < ? AND ExitTime > ? AND Date = ?");
			preparedStatement.setString(1, order.getParkName()); // 1-indexed parameter position
			preparedStatement.setString(2, order.getTime());
			preparedStatement.setString(3, order.getTime());
			preparedStatement.setString(4, order.getDate());
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
	        // Number of hours to add
	        int maxStay = getParkMaxStay(order.getParkName());
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
	
	public int getParkMaxStay(String parkName) {
		int maxStayTime=0;
		try {
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT MaxStay FROM g13.parks WHERE ParkName = ?");
			preparedStatement.setString(1, parkName);
			 // 1-indexed parameter position
			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			rs.next();
			maxStayTime = Integer.parseInt(rs.getString("MaxStay"));
			rs.close();
			preparedStatement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maxStayTime;
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
	public void updateRoleToGuide(User user)
	{
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.users SET UserPermission=? WHERE UserId = ?");
			 	ps.setString(1,"Guide");
	            ps.setString(2,user.getId());
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
	}
	
	/**
	   * This method deletes the client details from the database according to the ip
	   *
	   * @param ip, the client's ip
	   */
	public void deleteRequest(Park park) {
		 // SQL DELETE statement
    String sql = "DELETE FROM g13.requests WHERE ParkName = ?,Capacity = ?,MaxStay = ?";
    
    try {
            PreparedStatement pstmt = conn.prepareStatement(sql) ;
            pstmt.setString(1, park.getParkName());
            pstmt.setString(2, park.getCapacity());
            pstmt.setString(3, park.getMaxStay());

           // Execute the DELETE statement
           pstmt.executeUpdate();
           
       } catch (SQLException e) {
           e.printStackTrace();
       }
		
		
	}

	public ArrayList<String> getRequestInfo(String ParkName)
	{
		ArrayList<String> RequestInfo= new ArrayList<String>();

		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.requests WHERE ParkName=?");
			preparedStatement.setString(1, ParkName); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
            rs.next();
            RequestInfo.add(rs.getString("ParkName"));
            RequestInfo.add(rs.getString("Chang-Type"));
            RequestInfo.add(rs.getString("Status"));
            
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		return RequestInfo;
	}
	
	public void updateRequestInfo(String Status,String NewStatus){
			
			PreparedStatement ps;
			try {
				 	ps = conn.prepareStatement("UPDATE g13.request SET Status= ? WHERE ParkName = ?;");
		            ps.setString(1,NewStatus);
		            ps.executeUpdate();
		            ps.close();
		            
			} catch (SQLException e) {	e.printStackTrace();}
			 		
		}

	
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
/*	public String signUp(User user)
	{
		String message="";

		// SQL INSERT statement
        String sql = "INSERT INTO g13.users (UserId, Password, FullName, Email, PhoneNumber, IsLogged, UserPermission) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
             PreparedStatement pstmt = conn.prepareStatement(sql);

            // Set values for placeholders (?, ?, ?)
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhoneNumber());
            pstmt.setBoolean(6, user.isLogged());
            pstmt.setString(7, user.getUserPermission());
            

            // Execute the INSERT statement
            pstmt.executeUpdate();
            message="success";
           
        } catch (SQLException e) {
            message="error";
        }
        return message;
	}
*/
	
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
	public ArrayList<String> getOrders(ArrayList<String> ordersList)
	{
		ordersList= new ArrayList<String>();
		try 
		{	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM g13.order;");
	 		while(rs.next())
	 		{
	 			
	 		
	 			ordersList.add(rs.getString("OrderNumber"));
	 			//StudentFormController.addValue(rs.getString("ParkName"));
				
			} 
	 		
			rs.close();
			return ordersList;
			
		} catch (SQLException e) {e.printStackTrace();}
		return ordersList;
		
	}
	
	/**
	   * This method sets the order details from the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param order, a new order that has no info in it
	   */
	public ArrayList<String> getOrderInfo(String OrderNumber)
	{
		ArrayList<String> orderInfo= new ArrayList<String>();

		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.order WHERE OrderNumber=?");
			preparedStatement.setString(1, OrderNumber); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
            rs.next();
	        //order = new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),rs.getString("TimeOfVisit"),rs.getString("NumberOfVisitors"),rs.getString("TelephoneNumber"),rs.getString("Email"))  ; 
            orderInfo.add(rs.getString("ParkName"));
            orderInfo.add(rs.getString("OrderNumber"));
            orderInfo.add(rs.getString("TimeOfVisit"));
            orderInfo.add(rs.getString("NumberOfVisitors"));
            orderInfo.add(rs.getString("TelephoneNumber"));
            orderInfo.add(rs.getString("Email"));
	            
            
			rs.close();
			preparedStatement.close();
			
			
			
		} catch (SQLException e) {e.printStackTrace();}
		return orderInfo;
	}

	/**
	   * This method updates the order details to the database according to the OrderNumber
	   *
	   * @param OrderNumber, an ordernumber that the client selected
	   * @param newParkName, the new name 
	   * @param newTelphone, the new phone number
	   */
	public void updateOrderInfo(String orderNumber,String newParkName,String newTelphone){
		
		PreparedStatement ps;
		try {
			 	ps = conn.prepareStatement("UPDATE g13.order SET ParkName = ?,TelephoneNumber=? WHERE OrderNumber = ?;");
			 	ps.setString(1,newParkName);
	            ps.setString(2,newTelphone);
	            ps.setString(3,orderNumber);
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	e.printStackTrace();}
		 		
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


