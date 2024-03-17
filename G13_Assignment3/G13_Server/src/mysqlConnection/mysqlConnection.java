package mysqlConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*; 
import java.lang.*; 
import java.util.*;

import logic.CurClient;
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
	public User verifyWorkerLogin(User user)
	{
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM g13.users WHERE UserId=? AND Password=?");
			preparedStatement.setString(1, user.getId()); // 1-indexed parameter position
			preparedStatement.setString(2, user.getPassword()); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
            rs.next();
	        //order = new Order(rs.getString("ParkName"),rs.getString("OrderNumber"),rs.getString("TimeOfVisit"),rs.getString("NumberOfVisitors"),rs.getString("TelephoneNumber"),rs.getString("Email"))  ; 
            user.setFullName(rs.getString("FullName"));
            user.setEmail(rs.getString("Email"));
            user.setPhoneNumber(rs.getString("PhoneNumber"));
            user.setLogged(true);
            user.setUserPermission(rs.getString("UserPermission"));
            

	            
            
			rs.close();
			preparedStatement.close();
			
			
		} catch (SQLException e) {return user;}
		return user;
	}
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
	public User verifyVisitorLogin(User user)
	{
		try 
		{	
			// Prepare a statement with a placeholder
			PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) AS user_count FROM g13.users WHERE UserId=?");
			preparedStatement.setString(1, user.getId()); // 1-indexed parameter position

			// Execute the query
			ResultSet rs = preparedStatement.executeQuery();
			 // Process the result set
			// Process result set
            if (rs.next()) {
                int userCount = rs.getInt("user_count");
                if (userCount > 0) {
                	user.setPassword(rs.getString("Password"));
                	user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    user.setLogged(true);
                    user.setUserPermission(rs.getString("UserPermission"));
                } else {
                	try {
                		// SQL INSERT statement
                        String sql = "INSERT INTO g13.users (UserId, IsLogged, UserPermission) VALUES (?, ?, ?)";
                      
                        PreparedStatement pstmt = conn.prepareStatement(sql);

                       // Set values for placeholders (?, ?, ?)
                       pstmt.setString(1, user.getId());
                       pstmt.setBoolean(6, true);
                       pstmt.setString(7, "visitor");
                       

                       // Execute the INSERT statement
                       pstmt.executeUpdate();
                 
                      
                   } catch (SQLException e) {
                       return user;
                   }
                }
                return user;
            }
          

	            
          
			rs.close();
			preparedStatement.close();
			
			
		} catch (SQLException e) {return user;}
		return user;
	}
	
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
	public Park updatePark(Park park)
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
		return park;
	}
	
	/**
	   * This method adds all the orderNumbers from the database to the list it gets as a parameter.
	   *
	   * @param ordersList, an empty ArrayList
	   */
	public ArrayList<String> getParks(ArrayList<String> parksList)
	{
		parksList= new ArrayList<String>();
		try 
		{	
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT ParkName FROM g13.parks");
	 		while(rs.next())
	 		{
	 			
	 		
	 			parksList.add(rs.getString("ParkName"));
	 			//StudentFormController.addValue(rs.getString("ParkName"));
				
			} 
	 		
			rs.close();
			
		} catch (SQLException e) {e.printStackTrace();}
		return parksList;
		
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
          order.setVisitors(rs.getString("NumberOfVisitors"));
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
	            ps.setString(3,order.getVisitors());
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
            String sql = "INSERT INTO g13.orders (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors, PhoneNumber, Email, VisitorType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
          
            PreparedStatement pstmt = conn.prepareStatement(sql);

           // Set values for placeholders (?, ?, ?)
           pstmt.setString(1, order.getOrderNum());
           pstmt.setString(2, order.getParkName());
           pstmt.setString(3, order.getVisitorId());
           pstmt.setString(4, order.getDate());
           pstmt.setString(5, order.getTime());
           pstmt.setString(6, order.getVisitors());
           pstmt.setString(7, order.getTelephone());
           pstmt.setString(8, order.getEmail());
           pstmt.setString(9, order.getVisitorType());
                     

           // Execute the INSERT statement
           pstmt.executeUpdate();
     
          
       } catch (SQLException e) {
       }
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


