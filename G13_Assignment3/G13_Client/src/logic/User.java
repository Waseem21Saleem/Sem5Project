package logic;

import java.io.Serializable;

/**
 * The User class represents a user in the system.
 * 
 * This class provides attributes and methods to manage user information such as ID, password, full name,
 * email, phone number, login status, user permission, and associated park name.
 * 
 * This class implements Serializable to allow objects of this class to be serialized.
 */
public class User implements Serializable {

	 /** The user ID. */
    private String id;
    
    /** The user password. */
    private String password;
    
    /** The user's full name. */
    private String fullName;
    
    /** The user's email address. */
    private String email;
    
    /** The user's phone number. */
    private String phoneNumber;
    
    /** The user's login status. */
    private boolean isLogged;
    
    /** The user's permission level. */
    private String userPermission;
    
    /** The name of the park associated with the user. */
    private String parkName;

    /**
     * Constructs a new User object with the specified ID.
     * 
     * @param id The user ID.
     */
    public User(String id) {
        this.id = id;
        this.password = "";
        this.fullName = "";
        this.email = "";
        this.phoneNumber = "";
        this.isLogged = false;
        this.userPermission = "";
    }

    /**
     * Constructs a new User object with the specified ID and password.
     * 
     * @param id       The user ID.
     * @param password The user password.
     */
    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.fullName = "";
        this.email = "";
        this.phoneNumber = "";
        this.isLogged = false;
        this.userPermission = "";
    }

    /**
     * Constructs a new User object with the specified details.
     * 
     * @param id             The user ID.
     * @param password       The user password.
     * @param fullName       The user's full name.
     * @param email          The user's email address.
     * @param phoneNumber    The user's phone number.
     * @param isLogged       The user's login status.
     * @param userPermission The user's permission level.
     */
    public User(String id, String password, String fullName, String email, String phoneNumber, boolean isLogged,
            String userPermission) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isLogged = isLogged;
        this.userPermission = userPermission;
    }

    /**
     * Constructs a new User object with default values.
     */
    public User() {
        this.id = "";
        this.password = "";
        this.fullName = "";
        this.email = "";
        this.phoneNumber = "";
        this.isLogged = false;
        this.userPermission = "";
    }

    // Getter methods

    /**
     * Retrieves the user ID.
     * 
     * @return The user ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the user password.
     * 
     * @return The user password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's full name.
     * 
     * @return The user's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Retrieves the user's email address.
     * 
     * @return The user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retrieves the user's phone number.
     * 
     * @return The user's phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Retrieves the user's login status.
     * 
     * @return True if the user is logged in, false otherwise.
     */
    public boolean isLogged() {
        return isLogged;
    }

    /**
     * Retrieves the user's permission level.
     * 
     * @return The user's permission level.
     */
    public String getUserPermission() {
        return userPermission;
    }

    /**
     * Retrieves the name of the park associated with the user.
     * 
     * @return The name of the park associated with the user.
     */
    public String getParkName() {
        return parkName;
    }

    // Setter methods

    /**
     * Sets the user ID.
     * 
     * @param id The user ID to be set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the user password.
     * 
     * @param password The user password to be set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's full name.
     * 
     * @param fullName The user's full name to be set.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The user's email address to be set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the user's phone number.
     * 
     * @param phoneNumber The user's phone number to be set.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the user's login status.
     * 
     * @param isLogged The login status of the user.
     */
    public void setLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }

    /**
     * Sets the user's permission level.
     * 
     * @param userPermission The user's permission level to be set.
     */
    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    /**
     * Sets the name of the park associated with the user.
     * 
     * @param parkName The name of the park to be associated with the user.
     */
    public void setParkName(String parkName) {
        this.parkName = parkName;
    }
}