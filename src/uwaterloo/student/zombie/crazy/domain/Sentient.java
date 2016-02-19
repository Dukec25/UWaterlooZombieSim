package uwaterloo.student.zombie.crazy.domain;

public interface Sentient {
	/**
	 * update the sentient's status to represent the fact that it has made a new decision.
	 * So the sentient's action time may (and is normally expected to) change 
	 */
	void makeDecision();
	
	/**
	 * get the remaining time for this object's current action
	 * @return the remaining time for this object's current action
	 */
	long getActionTime();
}
