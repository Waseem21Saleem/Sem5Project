package logic;

import java.io.Serializable;

public class Order implements Serializable {

	private String parkName;
	private String orderNum;
	private String visitorId;
	private String visitorType;
	private String date;
	private String time;
	private String amountOfVisitors;
	private String telephone;
	private String email;
	private String exitTime;
	private String totalCost;
	private String payStatus;
	private String orderStatus;
	

	
	//constructor to edit order
	public Order(String parkName, String orderNum, String date, String time, String amountOfVisitors, String telephone, String email) {
	    this.parkName = parkName;
	    this.orderNum = orderNum;
	    this.date = date;
	    this.time = time;
	    this.amountOfVisitors = amountOfVisitors;
	    this.telephone=telephone;
	    this.email=email;
	 }
	//constructor to make new order
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

	//constructor to get order info
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
	// Constructor
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
	// Getter methods
    public String getParkName() {
        return parkName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public String getVisitorType() {
        return visitorType;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAmountOfVisitors() {
        return amountOfVisitors;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }
    
    public String getExitTime() {
        return exitTime;
    }
    public String getOrderStatus() {
        return orderStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }
    
    public String getTotalCost() {
        return totalCost;
    }

    
    // Setter methods
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public void setVisitorType(String visitorType) {
        this.visitorType = visitorType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAmountOfVisitors(String amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    

}
