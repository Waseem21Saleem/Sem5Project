package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements Serializable{

	private String reportType;
	private String parkName;
	private String month;
	private String year;
	private ArrayList<String> details;	
	// Constructor without parameters
    public Report() {
    }

    // Constructor with parameters
    public Report(String reportType,String parkName, String month, String year) {
    	this.reportType=reportType;
    	this.parkName=parkName;
        this.month = month;
        this.year = year;
    }
    public Report(String reportType,String parkName, String month, String year,ArrayList<String> details) {
    	this.reportType=reportType;
    	this.parkName=parkName;
        this.month = month;
        this.year = year;
        this.details=details;
    }

    // Getter for parkName
    public String getReportType() {
        return reportType;
    }
    
    // Setter for ParkName
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
    // Getter for parkName
    public String getParkName() {
        return parkName;
    }
    
    // Setter for ParkName
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }


    // Getter for month
    public String getMonth() {
        return month;
    }

    
    // Setter for month
    public void setMonth(String month) {
        this.month = month;
    }

    // Getter for year
    public String getYear() {
        return year;
    }

    // Setter for year
    public void setYear(String year) {
        this.year = year;
    }
 // Getter for parkName
    public ArrayList<String> getDetails() {
        return details;
    }
    
    // Setter for ParkName
    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }
}