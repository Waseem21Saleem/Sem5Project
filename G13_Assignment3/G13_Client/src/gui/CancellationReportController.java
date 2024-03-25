package gui;

import java.net.URL;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.Report;

public class CancellationReportController implements Initializable {
	@FXML
	private Label lblParkName,lblReportType,labelInputMonth,labelInputYear,labelOrderType,lblFirstType;
	@FXML
	private Label lblSecondAnswer,lblSecondType, lblFirstAnswer;
	@FXML
	private PieChart pieChart ;
	
	
	public void goBack(ActionEvent event) throws Exception {
		
		ChatClient.openGUI.goToGUI(event, "/gui/DepartmentManagerHomePage.fxml","","Department manager home page");
		 
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		Report report = ChatClient.report;
		lblParkName.setText(report.getParkName());
		lblReportType.setText(report.getReportType());
		labelInputMonth.setText(report.getMonth());
		labelInputYear.setText(report.getYear());
		lblFirstAnswer.setText(report.getDetails().get(0));
		lblSecondAnswer.setText(report.getDetails().get(1)); 
		if (report.getReportType().equals("cancellation report")) {
			labelOrderType.setText("Cancel Type");
			lblFirstType.setText("Cancelled Manually");
			lblSecondType.setText("Cancelled Automatically");
		}
		else {
			labelOrderType.setText("Visitor Type");
			lblFirstType.setText("Individuals");
			lblSecondType.setText("Organised groups");
			
		}
	
	}
	
}
