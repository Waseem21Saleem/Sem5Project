package gui;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.Report;

public class CancellationReportController implements Initializable {
	@FXML
	private Label lblParkName,lblReportType,labelInputMonth,labelInputYear,labelOrderType,lblFirstType;
	@FXML
	private Label lblSecondAnswer,lblSecondType, lblFirstAnswer,lblAverageAnswer,lblTotal,lblAverage,lblTotalAnswer;
	@FXML
	private PieChart pieChart ;
	private PieChart.Data slice1,slice2;
	
	
	public void goBack(ActionEvent event) throws Exception {
		
		ChatClient.openGUI.goToGUI(event, "/gui/DepartmentManagerHomePage.fxml","","Department manager home page");
		 
	}
	
	private double getAverage (String year,String month,double total) {
		YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
        int monthLength=yearMonthObject.lengthOfMonth();
        return total/monthLength;
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
        double total=Integer.parseInt(report.getDetails().get(0))+Integer.parseInt(report.getDetails().get(1));
        double average= getAverage(labelInputYear.getText(),labelInputMonth.getText(),total);
        lblTotalAnswer.setText(String.valueOf(total));
        lblAverageAnswer.setText(String.format("%.2f",average));
        if (report.getReportType().equals("Cancellation report")) {
	    	

	        labelOrderType.setText("Cancel Type");
	        lblFirstType.setText("Cancelled Manually");
	        lblSecondType.setText("Cancelled Automatically");
	        lblAverage.setText("Cancel/day:");
	        // Add data to the PieChart
	        slice1 = new PieChart.Data("Cancelled Manually", Integer.parseInt(lblFirstAnswer.getText()));
	        slice2 = new PieChart.Data("Cancelled Automatically", Integer.parseInt(lblSecondAnswer.getText()));

	    } else {
	        labelOrderType.setText("Visitor Type");
	        lblFirstType.setText("Individuals and families");
	        lblSecondType.setText("Organised groups");
	        lblAverage.setText("Visitor/day:");
	        // Add data to the PieChart
	        slice1 = new PieChart.Data("Non-organised", Integer.parseInt(lblFirstAnswer.getText()));
	        slice2 = new PieChart.Data("Organised", Integer.parseInt(lblSecondAnswer.getText()));

	    }
        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
	}
	
}
