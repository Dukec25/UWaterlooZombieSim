package uwaterloo.student.zombie.crazy.domain;

import java.util.List;

public abstract class Structure {
	int size; 
	String name;
	List<Sentient> sentients;
	List<Structure> neighbours; 
	
	public List<Sentient> getSentients() {
		return sentients;
	}

	public void addSentient(Sentient sentient){
		sentients.add(sentient);
	}
	
	public void removeSentient(Sentient sentient){
		if(sentients.contains(sentient)){
			sentients.remove(sentient);
		}
		else{
			throw new RuntimeException("remove null existing group");
		}
	}
	
	public void connectStructure(Structure neighbourToBe) {
		neighbours.add(neighbourToBe);
	}
}
