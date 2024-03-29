package logic;

import java.io.Serializable;
/**
 * The Notification class represents a notification related to an order in the park management system.
 * It contains information such as park name, order number, visitor ID, date, time, and number of visitors.
 */
public class Notification implements Serializable {

	/** The name of the park associated with the notification. */
    private String parkName;
    
    /** The order number associated with the notification. */
    private String orderNum;
    
    /** The ID of the visitor associated with the notification. */
    private String visitorId;
    
    /** The date of the notification. */
    private String date;
    
    /** The time of the notification. */
    private String time;
    
    /** The number of visitors associated with the notification. */
    private String amountOfVisitors;

    /**
     * Constructs a new Notification object with the specified attributes.
     *
     * @param parkName         the park name associated with the notification
     * @param orderNum         the order number associated with the notification
     * @param visitorId        the visitor ID associated with the notification
     * @param date             the date of the notification
     * @param time             the time of the notification
     * @param amountOfVisitors the number of visitors associated with the notification
     */
    public Notification(String parkName, String orderNum, String visitorId, String date, String time, String amountOfVisitors) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.date = date;
        this.time = time;
        this.amountOfVisitors = amountOfVisitors;
    }

    /**
     * Constructs a new empty Notification object.
     */
    public Notification() {
    }

    // Getter and Setter methods for parkName

    /**
     * Returns the park name associated with the notification.
     *
     * @return the park name
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Sets the park name associated with the notification.
     *
     * @param parkName the park name to set
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    // Getter and Setter methods for orderNum

    /**
     * Returns the order number associated with the notification.
     *
     * @return the order number
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * Sets the order number associated with the notification.
     *
     * @param orderNum the order number to set
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    // Getter and Setter methods for visitorId

    /**
     * Returns the visitor ID associated with the notification.
     *
     * @return the visitor ID
     */
    public String getVisitorId() {
        return visitorId;
    }

    /**
     * Sets the visitor ID associated with the notification.
     *
     * @param visitorId the visitor ID to set
     */
    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    // Getter and Setter methods for date

    /**
     * Returns the date of the notification.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the date of the notification.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    // Getter and Setter methods for time

    /**
     * Returns the time of the notification.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the notification.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    // Getter and Setter methods for amountOfVisitors

    /**
     * Returns the number of visitors associated with the notification.
     *
     * @return the number of visitors
     */
    public String getAmountOfVisitors() {
        return amountOfVisitors;
    }

    /**
     * Sets the number of visitors associated with the notification.
     *
     * @param amountOfVisitors the number of visitors to set
     */
    public void setAmountOfVisitors(String amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
    }

}
