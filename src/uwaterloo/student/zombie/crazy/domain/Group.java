package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;

public class Group implements Sentient {
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
	
	@Override
	public long getActionTime(){
		return action.getRemainingDurationInSecs();
	}
	
	public void removeMember(Creature creature){
		members.remove(creature);
	}
	
	@Override
	public void makeDecision() {
		// TODO Auto-generated method stub
		
	} 
	
	
}
