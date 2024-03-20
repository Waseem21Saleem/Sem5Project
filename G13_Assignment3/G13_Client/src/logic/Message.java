package logic;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable  {
	
	public enum ActionType {
	    USERLOGIN,
	    WORKERLOGIN,
	    ERROR,
	    LOGINSUCCESS,
	    PARKNAMES,
	    ORDERSNUMBERS,
	    ORDERINFO,
	    UPDATEORDER,
	    RESERVATION,
	    WAITINGLIST,
	    EXECUTEERROR,
	    LOGOUT
	    
	}
	
	private ActionType actionType;
    private Object content;

    // Constructor
    public Message(ActionType actionType, Object content) {
        this.actionType = actionType;
        this.content = content;
    }

    // Getter for actionType
    public ActionType getActionType() {
        return actionType;
    }

    // Setter for actionType
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    // Getter for content
    public Object getContent() {
        return content;
    }

    // Setter for content
    public void setContent(Object content) {
        this.content = content;
    }

}
