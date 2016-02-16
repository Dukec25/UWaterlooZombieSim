package uwaterloo.student.zombie.crazy.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

public class Game {
	Map<String, Structure> structureMap; // map from the structure's name to the structure itself
	PriorityQueue<Sentient> sentientList = new PriorityQueue<Sentient>( 2 , new Comparator<Sentient>(){

		@Override
		public int compare(Sentient o1, Sentient o2) {
			if(o1.getActionTime() < o2.getActionTime()){
				return -1; 
			}
			else if(o1.getActionTime() > o2.getActionTime()){
				return 1;
			}
			return 0;
		}

	});
	
	public static void main(String[] args) throws IOException{ 
		System.out.println("HELLO WORLD");
		
		Game game = new Game();
		game.constructMap();
		game.populateMap(5,5);
	}
	
	public void constructMap() throws IOException{
		String mapFileName = ""; // TODO: figure out where to put this file
		
		FileReader input = new FileReader(mapFileName);
		BufferedReader bufRead = new BufferedReader(input);
		String line = null;

		// construct the nodes
		line = bufRead.readLine();
		int numOfStructures = Integer.parseInt(line);
		for (int i = 0; i < numOfStructures; ++i) {
			line = bufRead.readLine();
			
			String[] lineItems = line.split(" ");
			if (3 != lineItems.length) {
				throw new RuntimeException("Incorrect format for structure line! Expected 3 items!");
			}
			
			Structure newStructure = null;
			if (lineItems[2] == "B") {
				newStructure = new Building(Integer.parseInt(lineItems[1]), lineItems[0]);
			} else if (lineItems[2] == "L") {
				newStructure = new Link(Integer.parseInt(lineItems[1]), lineItems[0]);
			} else {
				throw new RuntimeException("Incorrect format for structure line! "
						+ "Expected either 'B' or 'L' at the end of line!");
			}
			
			structureMap.put(newStructure.name, newStructure);
		}
		
		// link the nodes
		
		line = bufRead.readLine();
		int numOfConnections = Integer.parseInt(line);		
		for (int i = 0; i < numOfConnections; ++i) {
			String[] lineItems = line.split(" ");
			
			Structure structureA, structureB;
			structureA = structureMap.get(lineItems[0]);
			structureB = structureMap.get(lineItems[1]);
			
			structureA.connectStructure(structureB);
			structureB.connectStructure(structureA);
		}
	}
	
	public void populateMap(int zombieNum, int humanNum) {
		// TODO: implement!
		// create initial creature and possibly group 
	
		for(int n = 0; n < zombieNum; n++){
			Group temp = new Group();
			temp.addMember(new Zombie());
			sentientList.add(temp);
		}
		for(int n = 0; n < humanNum; n++){
			Group temp = new Group();
			temp.addMember(new Human());
			sentientList.add(temp);
		}
		
	}
}
