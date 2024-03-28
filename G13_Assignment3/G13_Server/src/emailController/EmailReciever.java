package emailController;


import java.util.Properties;

import javax.mail.*;


public class EmailReciever {

   public static String checkResponse(String orderNumber) 
   {
	  String host = "pop.gmail.com";// change accordingly
      String mailStoreType = "pop3";
      String username = "go13nature@gmail.com";// change accordingly
      String password = "chlr scpv vfhy xgsr";// change accordingly
      try {

      //create properties field
      Properties properties = new Properties();

      properties.put("mail.pop3.host", host);
      properties.put("mail.pop3.port", "995");
      properties.put("mail.pop3.starttls.enable", "true");
      Session emailSession = Session.getDefaultInstance(properties);
  
      //create the POP3 store object and connect with the pop server
      Store store = emailSession.getStore("pop3s");

      store.connect(host, username, password);

      //create the folder object and open it
      Folder emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_ONLY);

      // retrieve the messages from the folder in an array and print it
      Message[] messages = emailFolder.getMessages();
      for (int i = 0, n = messages.length; i < n; i++) {
         Message message = messages[i];
         if (message.getSubject().equals("Confirmation #"+orderNumber)) {
        	 return "Confirmation";
         }
         if (message.getSubject().equals("Cancellation #"+orderNumber)) {
        	return "Cancellation";
         }
         
      }

      //close the store and folder objects
      emailFolder.close(false);
      store.close();

      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      
      return "NoResponse";
   }



}