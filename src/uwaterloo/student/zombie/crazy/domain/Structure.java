package uwaterloo.student.zombie.crazy.domain;

import java.util.List;

public abstract class Structure {
	int size; 
	String name; 
	List<Group> groups;
	List<Structure> neighbours; 
	
	public void addGroup(Group group){
		groups.add(group);
	}
	
	public void removeGroup(Group group){
		if(groups.contains(group)){
			groups.remove(group);
		}
		else{
			throw new RuntimeException("remove null existing group");
		}
	}
}
