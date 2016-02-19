package uwaterloo.student.zombie.crazy.domain;

/**
 * represents objects that are capable of action and decision making (changing actions)
 * and also has a location state
 * this is useful when you want to determine when and how to change the state of an object
 * @author Difei & Duke
 *
 */
public interface Sentient {
	/**
	 * update the sentient's status to represent the fact that it has made a new decision.
	 * So the sentient's action time may (and is normally expected to) change 
	 */
	void makeDecision();
	
	Action getAction();
	
	void setAction(Action action);
	
	Structure getLocation();
	
	void setLocation(Structure location);
	
	/**
	 * get the probability (in percentage per min) that this Sentient encounters the other;
	 * Property; a.getEncounterProbabiltyWith(b) = b.getEncounterProbabiltyWith(a)
	 * @param other the Sentient to encounter
	 * @return the probability (in percentage per min) that this Sentient encounters the other
	 */
	float getEncounterProbabiltyWith(Sentient other);
}
