package logic;

import java.io.Serializable;

public class Park implements Serializable{

	private String parkName;
	private String reservedCapacity;
	private String totalCapacity;
	private String maxStay;

 
	
	// Constructor
    public Park( String parkName, String reservedCapacity,String totalCapacity, String maxStay) {

        this.parkName = parkName;
        this.reservedCapacity = reservedCapacity;
        this.totalCapacity=totalCapacity;
        this.maxStay = maxStay;
    }
	// Constructor
    public Park( String parkName) {

        this.parkName = parkName;
       
    }


    public Park() {
		// TODO Auto-generated constructor stub
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


    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setReservedCapacity(String reservedCapacity) {
        this.reservedCapacity = reservedCapacity;
    }
    
    public void setTotalCapacity(String totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }

}
