package logic;

import java.io.Serializable;

public class Requests implements Serializable{

	private String parkName;
	private String capacity ;
	private String maxStay;


	// Constructor
    public Requests( String parkName, String capacity, String maxStay) {

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


    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public void setMaxStay (String maxStay) {
    	this.maxStay=maxStay;
    }
    




	
}