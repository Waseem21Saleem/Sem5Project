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
		checkCardNumberMessage();
		checkCVCMessage();
		lblSuccess.setText("Confirmed And Paid Successfully!");
	}
	
	//An error message will appear if the CVC's length is incorrect
	public void checkCVCMessage() {
		if (txtCVC.getText().length() != 3) {
            lblErrorCVC.setText("CVC must be 3-digits!");
            lblErrorCVC.setVisible(true);
        } else if (!txtCVC.getText().matches("\\d*")) {
            lblErrorNotANumInCVC.setText("CVC must contain only numbers!");
            lblErrorNotANumInCVC.setVisible(true);
        }
    }
	
	//An error message will appear if the CardNumber's length is incorrect
	public void checkCardNumberMessage() {
		if (txtCardNum.getText().length() != 12) {
            lblErrorCardNum.setText("Card number must be 12-digits!");
            lblErrorCardNum.setVisible(true);
        } else if (!txtCardNum.getText().matches("\\d*")) {
            lblErrorNotANumInCard.setText("Card number must contain only numbers!");
            lblErrorNotANumInCard.setVisible(true);
        }
    }
	
	
	
}
