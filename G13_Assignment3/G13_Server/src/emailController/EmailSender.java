package emailController;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

	public static void sendMessage(String recipientEmail, String orderNumber, String parkName, String date, String time, String numberOfVisitors) {
	    // Sender's email credentials
	    String senderEmail = "go13nature@gmail.com";
	    String senderPassword = "chlr scpv vfhy xgsr";

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
	        // Set the email subject
	        message.setSubject("Reservation Confirmation #" + orderNumber);

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
	        message.setContent(htmlContent, "text/html");

	        // Send the email
	        Transport.send(message);

	        System.out.println("Email sent successfully!");
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
    
   
}
