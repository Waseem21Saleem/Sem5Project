package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class PaymentController {
	
	@FXML
	private Button btnPay = null;
	@FXML
	private ComboBox<String> cmbMonth;
	@FXML
	private ComboBox<String> cmbYear;
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
	
	
	//add months to the ComboBox
	public void setMonthComboBox(ActionEvent event) throws Exception {
       	cmbMonth.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
	}
	
	//add years to the ComboBox
	public void setYearComboBox(ActionEvent event) throws Exception {
	    cmbYear.getItems().addAll("2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2032", "2033", "2034");
	}
	
	public void confirmAndPayBtn(ActionEvent event) throws Exception {
		lengthErrorCardNumberMessage();
		notNumberErrorCardNumberMessage();
		lengthErrorCVCMessage();
		notNumberErroCVCMessage();
		lblSuccess.setText("Confirmed And Paid Successfully!");
	}
	
	//An error message will appear if the CVC's length is incorrect
	public void lengthErrorCVCMessage() {
		// Initialize the lblErrorCVC message to be invisible
		lblErrorCVC.setVisible(false);

		// Add a listener to txtCVC to check the length of the entered text
		txtCVC.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() != 3) {
				// If the length is not 3, display the error message
				lblErrorCVC.setText("CVC must be 3-digits!");
				lblErrorCVC.setVisible(true);
			} else {
				// If the length is 3, hide the error message
				lblErrorCVC.setVisible(false);
			}
		});
    }
	
	//An error message will appear if the CardNumber's length is incorrect
	public void lengthErrorCardNumberMessage() {
		// Initialize the lblErrorCardNum message to be invisible
		lblErrorCardNum.setVisible(false);

        // Add a listener to txtCardNum to check the length of the entered text
		txtCardNum.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() != 12) {
                // If the length is not 12, display the error message
            	lblErrorCardNum.setText("Card number must be 12-digits!");
            	lblErrorCardNum.setVisible(true);
            } else {
                // If the length is 12, hide the error message
            	lblErrorCardNum.setVisible(false);
            }
        });
    }
	
	public void notNumberErrorCardNumberMessage() {
		// Add listener to textProperty of the TextField
		txtCardNum.textProperty().addListener((observable, oldValue, newValue) -> {
			// Checks if the new value contains non-numeric characters
			if (!newValue.matches("\\d*")) { 
            	lblErrorNotANumInCard.setText("Card number must contain only numbers!");
            	lblErrorNotANumInCard.setVisible(true);
            } else {
            	lblErrorNotANumInCard.setVisible(false);
            }
        });
	}
	
	public void notNumberErroCVCMessage() {
		// Add listener to textProperty of the TextField
		txtCVC.textProperty().addListener((observable, oldValue, newValue) -> {
			// Checks if the new value contains non-numeric characters
			if (!newValue.matches("\\d*")) { 
				lblErrorNotANumInCVC.setText("CVC must contain only numbers!");
				lblErrorNotANumInCVC.setVisible(true);
            } else {
            	lblErrorNotANumInCVC.setVisible(false);
            }
        });
	}
}
