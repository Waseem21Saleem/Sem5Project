package logic;

import java.io.Serializable;

public class WaitingListEntry implements Serializable {
    private String placement;
    private String time;
    private String exitTime;
    private String date;
    private String numberOfVisitors;

    public WaitingListEntry(String placement, String time, String exitTime, String date, String numberOfVisitors) {
        this.placement = placement;
        this.time = time;
        this.exitTime = exitTime;
        this.date = date;
        this.numberOfVisitors = numberOfVisitors;
    }

    
    
    // Getters and Setters
    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumberOfVisitors() {
        return numberOfVisitors;
    }

    public void setNumberOfVisitors(String numberOfVisitors) {
        this.numberOfVisitors = numberOfVisitors;
    }
}

