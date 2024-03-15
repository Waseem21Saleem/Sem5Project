package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PaymentController {
	
	@FXML
	private Button btnPay;
	@FXML
	private ComboBox cmbMonth;
	@FXML
	private ComboBox cmbYear;
	@FXML
	private Label lblCardNumber;
	@FXML
	private Label lblCVC;
	@FXML
	private Label lblExpDate;
	@FXML
	private Label lblSuccess;
	@FXML
	private Label lblVisaDetails;
	@FXML
	private Label lblErrorCardNum;
	@FXML
	private Label lblErrorCVC;
	@FXML
	private Label lblErrorNotANumInCard;
	@FXML
	private Label lblErrorNotANumInCVC;
	@FXML
	private TextField txtCardNum;
	@FXML
	private TextField txtCVC;
	
	/*
	public void setMonthComboBox(ActionEvent event) throws Exception {
		// Load the FXML file
		 Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml"));
        
        // Set up the scene
		Stage primaryStage = new Stage();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
       	cmbMonth.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
	}
	
	public void setYearComboBox(ActionEvent event) throws Exception {
		
	}
	*/
	public void confirmAndPayBtn(ActionEvent event) throws Exception {
		if(event.getSource() == btnPay) { 
			lblSuccess.setText("Confirmed And Paid Successfully!");
		}
	}
	
	//An error message will appear if the CVC's length is incorrect or if it contains a character thats not a number
	public void errorCVCMessage(ActionEvent event) throws Exception {
		// Initialize lblErrorCVC to be invisible
		lblErrorCVC.setVisible(false);

        // Initialize lblErrorNotANumInCVC to be invisible
		lblErrorNotANumInCVC.setVisible(false);

        // Add a listener to the txtCVC to check for non-numeric characters and enforce 3-digit length
		txtCVC.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}")) {
                // If the new value contains non-numeric characters or not 3 digits, prevent it from being entered
            	txtCVC.setText(oldValue);
            } else {
                // If the length is not exactly 3, display the error message
                if (newValue.length() != 3) {
                	lblErrorCVC.setText("CVC must be 3-digits!");
                	lblErrorCVC.setVisible(true);
                	lblErrorNotANumInCVC.setVisible(false); // Hide lblErrorNotANumInCVC message
                } else {
                    // If the length is 3, hide lblErrorCVC message and display lblErrorNotANumInCVC message
                	lblErrorCVC.setVisible(false);
                	lblErrorNotANumInCVC.setText("CVC must contain only numbers!");
                	lblErrorNotANumInCVC.setVisible(true);
                }
            }
        });
    }
	
	//An error message will appear if the CardNumber's length is incorrect or if it contains a character thats not a number
		public void errorCardNumberMessage(ActionEvent event) throws Exception {
			// Initialize lblErrorCardNum to be invisible
			lblErrorCardNum.setVisible(false);

	        // Initialize lblErrorNotANumInCard to be invisible
			lblErrorNotANumInCard.setVisible(false);

	        // Add a listener to the txtCardNum to check for non-numeric characters and enforce 12-digit length
			txtCardNum.textProperty().addListener((observable, oldValue, newValue) -> {
	            if (!newValue.matches("\\d{0,12}")) {
	                // If the new value contains non-numeric characters or exceeds 12 digits, prevent it from being entered
	            	txtCardNum.setText(oldValue);
	            } else {
	                // If the length is not exactly 12, display the error message
	                if (newValue.length() != 12) {
	                	lblErrorCardNum.setText("Card number must be 12-digits!");
	                	lblErrorCardNum.setVisible(true);
	                	lblErrorNotANumInCard.setVisible(false); // Hide lblErrorNotANumInCard message
	                } else {
	                    // If the length is 12, hide lblErrorCardNum message and display lblErrorNotANumInCard message
	                	lblErrorCardNum.setVisible(false);
	                	lblErrorNotANumInCard.setText("Card number must contain only numbers!");
	                	lblErrorNotANumInCard.setVisible(true);
	                }
	            }
	        });
	    }
}
