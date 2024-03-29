package mysqlConnection;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import com.mysql.cj.Session;

import emailController.EmailReciever;
import emailController.EmailSender;
import logic.Notification;
import logic.Order;

/**
 * This class implements the Runnable interface to perform various database operations in separate threads.
 */
public class RunnableSql implements Runnable  {
	private Connection connection;
	
	/**
     * Run method to execute the database operations in separate threads.
     */
	@Override
	public void run() {

        // Create threads for different database operations

        Thread orderThread = new Thread(() -> checkOrders());
        Thread notificationThread = new Thread(() -> checkNotifications());
        Thread orderInsideThread = new Thread(()-> updateOrderInside());
        Thread orderCompletedThread = new Thread(()-> updateOrderCompleted());
        Thread deleteWaitingListThread = new Thread(()-> deleteWaitingList());
        // Start threads
        orderThread.start();
        notificationThread.start();
        orderInsideThread.start();
        orderCompletedThread.start();
        deleteWaitingListThread.start();

        try {
            // Join threads to wait for them to complete
            orderThread.join();
            notificationThread.join();
            orderInsideThread.join();
            orderCompletedThread.join();
            deleteWaitingListThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	    
	}


    /**
     * Returns the current time minus X hours formatted as HH:mm.
     * @param x The number of hours to subtract.
     * @return The formatted time string.
     */
	public static String getCurrentTimeMinusX(long x) {
		// Get the current time
        LocalTime currentTime = LocalTime.now();
        // Subtract 2 hours from the current time
        LocalTime modifiedTime = currentTime.minusHours(x);
        // Define the format for hh:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Format the modified time
        String formattedTime = modifiedTime.format(formatter);
        
        return formattedTime;
	}
	
	
    /**
     * Returns the current time plus X hours formatted as HH:mm.
     * @param x The number of hours to add.
     * @return The formatted time string.
     */
	public static String getCurrentTimePlusX (long x) {
		// Get the current time
        LocalTime currentTime = LocalTime.now();
        // Subtract 2 hours from the current time
        LocalTime modifiedTime = currentTime.plusHours(x);
        // Define the format for hh:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        // Format the modified time
        String formattedTime = modifiedTime.format(formatter);
        
        return formattedTime;
	}
	

	   /**
     * Returns the current date plus X days formatted as dd-MM-yy.
     * @param x The number of days to add.
     * @return The formatted date string.
     */
	public static String getCurrentDatePlusX(long x) {
		
		LocalDate today = LocalDate.now().plusDays(x);
        // Define a formatter for "dd-MM-yy" format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        // Format the date using the formatter
        String todayDateString = today.format(formatter);
        
        return todayDateString;
	}

    /**
     * Sets the connection to the database.
     * @param connection The connection to the database.
     */
	public void setConnection(Connection connection) {
		this.connection=connection;
	}
	
    /**
     * Checks notifications where 2 hours have passed, then deletes and updates orders accordingly.
     */
	private void checkNotifications() {
		 while (true) {
		String query = "SELECT * FROM g13.notifications WHERE Time = ?";
		String formattedTime=getCurrentTimeMinusX(2);
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	        	
 
	            statement.setString(1, formattedTime);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                	String orderNumber=resultSet.getString("OrderNumber");
	                	String result=EmailReciever.checkResponse(orderNumber);
	                	deleteNotification(orderNumber);
	                	Order order = new Order(orderNumber);
                		logic.Message msg = mysqlConnection.getOrderInfo(order);
                		order=(Order)msg.getContent();
	                	if (result.equals("NoResponse")) {
	                		mysqlConnection.checkWaitingList(order);
	                		changeOrderStatus(orderNumber,"cancelled automatically");
	                	}
	                	else if (result.equals("Cancellation")) {
	                		mysqlConnection.checkWaitingList(order);
	                		changeOrderStatus(orderNumber,"cancelled manually");
	                	}
	                	else
	                		changeOrderStatus(orderNumber,"confirmed");


	                }
	            }
	            Thread.sleep(60000);  // Sleep for 1 minute (adjust as needed)
	        } catch (SQLException | InterruptedException e) {
	            e.printStackTrace();
	            // Handle SQLException or InterruptedException
	        }
	    }
	    
	}
	
    /**
     * Deletes a notification from the notifications table.
     * @param orderNumber The order number of the notification to be deleted.
     */
	private void deleteNotification(String orderNumber) {
		 // SQL DELETE statement
        String sql = "DELETE FROM g13.notifications WHERE OrderNumber = ?";
        
        try {
                PreparedStatement pstmt = connection.prepareStatement(sql) ;
                pstmt.setString(1, orderNumber);

               // Execute the DELETE statement
               pstmt.executeUpdate();
               
           } catch (SQLException e) {
               e.printStackTrace();
           }
	}

	  /**
     * Deletes rows from the waitinglist where the date equals yesterday.
     */
	private void deleteWaitingList() {
		while (true) {
       String sql = "DELETE FROM g13.waitinglist WHERE Time = ? AND Date = ?";
       LocalDate yesterday = LocalDate.now().minusDays(1);
       // Define a formatter for "dd-MM-yy" format
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
       // Format the date using the formatter
       String yesterdayDateString = yesterday.format(formatter);
       String formattedTime=getCurrentTimeMinusX(0);
       try {
               PreparedStatement pstmt = connection.prepareStatement(sql) ;
               pstmt.setString(1, formattedTime);
               pstmt.setString(2, yesterdayDateString);

              // Execute the DELETE statement
              pstmt.executeUpdate();
              
          } catch (SQLException e) {
              e.printStackTrace();
          }
		}
	}
	 

	  /**
     * Updates the order status to the given orderStatus.
     * @param orderNumber The order number of the order to be updated.
     * @param orderStatus The new status of the order.
     */
	private void changeOrderStatus(String orderNumber,String orderStatus) {
		PreparedStatement ps;
		try {
			 	ps = connection.prepareStatement("UPDATE g13.orders SET OrderStatus=? WHERE OrderNumber = ?");
	            ps.setString(1,orderStatus);
	            ps.setString(2,orderNumber);
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
	}
	

	/**
     * Updates the order status to "inside" when the start time occurs.
     */
	private void updateOrderInside() {
		while (true) {
		
		
		String todayDateString = getCurrentDatePlusX(0);
        String formattedTime=getCurrentTimeMinusX(0);
        
		PreparedStatement ps;
		try {
			 	ps = connection.prepareStatement("UPDATE g13.orders SET OrderStatus=? WHERE Time = ? AND Date=? AND OrderStatus= ?");
	            ps.setString(1,"inside");
	            ps.setString(2,formattedTime);
	            ps.setString(3,todayDateString);
	            ps.setString(4,"confirmed");
	            ps.executeUpdate();
	            ps.close();
	            
		} catch (SQLException e) {	}
		}
	}
	  /**
     * Updates the order status to "completed" when the exit time occurs.
     */
	private void updateOrderCompleted() {
		while (true) {
			String todayDateString = getCurrentDatePlusX(0);
		    String formattedTime=getCurrentTimeMinusX(0);
			PreparedStatement ps;
			try {
				 	ps = connection.prepareStatement("UPDATE g13.orders SET OrderStatus=? WHERE ExitTime = ? AND Date=? AND OrderStatus= ?");
		            ps.setString(1,"completed");
		            ps.setString(2,formattedTime);
		            ps.setString(3,todayDateString);
		            ps.setString(4,"inside");
		            ps.executeUpdate();
		            ps.close();
	            
		} catch (SQLException e) {	}
		}
	}
	
	 /**
     * Checks orders for the next day and sends confirmation emails to visitors.
     */
	private void checkOrders() {
		Notification notification;
	    String query = "SELECT * FROM g13.orders WHERE Date = ? AND OrderStatus != ? AND Time = ?";
	    while (true) {
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	        	String tomorrowDateString = getCurrentDatePlusX(1);
			    String formattedTime=getCurrentTimeMinusX(0);

	            // Set the date parameter in the prepared statement
	            statement.setString(1, tomorrowDateString);
	            statement.setString(2, "cancelled manually");
	            statement.setString(3, formattedTime);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    notification = new Notification();
	                    notification.setParkName(resultSet.getString("ParkName"));
	                    notification.setOrderNum(resultSet.getString("OrderNumber"));
	                    notification.setVisitorId(resultSet.getString("VisitorId"));
	                    notification.setDate(resultSet.getString("Date"));
	                    notification.setTime(resultSet.getString("Time"));
	                    notification.setAmountOfVisitors(resultSet.getString("NumberOfVisitors"));
	                    EmailSender.sendMessage(resultSet.getString("Email"),resultSet.getString("OrderNumber"),resultSet.getString("ParkName"),resultSet.getString("Date"),resultSet.getString("Time"),resultSet.getString("NumberOfVisitors"),"Confirmation");
	                    insertNotification( notification);
	                }
	            }
	            Thread.sleep(60000);  // Sleep for 1 minute (adjust as needed)
	        } catch (SQLException | InterruptedException e) {
	            e.printStackTrace();
	            // Handle SQLException or InterruptedException
	        }
	    }
	}
	
    /**
     * Inserts a notification into the notifications table.
     * @param notification The notification to be inserted.
     * @throws SQLException if a database access error occurs or this method is called on a closed connection.
     */
    private void insertNotification( Notification notification) throws SQLException {
        String insertQuery = "INSERT INTO g13.notifications (OrderNumber, ParkName, VisitorId, Date, Time, NumberOfVisitors) VALUES (?,?,?,?,?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, notification.getOrderNum());
            statement.setString(2, notification.getParkName());
            statement.setString(3, notification.getVisitorId());
            statement.setString(4, notification.getDate());
            statement.setString(5, notification.getTime());
            statement.setString(6, notification.getAmountOfVisitors());
            statement.executeUpdate();
        }
    }
    
    

}



    