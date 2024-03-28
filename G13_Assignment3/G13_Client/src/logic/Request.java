package logic;

import java.io.Serializable;

public class Request implements Serializable{

	private String parkName;
	private String reservedCapacity;
	private String totalCapacity;
	private String maxStay;
	private String status;

 
	// Constructor
    public Request( String parkName, String reservedCapacity,String totalCapacity, String maxStay) {

        this.parkName = parkName;
        this.reservedCapacity = reservedCapacity;
        this.totalCapacity=totalCapacity;
        this.maxStay = maxStay;
    }
	
    


    public String getParkName() {
        return parkName;
    }

    public String getReservedCapacity() {
        return reservedCapacity;
    }

    public String getTotalCapacity() {
        return totalCapacity;
    }

    public String getMaxStay() {
        return maxStay;
    }
    
    
    public String getStatus() {
        return status;
    }


    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setReservedCapacity(String reservedCapacity) {
        this.reservedCapacity = reservedCapacity;
    }
    
    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setMaxStay (String maxStay) {
    	this.maxStay=maxStay;
    }
    
    public void setStatus (String status) {
    	this.status=status;
    }




	
}