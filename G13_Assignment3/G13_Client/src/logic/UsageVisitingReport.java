package logic;

import java.io.Serializable;

/**
 * The UsageVisitingReport class represents a visiting report for usage statistics.
 * 
 * This class provides attributes and methods to manage visiting report information such as time range, 
 * full/enter counter, and not full/exit counter.
 * 
 * This class implements Serializable to allow objects of this class to be serialized.
 */
public class UsageVisitingReport implements Serializable {

    /** The time range of the report. */
    private String timeRange;
    
    /** The counter for full or enter events. */
    private int fullOrEnterCounter;
    
    /** The counter for not full or exit events. */
    private int notFullOrExitCounter;


    /**
     * Constructs an empty UsageVisitingReport object.
     */
    public UsageVisitingReport() {
        
    }

    /**
     * Constructs a UsageVisitingReport object with the specified details.
     * 
     * @param timeRange          The time range of the report.
     * @param fullOrEnterCounter The counter for full or enter events.
     * @param notFullOrExitCounter The counter for not full or exit events.
     */
    public UsageVisitingReport(String timeRange, int fullOrEnterCounter, int notFullOrExitCounter) {
        this.timeRange = timeRange;
        this.fullOrEnterCounter = fullOrEnterCounter;
        this.notFullOrExitCounter = notFullOrExitCounter;
    }

    /**
     * Retrieves the time range of the report.
     * 
     * @return The time range of the report.
     */
    public String getTimeRange() {
        return timeRange;
    }

    /**
     * Sets the time range of the report.
     * 
     * @param timeRange The time range to be set.
     */
    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    /**
     * Retrieves the counter for full or enter events.
     * 
     * @return The counter for full or enter events.
     */
    public int getFullOrEnterCounter() {
        return fullOrEnterCounter;
    }

    /**
     * Sets the counter for full or enter events.
     * 
     * @param fullOrEnterCounter The counter for full or enter events to be set.
     */
    public void setFullOrEnterCounter(int fullOrEnterCounter) {
        this.fullOrEnterCounter = fullOrEnterCounter;
    }

    /**
     * Retrieves the counter for not full or exit events.
     * 
     * @return The counter for not full or exit events.
     */
    public int getNotFullOrExitCounter() {
        return notFullOrExitCounter;
    }

    /**
     * Sets the counter for not full or exit events.
     * 
     * @param notFullOrExitCounter The counter for not full or exit events to be set.
     */
    public void setNotFullOrExitCounter(int notFullOrExitCounter) {
        this.notFullOrExitCounter = notFullOrExitCounter;
    }
}
