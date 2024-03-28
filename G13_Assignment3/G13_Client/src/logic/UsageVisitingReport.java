package logic;

import java.io.Serializable;

public class UsageVisitingReport implements Serializable {

	private String timeRange;
	private int fullOrEnterCounter; //if usage report then fullCounter else enterCounter
	private int notFullOrExitCounter; //if usage report then notFullCounter else exitCounter
	
	public UsageVisitingReport() {
		
	}
	public UsageVisitingReport(String timeRange, int fullOrEnterCounter, int notFullOrExitCounter) {
        this.timeRange = timeRange;
        this.fullOrEnterCounter = fullOrEnterCounter;
        this.notFullOrExitCounter = notFullOrExitCounter;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public int getFullOrEnterCounter() {
        return fullOrEnterCounter;
    }

    public void setFullOrEnterCounter(int fullOrEnterCounter) {
        this.fullOrEnterCounter = fullOrEnterCounter;
    }

    public int getNotFullOrExitCounter() {
        return notFullOrExitCounter;
    }

    public void setNotFullOrExitCounter(int notFullOrExitCounter) {
        this.notFullOrExitCounter = notFullOrExitCounter;
    }
}
