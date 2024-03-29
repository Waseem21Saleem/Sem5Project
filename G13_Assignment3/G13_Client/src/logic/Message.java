package logic;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Message class represents a message used for communication between different components of the park management system.
 * It includes an action type and content associated with that action.
 */
public class Message implements Serializable  {
	

    /**
     * Enumeration representing different types of actions that can be performed.
     */
	public enum ActionType {
	    /**
	     * Represents a user login action (visitor or guide login).
	     */
	    USERLOGIN,

	    /**
	     * Represents a worker login action (worker, park manager, department manager, services).
	     */
	    WORKERLOGIN,

	    /**
	     * Represents an error message from the server (success or failure).
	     */
	    ERROR,

	    /**
	     * Represents the success of a login action.
	     */
	    LOGINSUCCESS,

	    /**
	     * Retrieves the names of parks.
	     */
	    PARKNAMES,

	    /**
	     * Retrieves the order numbers for a specific user.
	     */
	    ORDERSNUMBERS,

	    /**
	     * Retrieves information about an order.
	     */
	    ORDERINFO,

	    /**
	     * Updates information about an order.
	     */
	    UPDATEORDER,

	    /**
	     * Cancels an order.
	     */
	    DELETEORDER,

	    /**
	     * Calculates the available places.
	     */
	    AVAILABLEPLACES,

	    /**
	     * Adds unplanned visitors.
	     */
	    ADDUNPLANNED,

	    /**
	     * Creates an invoice for a specific order number.
	     */
	    GETINVOICE,

	    /**
	     * Approves the exit of an order.
	     */
	    APPROVEEXIT,

	    /**
	     * Retrieves information about a park.
	     */
	    PARKINFO,

	    /**
	     * Creates a new request.
	     */
	    NEWREQUEST,

	    /**
	     * Retrieves requests in a table format.
	     */
	    REQUESTSTABLE,

	    /**
	     * Approves a specific request.
	     */
	    APPROVEREQUEST,

	    /**
	     * Rejects a specific request.
	     */
	    REJECTREQUEST,

	    /**
	     * Creates a cancellation report.
	     */
	    CANCELLATIONREPORT,

	    /**
	     * Creates a usage report.
	     */
	    USAGEREPORT,

	    /**
	     * Creates a total visitors report.
	     */
	    TOTALVISITORSREPORT,

	    /**
	     * Creates a visiting report.
	     */
	    VISITINGREPORT,

	    /**
	     * Retrieves information about a report.
	     */
	    REPORTINFO,

	    /**
	     * Checks a reservation.
	     */
	    RESERVATION,

	    /**
	     * Adds an order to the waiting list.
	     */
	    WAITINGLIST,

	    /**
	     * Retrieves the waiting list in a table format.
	     */
	    WAITINGLISTTABLE,

	    /**
	     * Retrieves 5 alternative dates.
	     */
	    ALTERNATIVEDATE,

	    /**
	     * Changes the role from visitor to guide.
	     */
	    CHANGEROLE,

	    /**
	     * Returns an execution error.
	     */
	    EXECUTEERROR,

	    /**
	     * Logs out a user.
	     */
	    LOGOUT,

	    /**
	     * Represents a client disconnection event.
	     */
	    CLIENTDISCONNECTED
	}

	/** The type of action. */
	private ActionType actionType;
	
	/** The content associated with the action. */
    private Object content;

    /**
     * Constructs a new Message object with the specified action type and content.
     *
     * @param actionType the type of action associated with the message
     * @param content    the content of the message
     */
    public Message(ActionType actionType, Object content) {
        this.actionType = actionType;
        this.content = content;
    }

    /**
     * Returns the action type of the message.
     *
     * @return the action type
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Sets the action type of the message.
     *
     * @param actionType the action type to set
     */
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message
     */
    public Object getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

}