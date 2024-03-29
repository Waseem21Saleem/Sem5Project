package logic;

import java.io.Serializable;

/**
 * The Request class represents a request for park information.
 * It contains details such as park name, reserved capacity, total capacity, maximum stay duration,
 * and status of the request.
 * 
 * This class implements Serializable to allow objects of this class to be serialized.
 */
public class Request implements Serializable {

	   /** The name of the park associated with the request. */
    private String parkName;
    
    /** The reserved capacity of the park. */
    private String reservedCapacity;
    
    /** The total capacity of the park. */
    private String totalCapacity;
    
    /** The maximum stay duration in the park. */
    private String maxStay;
    
    /** The status of the request. */
    private String status;

    /**
     * Constructs a new Request object with the specified details.
     * 
     * @param parkName          The name of the park.
     * @param reservedCapacity  The reserved capacity of the park.
     * @param totalCapacity     The total capacity of the park.
     * @param maxStay           The maximum stay duration in the park.
     */
    public Request(String parkName, String reservedCapacity, String totalCapacity, String maxStay) {
        this.parkName = parkName;
        this.reservedCapacity = reservedCapacity;
        this.totalCapacity = totalCapacity;
        this.maxStay = maxStay;
    }

    // Getters and setters for the attributes of the Request class

    /**
     * Retrieves the name of the park.
     * 
     * @return The name of the park.
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Retrieves the reserved capacity of the park.
     * 
     * @return The reserved capacity of the park.
     */
    public String getReservedCapacity() {
        return reservedCapacity;
    }

    /**
     * Retrieves the total capacity of the park.
     * 
     * @return The total capacity of the park.
     */
    public String getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * Retrieves the maximum stay duration in the park.
     * 
     * @return The maximum stay duration in the park.
     */
    public String getMaxStay() {
        return maxStay;
    }

    /**
     * Retrieves the status of the request.
     * 
     * @return The status of the request.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the name of the park.
     * 
     * @param parkName The name of the park to be set.
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    /**
     * Sets the reserved capacity of the park.
     * 
     * @param reservedCapacity The reserved capacity of the park to be set.
     */
    public void setReservedCapacity(String reservedCapacity) {
        this.reservedCapacity = reservedCapacity;
    }

    /**
     * Sets the total capacity of the park.
     * 
     * @param totalCapacity The total capacity of the park to be set.
     */
    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    /**
     * Sets the maximum stay duration in the park.
     * 
     * @param maxStay The maximum stay duration in the park to be set.
     */
    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }

    /**
     * Sets the status of the request.
     * 
     * @param status The status of the request to be set.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}