package emailController;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * This class provides methods to send confirmation or waiting list update emails to visitors.
 */
public class EmailSender {

	 /**
     * Sends an email message with reservation confirmation or waiting list update.
     *
     * @param recipientEmail The recipient's email address.
     * @param orderNumber    The order number associated with the reservation.
     * @param parkName       The name of the park.
     * @param date           The date of the reservation.
     * @param time           The time of the reservation.
     * @param numberOfVisitors The number of visitors included in the reservation.
     * @param msgType        The type of message to be sent: "Confirmation" or "WaitingListUpdate".
     */
	public static void sendMessage(String recipientEmail, String orderNumber, String parkName, String date, String time, String numberOfVisitors, String msgType) {
	    // Sender's email credentials
	    String senderEmail = "go13nature@gmail.com";
	    String senderPassword = "chlr scpv vfhy xgsr";
	    String htmlContent="";

	    // SMTP server configuration for Gmail
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");

	    // Create a Session object with authentication
	    Session session = Session.getInstance(props, new Authenticator() {
	        @Override
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(senderEmail, senderPassword);
	        }
	    });

	    try {
	    	 // Create a MimeMessage object
	        MimeMessage message = new MimeMessage(session);
	        // Set the sender's email address
	        message.setFrom(new InternetAddress(senderEmail));
	        // Set the recipient's email address
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
	        
	        if (msgType.contains("Confirmation"))
	        {
	        // Set the email subject
	        message.setSubject("Reservation Confirmation #" + orderNumber);
	        htmlContent=confirmationEmail( orderNumber,  parkName,  date,  time,  numberOfVisitors);
	        message.setContent(htmlContent, "text/html");

	        }
	        else {
	        	message.setSubject("Order update #" + orderNumber);
		        htmlContent=WaitingListEmail( orderNumber,  parkName,  date,  time,  numberOfVisitors);
		        message.setContent(htmlContent, "text/html");
	        	 
	        }
	        // Send the email
	        Transport.send(message);

	        System.out.println("Email sent successfully!");
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
     * Generates HTML content for a reservation confirmation email.
     *
     * @param orderNumber     The order number associated with the reservation.
     * @param parkName        The name of the park.
     * @param date            The date of the reservation.
     * @param time            The time of the reservation.
     * @param numberOfVisitors The number of visitors included in the reservation.
     * @return HTML content for the reservation confirmation email.
     */
	public static String confirmationEmail(String orderNumber, String parkName, String date, String time, String numberOfVisitors) {
		
        // Set the email content as HTML
        String htmlContent = "<h4>Hello visitor!</h4>"
                + "<p>This is a reservation confirmation email from GoNature. Below are the details of your reservation:</p>"
                + "<ul>"
                + "<li>Order Number: " + orderNumber + "</li>"
                + "<li>Park Name: " + parkName + "</li>"
                + "<li>Date: " + date + "</li>"
                + "<li>Time: " + time + "</li>"
                + "<li>Number of Visitors: " + numberOfVisitors + "</li>"
                + "</ul>"
                + "<p><h5>You have 2 hours to respond, or your reservation will be cancelled automatically.</h5></p>"
                + "<p>Please <a href=\"mailto:go13nature@gmail.com?subject=Confirmation%20#" + orderNumber + "&body=Please%20confirm%20my%20reservation%20#" + orderNumber + "\">click here</a> to confirm your reservation.</p>"
                + "<p>If you wish to cancel your reservation, please <a href=\"mailto:go13nature@gmail.com?subject=Cancellation%20#" + orderNumber + "&body=Please%20cancel%20my%20reservation%20#" + orderNumber + "\">click here</a> to proceed with the cancellation.</p>";
        return htmlContent;
	}
	
	
    /**
     * Generates HTML content for a waiting list update email.
     *
     * @param orderNumber     The order number associated with the reservation.
     * @param parkName        The name of the park.
     * @param date            The date of the reservation.
     * @param time            The time of the reservation.
     * @param numberOfVisitors The number of visitors included in the reservation.
     * @return HTML content for the waiting list update email.
     */
	public static String WaitingListEmail(String orderNumber, String parkName, String date, String time, String numberOfVisitors) {
			
	        // Set the email content as HTML
	        String htmlContent = "<h4>Hello visitor!</h4>"
	                + "<p>Great news for you! you are no longer in the waiting list! your reservation has been saved.</p>"
	                + "<ul>"
	                + "<li>Order Number: " + orderNumber + "</li>"
	                + "<li>Park Name: " + parkName + "</li>"
	                + "<li>Date: " + date + "</li>"
	                + "<li>Time: " + time + "</li>"
	                + "<li>Number of Visitors: " + numberOfVisitors + "</li>"
	                + "</ul>"
	                + "<p><h5>You will receive a confirmation email one day before the date above.</h5></p>"
	                + "<p><h5>"+parkName+" is waiting for you! Good bye!</h5></p>";
	        return htmlContent;
		}
    
   
}
