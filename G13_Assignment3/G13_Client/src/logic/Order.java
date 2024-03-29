package logic;

import java.io.Serializable;

/**
 * The Order class represents an order made by a visitor in the park management system.
 * It contains various attributes related to the order, such as park name, order number, visitor ID, visitor type,
 * date, time, number of visitors, contact details, exit time, total cost, payment status, and order status.
 */
public class Order implements Serializable {

	/** The name of the park associated with the order. */
    private String parkName;
    
    /** The order number associated with the order. */
    private String orderNum;
    
    /** The ID of the visitor associated with the order. */
    private String visitorId;
    
    /** The type of visitor associated with the order. */
    private String visitorType;
    
    /** The date of the order. */
    private String date;
    
    /** The time of the order. */
    private String time;
    
    /** The number of visitors associated with the order. */
    private String amountOfVisitors;
    
    /** The telephone number of the visitor associated with the order. */
    private String telephone;
    
    /** The email address of the visitor associated with the order. */
    private String email;
    
    /** The exit time of the order. */
    private String exitTime;
    
    /** The total cost of the order. */
    private String totalCost;
    
    /** The payment status of the order. */
    private String payStatus;
    
    /** The status of the order. */
    private String orderStatus;
	

	// Constructors

    /**
     * Constructs an Order object for editing an existing order.
     *
     * @param parkName         the park name
     * @param orderNum         the order number
     * @param date             the date of the order
     * @param time             the time of the order
     * @param amountOfVisitors the number of visitors
     * @param telephone        the visitor's telephone number
     * @param email            the visitor's email address
     */
	public Order(String parkName, String orderNum, String date, String time, String amountOfVisitors, String telephone, String email) {
	    this.parkName = parkName;
	    this.orderNum = orderNum;
	    this.date = date;
	    this.time = time;
	    this.amountOfVisitors = amountOfVisitors;
	    this.telephone=telephone;
	    this.email=email;
	 }
	
	
	/**
     * Constructs a new Order object for creating a new order.
     *
     * @param parkName         the park name
     * @param visitorId        the visitor ID
     * @param visitorType      the visitor type
     * @param date             the date of the order
     * @param time             the time of the order
     * @param amountOfVisitors the number of visitors
     * @param telephone        the visitor's telephone number
     * @param email            the visitor's email address
     */
	public Order(String parkName,String visitorId,String visitorType, String date, String time, String amountOfVisitors, String telephone, String email) {
	    this.parkName = parkName;
	    this.visitorId=visitorId;
		this.visitorType=visitorType;
	    this.date = date;
	    this.time = time;
	    this.amountOfVisitors = amountOfVisitors;
	    this.telephone=telephone;
	    this.email=email;
	}

	/**
     * Constructs an Order object for retrieving order information.
     *
     * @param orderNum the order number
     */
	public Order(String orderNum)
	{
		this.parkName="";
		this.orderNum=orderNum;
		this.visitorId="";
		this.visitorType="";
		this.date="";
		this.time="";
		this.amountOfVisitors="";
		this.telephone="";
		this.email="";
	}

	/**
     * Constructs an Order object with all attributes except exit time, total cost, payment status, and order status.
     *
     * @param parkName         the park name
     * @param orderNum         the order number
     * @param visitorId        the visitor ID
     * @param visitorType      the visitor type
     * @param date             the date of the order
     * @param time             the time of the order
     * @param amountOfVisitors the number of visitors
     * @param telephone        the visitor's telephone number
     * @param email            the visitor's email address
     */
	public Order(String parkName, String orderNum, String visitorId, String visitorType,
                 String date, String time, String amountOfVisitors, String telephone, String email) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.visitorType = visitorType;
        this.date = date;
        this.time = time;
        this.amountOfVisitors = amountOfVisitors;
        this.telephone = telephone;
        this.email = email;
    }
	
	
	/**
     * Constructs an Order object with all attributes including exit time, total cost, payment status, and order status.
     *
     * @param parkName    the park name
     * @param orderNum    the order number
     * @param visitorId   the visitor ID
     * @param visitorType the visitor type
     * @param date        the date of the order
     * @param time        the time of the order
     * @param amountOfVisitors the number of visitors
     * @param telephone   the visitor's telephone number
     * @param email       the visitor's email address
     * @param exitTime    the exit time 
     * */
    public Order(String parkName, String orderNum, String visitorId, String visitorType,
                 String date, String time, String amountOfVisitors, String telephone, String email,String exitTime) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.visitorType = visitorType;
        this.date = date;
        this.time = time;
        this.amountOfVisitors = amountOfVisitors;
        this.telephone = telephone;
        this.email = email;
        this.exitTime=exitTime;
    }
    

	/**
     * Constructs an Order object with all attributes including exit time, total cost, payment status, and order status.
     *
     * @param parkName    the park name
     * @param orderNum    the order number
     * @param visitorId   the visitor ID
     * @param visitorType the visitor type
     * @param date        the date of the order
     * @param time        the time of the order
     * @param amountOfVisitors the number of visitors
     * @param telephone   the visitor's telephone number
     * @param email       the visitor's email address
     * @param exitTime    the exit time
     * @param totalCost   the total cost of the order
     * @param payStatus   the payment status of the order
     * @param orderStatus the order status
     */
    public Order(String parkName, String orderNum, String visitorId, String visitorType, String date, String time,
            String amountOfVisitors, String telephone, String email, String exitTime, String totalCost,
            String payStatus, String orderStatus) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.visitorType = visitorType;
        this.date = date;
        this.time = time;
        this.amountOfVisitors = amountOfVisitors;
        this.telephone = telephone;
        this.email = email;
        this.exitTime = exitTime;
        this.totalCost = totalCost;
        this.payStatus = payStatus;
        this.orderStatus = orderStatus;
    }
    /**
     * Constructs an empty Order object with default values.
     */
    public Order() {
    	this.parkName="";
		this.orderNum="";
		this.visitorId="";
		this.visitorType="";
		this.date="";
		this.time="";
		this.amountOfVisitors="";
		this.telephone="";
		this.email="";
		this.exitTime="";
	}

    /**
     * Returns the park name of the order.
     *
     * @return the park name
     */
    public String getParkName() {
        return parkName;
    }

    /**
     * Returns the order number of the order.
     *
     * @return the order number
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * Returns the visitor ID associated with the order.
     *
     * @return the visitor ID
     */
    public String getVisitorId() {
        return visitorId;
    }

    /**
     * Returns the visitor type associated with the order.
     *
     * @return the visitor type
     */
    public String getVisitorType() {
        return visitorType;
    }

    /**
     * Returns the date of the order.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the time of the order.
     *
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * Returns the number of visitors associated with the order.
     *
     * @return the number of visitors
     */
    public String getAmountOfVisitors() {
        return amountOfVisitors;
    }

    /**
     * Returns the telephone number associated with the order.
     *
     * @return the telephone number
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Returns the email address associated with the order.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Returns the exit time associated with the order.
     *
     * @return the exit time
     */
    public String getExitTime() {
        return exitTime;
    }
    /**
     * Returns the order status of the order.
     *
     * @return the order status
     */
    public String getOrderStatus() {
        return orderStatus;
    }
/**
     * Returns the payment status of the order.
     *
     * @return the payment status
     */
    public String getPayStatus() {
        return payStatus;
    }
    /**
     * Returns the total cost of the order.
     *
     * @return the total cost
     */
    public String getTotalCost() {
        return totalCost;
    }

     /**
     * Sets the park name for the order.
     *
     * @param parkName the park name to set
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }
/**
     * Sets the order number for the order.
     *
     * @param orderNum the order number to set
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
/**
     * Sets the visitor ID for the order.
     *
     * @param visitorId the visitor ID to set
     */
    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }
 /**
     * Sets the visitor type for the order.
     *
     * @param visitorType the visitor type to set
     */
    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType;
    }
/**
     * Sets the date for the order.
     *
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }
/**
     * Sets the time for the order.
     *
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }
/**
     * Sets the number of visitors for the order.
     *
     * @param amountOfVisitors the number of visitors to set
     */
    public void setAmountOfVisitors(String amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
    }
/**
     * Sets the telephone number for the order.
     *
     * @param telephone the telephone number to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
/**
     * Sets the email address for the order.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
/**
     * Sets the exit time for the order.
     *
     * @param exitTime the exit time to set
     */
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
/**
     * Sets the order status for the order.
     *
     * @param orderStatus the order status to set
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
/**
     * Sets the payment status for the order.
     *
     * @param payStatus the payment status to set
     */

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
 /**
     * Sets the total cost for the order.
     *
     * @param totalCost the total cost to set
     */
    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }
}