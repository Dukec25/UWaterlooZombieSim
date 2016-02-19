package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;

public class Group implements Sentient {
	// constants:
	private static final float ENCOUNTER_PROB = (float) 1.0; // in percentage per minute
	
	Action action;
	ArrayList<Creature> members; 
	Structure location;
	
	public void addMember(Creature creature){
		if( !members.isEmpty()){
			if( (creature instanceof Human && members.get(0) instanceof Zombie) 
					|| (creature instanceof Zombie && members.get(0) instanceof Human) ){
				throw new RuntimeException("incompatible type");
			}
		}
		members.add(creature);
		
	}
	
	public void removeMember(Creature creature){
		members.remove(creature);
	}
	
	@Override
	public void makeDecision() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action getAction() {
		return action;
	}

	@Override
	public float getEncounterProbabiltyWith(Sentient other) {
		return ENCOUNTER_PROB;
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
	} 
	
}
