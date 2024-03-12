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


public class mysqlConnection {
	
	
	private static Connection conn;

	public void main(String[] args) 
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        }
        
        ConnectToDB();
        System.out.println("SQL connection succeed");
        //getParkNames(StudentFormController.parkNames);
            //createTableCourses(conn);
            //updateArrivalTimeForFlights(conn);
     	
   	}
	
	/**
	   * This method connects us to the database.
	   *
	   
	   */
	private static void ConnectToDB() {
    	try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/g13?serverTimezone=IST","group13","13Group13");
		} catch (SQLException ex) {
			 System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
		}

		
	}
	
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


