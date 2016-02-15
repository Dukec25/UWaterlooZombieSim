package uwaterloo.student.zombie.crazy.domain;

public class Action {
	public enum ActionType {
	    MOVING, EXPLORING, IDLE, ENCOUNTERED, FIGHTING, RUNNING 
	}
	
	ActionType type;
	//implement duration later
	
	Structure dest = null;
	
	Group encountered = null;
	
	Action(ActionType type){
		this.type = type;
	}
	
	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
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
	
}
