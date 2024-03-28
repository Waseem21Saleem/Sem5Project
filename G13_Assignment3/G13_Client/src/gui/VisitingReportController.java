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

public class VisitingReportController implements Initializable {
	@FXML
	private Label lblParkName,lblReportType,labelInputMonth,labelInputYear;

	@FXML
	StackedBarChart <String, Number> stackedBarChart1,stackedBarChart2;
	@FXML
	CategoryAxis xAxis1,xAxis2;
	@FXML
	NumberAxis yAxis1,yAxis2;
	
	
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
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        ArrayList<UsageVisitingReport> list0=ChatClient.visitingArrayList.get(0);
        ArrayList<UsageVisitingReport> list1=ChatClient.visitingArrayList.get(1);
        // Set labels for the axes
        xAxis1.setLabel("Time Range");
        yAxis1.setLabel("Number of Visitors");
        xAxis2.setLabel("Time Range");
        yAxis2.setLabel("Number of Visitors");
        series1.setName("Entered count");
        series2.setName("Exited count");
        series3.setName("Entered count");
        series4.setName("Exited count");
        for (int i=0;i<4;i++) {
    		series1.getData().add(new XYChart.Data<>(list0.get(i).getTimeRange(), list0.get(i).getFullOrEnterCounter()));
    		series2.getData().add(new XYChart.Data<>(list0.get(i).getTimeRange(), list0.get(i).getNotFullOrExitCounter()));
    		series3.getData().add(new XYChart.Data<>(list1.get(i).getTimeRange(), list1.get(i).getFullOrEnterCounter()));
    		series4.getData().add(new XYChart.Data<>(list1.get(i).getTimeRange(), list1.get(i).getNotFullOrExitCounter()));
    	}
        stackedBarChart1.getData().addAll(series1, series2);
        stackedBarChart2.getData().addAll(series3, series4);



	}
	
}
