package gui;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import logic.Report;
import logic.UsageVisitingReport;

public class UsageVisitingReportController implements Initializable {
	@FXML
	private Label lblParkName,lblReportType,labelInputMonth,labelInputYear;

	@FXML
	StackedBarChart <String, Number> stackedBarChart;
	@FXML
	CategoryAxis xAxis;
	@FXML
	NumberAxis yAxis;
	
	
	public void goBack(ActionEvent event) throws Exception {
		
		ChatClient.openGUI.goToGUI(event, "/gui/DepartmentManagerHomePage.fxml","","Department manager home page");
		 
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {    
	    Report report = DepartmentManagerHomePageController.report;
	    lblParkName.setText(report.getParkName());
	    lblReportType.setText(report.getReportType());
	    labelInputMonth.setText(report.getMonth());
	    labelInputYear.setText(report.getYear());
	 // Create series
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        ArrayList<UsageVisitingReport> list = ChatClient.usageVisitingList;
        // Set labels for the axes
        xAxis.setLabel("Time Range");
        yAxis.setLabel("Number of Visitors");
        series1.setName("Fully booked");
        series2.setName("Not-Fully booked");

        // Add series to the chart
      /*  if (report.getReportType().equals("Usage report")) {
        	 // Populate series with data
        	*/for (int i=0;i<8;i++) {
        		series1.getData().add(new XYChart.Data<>(list.get(i).getTimeRange(), list.get(i).getFullOrEnterCounter()));
        		series2.getData().add(new XYChart.Data<>(list.get(i).getTimeRange(), list.get(i).getNotFullOrExitCounter()));
        	}
            
         
	  /*  } else {
	       

	        
	    }*/
        
        stackedBarChart.getData().addAll(series1, series2);


	}
	
}
