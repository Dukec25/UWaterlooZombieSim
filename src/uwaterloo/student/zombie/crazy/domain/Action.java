package uwaterloo.student.zombie.crazy.domain;

public class Action {
	public enum ActionType {
	    MOVING, EXPLORING, IDLE, ENCOUNTERED, FIGHTING, RUNNING 
	}
	
	final ActionType type;
	final long totalDurationInSecs; // how much time is required to complete the action from start to finish
	long remainingDurationInSecs; // how much more time is needed to complete the action
	
	// extra data (usually that go with specific action types) that doesn't necessarily need to be populated
	Structure dest = null;
	Group encountered = null;
	
	Action(ActionType type, long totalDurationInSecs){
		this.type = type;
		this.totalDurationInSecs = totalDurationInSecs;
	}
	
	public ActionType getType() {
		return type;
	}

	public Structure getDest() {
		return dest;
	}

	public void setDest(Structure dest) {
		this.dest = dest;
	}

	public Group getEncountered() {
		return encountered;
	}

	public void setEncountered(Group encountered) {
		this.encountered = encountered;
	}

	public long getRemainingDurationInSecs() {
		return remainingDurationInSecs;
	}
	
	// post-condition: remainingDurationInSecs >= 0
	public void reduceRemainingDurationInSecs(long amountInSecs) {
		remainingDurationInSecs -= amountInSecs;
		if (0 > remainingDurationInSecs) {
			remainingDurationInSecs = 0;
		}
	}
}
