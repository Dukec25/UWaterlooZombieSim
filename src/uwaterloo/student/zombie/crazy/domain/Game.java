package uwaterloo.student.zombie.crazy.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Game {
	Map<String, Structure> structureMap; // map from the structure's name to the structure itself
	PriorityQueue<Sentient> gameParticipants = new PriorityQueue<Sentient>( 2 , new Comparator<Sentient>(){

		@Override
		public int compare(Sentient o1, Sentient o2) {
			if(o1.getAction().getRemainingDurationInSecs() < o2.getAction().getRemainingDurationInSecs()){
				return -1; 
			}
			else if(o1.getAction().getRemainingDurationInSecs() > o2.getAction().getRemainingDurationInSecs()){
				return 1;
			}
			return 0;
		}

	});
	
	int timeInSecs = 0; // how much time has elapsed since the start of the game
	
	private void run(){
		while(true){
			// every time this loop iterates, we simulate 60 seconds (1 min) of game time
			advanceStateForTime(60);
			makeDecisions();
			generateEvents();
		}
	}
	
	/**
	 * Update the state of all groups/creatures/entities in the game for a specified amount of time.
	 * This includes updating the remaining times on all sentient beings' actions
	 * @param timeInSecs time to advance the state, in seconds
	 */
	private void advanceStateForTime(long timeInSecs) {
		// TODO: implement!
		
		// update remaining times (TODO: maybe do this while updating state?)
		for (Sentient participant : gameParticipants) {
			participant.getAction().reduceRemainingDurationInSecs(timeInSecs);
		}
	}
	
	/**
	 * look at the current state of the world and determine whether certain events should take place,
	 * thus changing the state
	 */
	private void generateEvents() {
		// for each structure, determine whether groups in the structure encounter each other
		for (Structure structure : structureMap.values()) {
			// loop over each pair of Sentients in structure (s_i, s_j), where i < j
			for (int i = 0; i < structure.getSentients().size() - 1; ++i) {
				for (int j = i + 1; j < structure.getSentients().size(); ++j) {
					Sentient sentientI, sentientJ;
					sentientI = structure.getSentients().get(i);
					sentientJ = structure.getSentients().get(j);
					
					if (GameUtil.chancePercentage(sentientI.getEncounterProbabiltyWith(sentientJ))) {
						// update sentientI's state
						if (sentientI.getAction().getType() != Action.ActionType.ENCOUNTERED) {
							sentientI.setAction(new Action(Action.ActionType.ENCOUNTERED, Action.ENCOUNTER_DURATION_IN_SECS));
						}
						sentientI.getAction().getEncounteredSentients().add(sentientJ);
						
						// update sentientJ's state
						if (sentientJ.getAction().getType() != Action.ActionType.ENCOUNTERED) {
							sentientJ.setAction(new Action(Action.ActionType.ENCOUNTERED, Action.ENCOUNTER_DURATION_IN_SECS));
						}
						sentientJ.getAction().getEncounteredSentients().add(sentientI);
					}
				}
			} // end of: loop over each pair of Sentients in structure (s_i, s_j), where i < j
		}
	}
	
	// go through the Sentient list, find the one has time remaining zero and do the decision
	// does not advance time 
	private void makeDecisions() {
		if(gameParticipants.isEmpty()){
			return;
		}
		
		while (0 == gameParticipants.peek().getAction().getRemainingDurationInSecs()) {
			Sentient temp = gameParticipants.poll();
			temp.makeDecision();
			gameParticipants.add(temp);
		}
	}
	
	private void constructMap() throws IOException{
		String mapFileName = ""; // TODO: figure out where to put this file
		
		FileReader input = new FileReader(mapFileName);
		BufferedReader bufRead = new BufferedReader(input);
		try{
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
		} finally {
			bufRead.close();
		}
	}
	
	private void populateMap(int zombieNum, int humanNum) {
		// create initial creature and possibly group 	
		for(int n = 0; n < zombieNum; n++){
			Group temp = new Group();
			temp.addMember(new Zombie());
			gameParticipants.add(temp);
		}
		for(int n = 0; n < humanNum; n++){
			Group temp = new Group();
			temp.addMember(new Human());
			gameParticipants.add(temp);
		}
		
		
	}
	
	public static void main(String[] args) throws IOException{ 
		System.out.println("HELLO WORLD");
		
		Game game = new Game();
		game.constructMap();
		game.populateMap(5,5);
		game.run();
	}
}
