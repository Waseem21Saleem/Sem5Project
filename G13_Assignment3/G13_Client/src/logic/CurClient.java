package logic;

public class CurClient {

	private String ip;
	private String host;
	private boolean connected;
	public CurClient() {
		this.ip="";
		this.host="";
		this.connected=false;
	}
	public CurClient(String ip,String host, boolean connected) {
		 
		this.ip=ip;
		this.host=host;
		this.connected=connected;
		
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param name the ip to set
	 */
	public void setIp(String name) {
		ip = name;
	}
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param name the host to set
	 */
	public void setHost(String name) {
		host = name;
	}
	/**
	 * @return the connection
	 */
	public boolean getConnection() {
		return connected;
	}
	/**
	 * @param name the connection to set
	 */
	public void setConnection(boolean name) {
		connected = name;
	}

}
