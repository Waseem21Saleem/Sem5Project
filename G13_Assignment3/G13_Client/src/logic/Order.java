package logic;

public class Order {

	private String parkName;
	private String orderNum;
	private String visitorId;
	private String visitorType;
	private String date;
	private String time;
	private String visitors;
	private String telephone;
	private String email;
	
	
	//constructor to edit order
	public Order(String parkName, String orderNum, String date, String time, String visitors, String telephone, String email) {
	    this.parkName = parkName;
	    this.orderNum = orderNum;
	    this.date = date;
	    this.time = time;
	    this.visitors = visitors;
	    this.telephone=telephone;
	    this.email=email;
	 }
	//constructor to make new order
	public Order(String parkName, String date, String time, String visitors, String telephone, String email) {
	    this.parkName = parkName;
	    this.date = date;
	    this.time = time;
	    this.visitors = visitors;
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
		this.visitors="";
		this.telephone="";
		this.email="";
	}
	// Constructor
    public Order(String parkName, String orderNum, String visitorId, String visitorType,
                 String date, String time, String visitors, String telephone, String email) {
        this.parkName = parkName;
        this.orderNum = orderNum;
        this.visitorId = visitorId;
        this.visitorType = visitorType;
        this.date = date;
        this.time = time;
        this.visitors = visitors;
        this.telephone = telephone;
        this.email = email;
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

    public String getVisitors() {
        return visitors;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
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

    public void setVisitors(String visitors) {
        this.visitors = visitors;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
