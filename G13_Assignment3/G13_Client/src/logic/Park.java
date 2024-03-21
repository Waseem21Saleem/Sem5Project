package logic;

import java.io.Serializable;

public class Park implements Serializable{

	private String parkName;
	private String capacity;
	private String maxStay;


	// Constructor
    public Park( String parkName, String capacity, String maxStay) {

        this.parkName = parkName;
        this.capacity = capacity;
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

    public void setMaxStay(String maxStay) {
        this.maxStay = maxStay;
    }

}
