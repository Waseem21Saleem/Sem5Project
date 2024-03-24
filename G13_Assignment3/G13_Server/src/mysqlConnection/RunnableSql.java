/*package mysqlConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RunnableSql implements Runnable  {
	@Override
    public void run() {
        try (Connection connection =  ) {
            String query = "SELECT * FROM orders WHERE DATEDIFF(Date, CURDATE()) = 1";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String orderNumber = resultSet.getString("OrderNumber");
                    String parkName = resultSet.getString("ParkName");
                    // Retrieve other order details as needed

                    // Insert into notifications table
                    insertNotification(connection, orderNumber, parkName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    private void insertNotification(Connection connection, String orderNumber, String parkName) throws SQLException {
        String insertQuery = "INSERT INTO notifications (OrderNumber, ParkName, NotificationDate) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, orderNumber);
            statement.setString(2, parkName);
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        }
    }

}

*/

    