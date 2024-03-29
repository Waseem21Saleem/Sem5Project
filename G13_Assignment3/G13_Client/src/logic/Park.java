package logic;

import java.io.Serializable;

/**
 * Represents a park with its attributes such as name, reserved capacity, total capacity, and maximum stay duration.
 * This class implements Serializable to support serialization and deserialization.
 */
public class Park implements Serializable {

	 /** The name of the park. */
    private String parkName;
    
    /** The reserved capacity of the park. */
    private String reservedCapacity;
    
    /** The total capacity of the park. */
    private String totalCapacity;
    
    /** The maximum stay allowed in the park. */
    private String maxStay;

    /**
     * Constructs a Park object with specified attributes.
     *
     * @param parkName         The name of the park.
     * @param reservedCapacity The reserved capacity of the park.
     * @param totalCapacity    The total capacity of the park.
     * @param maxStay          The maximum stay duration allowed in the park.
     */
    public Park(String parkName, String reservedCapacity, String totalCapacity, String maxStay) {
        this.parkName = parkName;
        this.reservedCapacity = reservedCapacity;
        this.totalCapacity = totalCapacity;
        this.maxStay = maxStay;
    }

    /**
     * Constructs a Park object with only the park name specified.
     *
     * @param parkName The name of the park.
     */
    public Park(String parkName) {
        this.parkName = parkName;
    }

    /**
     * Default constructor for the Park class.
     */
    public Park() {
        // Default constructor
    }

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
     * Retrieves the maximum stay duration allowed in the park.
     *
     * @return The maximum stay duration allowed in the park.
     */
    public String getMaxStay() {
        return maxStay;
    }

    /**
     * Sets the name of the park.
     *
     * @param parkName The name of the park to set.
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    /**
     * Sets the reserved capacity of the park.
     *
     * @param reservedCapacity The reserved capacity of the park to set.
     */
    public void setReservedCapacity(String reservedCapacity) {
        this.reservedCapacity = reservedCapacity;
    }

    /**
     * Sets the total capacity of the park.
     *
     * @param totalCapacity The total capacity of the park to set.
     */
    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    /**
     * Sets the maximum stay duration allowed in the park.
     *
     * @param maxStay The maximum stay duration allowed in the park to set.
     */
    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }
}