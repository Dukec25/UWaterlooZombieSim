package uwaterloo.student.zombie.crazy.domain;

/**
 * represents objects that are capable of action and decision making (changing actions)
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
}
