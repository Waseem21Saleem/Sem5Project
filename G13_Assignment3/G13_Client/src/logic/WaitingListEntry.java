package logic;

import java.io.Serializable;

/**
 * The WaitingListEntry class represents an entry in the waiting list.
 * 
 * This class provides attributes and methods to manage information about a waiting list entry,
 * including placement, time, exit time, date, and number of visitors.
 * 
 * This class implements Serializable to allow objects of this class to be serialized.
 */
public class WaitingListEntry implements Serializable {
    /** The placement of the entry. */
    private String placement;
    
    /** The time of entry. */
    private String time;
    
    /** The exit time. */
    private String exitTime;
    
    /** The date of entry. */
    private String date;
    
    /** The number of visitors. */
    private String numberOfVisitors;

    /**
     * Constructs a WaitingListEntry object with the specified details.
     * 
     * @param placement         The placement of the entry.
     * @param time              The time of entry.
     * @param exitTime          The exit time.
     * @param date              The date of entry.
     * @param numberOfVisitors  The number of visitors.
     */
    public WaitingListEntry(String placement, String time, String exitTime, String date, String numberOfVisitors) {
        this.placement = placement;
        this.time = time;
        this.exitTime = exitTime;
        this.date = date;
        this.numberOfVisitors = numberOfVisitors;
    }
 
    /**
     * Retrieves the placement of the waiting list entry.
     * 
     * @return The placement of the waiting list entry.
     */
    public String getPlacement() {
        return placement;
    }

    /**
     * Sets the placement of the waiting list entry.
     * 
     * @param placement The placement to be set.
     */
    public void setPlacement(String placement) {
        this.placement = placement;
    }

    /**
     * Retrieves the time of the waiting list entry.
     * 
     * @return The time of the waiting list entry.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the waiting list entry.
     * 
     * @param time The time to be set.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Retrieves the exit time of the waiting list entry.
     * 
     * @return The exit time of the waiting list entry.
     */
    public String getExitTime() {
        return exitTime;
    }

    /**
     * Sets the exit time of the waiting list entry.
     * 
     * @param exitTime The exit time to be set.
     */
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    /**
     * Retrieves the date of the waiting list entry.
     * 
     * @return The date of the waiting list entry.
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the waiting list entry.
     * 
     * @param date The date to be set.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Retrieves the number of visitors of the waiting list entry.
     * 
     * @return The number of visitors of the waiting list entry.
     */
    public String getNumberOfVisitors() {
        return numberOfVisitors;
    }

    /**
     * Sets the number of visitors of the waiting list entry.
     * 
     * @param numberOfVisitors The number of visitors to be set.
     */
    public void setNumberOfVisitors(String numberOfVisitors) {
        this.numberOfVisitors = numberOfVisitors;
    }
}
