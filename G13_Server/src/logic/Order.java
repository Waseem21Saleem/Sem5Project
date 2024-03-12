package logic;

public class Order {

	private String parkName;
	private String orderNum;
	private String time;
	private String visitors;
	private String telephone;
	private String email;
	
	public Order()
	{
		this.parkName="";
		this.orderNum="";
		this.time="";
		this.visitors="";
		this.telephone="";
		this.email="";
	}
	public Order(String parkName,String orderNum,String time,String visitors,String telephone,String email) {
		this.parkName=parkName;
		this.orderNum=orderNum;
		this.time=time;
		this.visitors=visitors;
		this.telephone=telephone;
		this.email=email;
	}
	
	/**
	 * @return the parkName
	 */
	public String getParkName() {
		return parkName;
	}
	/**
	 * @param name the parkName to set
	 */
	public void setParkName(String name) {
		parkName = name;
	}
	/**
	 * @return the order number
	 */
	public String getOrderNum() {
		return orderNum;
	}
	/**
	 * @param name the order number to set
	 */
	public void setOrderNum(String name) {
		orderNum = name;
	}
	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}
	/**
	 * @param name the time to set
	 */
	public void setTime(String name) {
		time = name;
	}
	/**
	 * @return the visitors
	 */
	public String getVisitors() {
		return visitors;
	}
	/**
	 * @param name the Visitors to set
	 */
	public void setVisitors(String name) {
		visitors = name;
	}
	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}
	/**
	 * @param name the parkName to set
	 */
	public void setTelephone(String name) {
		telephone = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param name the parkName to set
	 */
	public void setEmail(String name) {
		email = name;
	}
	
	public String toString(){
		return String.format("%s %s %s %s %s %s\n",parkName,orderNum,time,visitors,telephone,email);
	}

}
