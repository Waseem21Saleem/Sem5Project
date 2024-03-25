package logic;

import java.io.Serializable;

public class Notification implements Serializable{
	
	private String parkName;
	private String orderNum;
	private String visitorId;
	private String date;
	private String time;
	private String amountOfVisitors;
	
	// Constructor
    public Notification(String parkName, String orderNum, String visitorId, String date, String time, String amountOfVisitors) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.date = date;
        this.time = time;
        this.amountOfVisitors = amountOfVisitors;
   
    }

 // Constructor
    public Notification() {
    	
    }
    // Getter and Setter methods for parkName
    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    // Getter and Setter methods for orderNum
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    // Getter and Setter methods for visitorId
    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    // Getter and Setter methods for date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter and Setter methods for time
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // Getter and Setter methods for amountOfVisitors
    public String getAmountOfVisitors() {
        return amountOfVisitors;
    }

    public void setAmountOfVisitors(String amountOfVisitors) {
        this.amountOfVisitors = amountOfVisitors;
    }

   

}
