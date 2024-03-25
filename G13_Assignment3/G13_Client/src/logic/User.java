package logic;

import java.io.Serializable;

public class User implements Serializable{


	private String id;
	private String password;
	private String fullName;
	private String email;
	private String phoneNumber;
	private boolean isLogged;
	private String userPermission;
	private String parkName;
	
	
	public User(String id)
	{
		this.id=id;
		this.password="";
		this.fullName="";
		this.email="";
		this.phoneNumber="";
		this.isLogged=false;
		this.userPermission="";
		
	}
	
	public User(String id, String password)
	{
		this.id=id;
		this.password=password;
		this.fullName="";
		this.email="";
		this.phoneNumber="";
		this.isLogged=false;
		this.userPermission="";
		
	}
	public User(String id,String password,String fullName,String email,String phoneNumber,Boolean isLogged,String userPermission)
	{
		this.id=id;
		this.password=password;
		this.fullName=fullName;
		this.email=email;
		this.phoneNumber=phoneNumber;
		this.isLogged=isLogged;
		this.userPermission=userPermission;
		
	}
	
	public User() {
		this.id="";
		this.password="";
		this.fullName="";
		this.email="";
		this.phoneNumber="";
		this.isLogged=false;
		this.userPermission="";
	}
	// Getter methods
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public String getParkName() {
        return parkName;
    }
    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }


}

