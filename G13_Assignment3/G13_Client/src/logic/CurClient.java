package logic;

/**
 * This class represents a client currently connected to the system.
 * It stores information such as IP address, host, and connection status.
 * <p>Author: Mohammed Khateeb</p>
 */
public class CurClient {

    /** The IP address of the client. */
    private String ip;

    /** The host of the client. */
    private String host;

    /** The connection status of the client. */
    private String connected;

    /** Constructs a new CurClient with default values. */
    public CurClient() {
        this.ip = "";
        this.host = "";
        this.connected = "";
    }

    /**
     * Constructs a new CurClient with the specified IP, host, and connection status.
     *
     * @param ip        The IP address of the client.
     * @param host      The host of the client.
     * @param connected The connection status of the client.
     */
    public CurClient(String ip, String host, String connected) {
        this.ip = ip;
        this.host = host;
        this.connected = connected;
    }

    /**
     * Retrieves the IP address of the client.
     *
     * @return The IP address of the client.
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the IP address of the client.
     *
     * @param ip The IP address to set.
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Retrieves the host of the client.
     *
     * @return The host of the client.
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets the host of the client.
     *
     * @param host The host to set.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Retrieves the connection status of the client.
     *
     * @return The connection status of the client.
     */
    public String getConnection() {
        return connected;
    }

    /**
     * Sets the connection status of the client.
     *
     * @param connected The connection status to set.
     */
    public void setConnection(String connected) {
        this.connected = connected;
    }
}