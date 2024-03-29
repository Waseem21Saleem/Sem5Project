package logic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a report with its attributes such as report type, park name, month, year, and details.
 * This class implements Serializable to support serialization and deserialization.
 */
public class Report implements Serializable {

    /** The type of report. */
    private String reportType;
    
    /** The name of the park associated with the report. */
    private String parkName;
    
    /** The month for which the report is generated. */
    private String month;
    
    /** The year for which the report is generated. */
    private String year;
    
    /** The details of the report. */
    private ArrayList<String> details;

    /**
     * Default constructor for the Report class.
     */
    public Report() {
        // Default constructor
    }

    /**
     * Constructs a Report object with specified report type, park name, month, and year.
     *
     * @param reportType The type of the report.
     * @param parkName   The name of the park associated with the report.
     * @param month      The month of the report.
     * @param year       The year of the report.
     */
    public Report(String reportType, String parkName, String month, String year) {
        this.reportType = reportType;
        this.parkName = parkName;
        this.month = month;
        this.year = year;
    }

    /**
     * Constructs a Report object with specified report type, park name, month, year, and details.
     *
     * @param reportType The type of the report.
     * @param parkName   The name of the park associated with the report.
     * @param month      The month of the report.
     * @param year       The year of the report.
     * @param details    The details of the report.
     */
    public Report(String reportType, String parkName, String month, String year, ArrayList<String> details) {
        this.reportType = reportType;
        this.parkName = parkName;
        this.month = month;
        this.year = year;
        this.details = details;
    }

    /**
     * Retrieves the report type.
     *
     * @return The report type.
     */
    public String getReportType() {
        return reportType;
    }

    /**
     * Sets the report type.
     *
     * @param reportType The report type to set.
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /**
     * Retrieves the park name associated with the report.
     *
     * @return The park name.
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Sets the park name associated with the report.
     *
     * @param parkName The park name to set.
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    /**
     * Retrieves the month of the report.
     *
     * @return The month of the report.
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month of the report.
     *
     * @param month The month to set.
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Retrieves the year of the report.
     *
     * @return The year of the report.
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year of the report.
     *
     * @param year The year to set.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Retrieves the details of the report.
     *
     * @return The details of the report.
     */
    public ArrayList<String> getDetails() {
        return details;
    }

    /**
     * Sets the details of the report.
     *
     * @param details The details to set.
     */
    public void setDetails(ArrayList<String> details) {
        this.details = details;
    }
}