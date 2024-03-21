package logic;

import java.io.Serializable;

public class Request implements Serializable{

	private String parkName;
	private String capacity ;
	private String maxStay;
	private String status;


	// Constructor
    public Request( String parkName, String capacity, String maxStay) {

        this.parkName = parkName;
        this.capacity = capacity;
        this.maxStay = maxStay;
    }
	



    public String getParkName() {
        return parkName;
    }

    public String getCapacity() {
        return capacity;
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

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setMaxStay (String maxStay) {
    	this.maxStay=maxStay;
    }
    
    public void setStatus (String status) {
    	this.status=status;
    }




	
}