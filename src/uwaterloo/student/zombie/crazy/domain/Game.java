package uwaterloo.student.zombie.crazy.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Game {

    /////////////////////////////////////////////////////////
    // Game level constants

    // game state (updating) constants
    final int UPDATE_STEP_SIZE_SEC = 60; // how much game time each
                                         // update will advance for
    final long UPDATE_STEP_SIZE_NS = UPDATE_STEP_SIZE_SEC * 1000000000;

    // rendering constants
    final int TARGET_FPS = 60;
    final int MAX_UPDATES_PER_RENDER = 10; // keeps us from rendering very
                        // infrequently if game state takes super long to update
    //final long TARGET_NS_BETWEEN_FRAMES = 1000000000 / TARGET_FPS; // TODO: do we need this?

    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////
    // Game level variables
    int timeInSecs = 0; // how much game time has elapsed since the start of the game

    boolean isRunning = false;

    // map of structure names to the structure for all structures in map
    Map<String, Structure> structureMap = new HashMap<String, Structure>();

    List<Building> buildingList = new ArrayList<Building>();
    PriorityQueue<Sentient> gameParticipants = new PriorityQueue<Sentient>(2, new Comparator<Sentient>() {

        @Override
        public int compare(Sentient o1, Sentient o2) {
            if (o1.getAction().getRemainingDurationInSecs() < o2.getAction().getRemainingDurationInSecs()) {
                return -1;
            } else if (o1.getAction().getRemainingDurationInSecs() > o2.getAction().getRemainingDurationInSecs()) {
                return 1;
            }
            return 0;
        }

    });

    /////////////////////////////////////////////////////////

    private void runGame() {
        Thread gameLoopThread = new Thread() {
            @Override
            public void run() {
                startGameLoop();
            }
        };
        gameLoopThread.start();
    }

    private void startGameLoop() {
        long lastUpdateTime = System.nanoTime(); // real time we last ran loop
        long timeToUpdate = 0L;

        isRunning = true;

        while (isRunning) {
            // step 1: deal with user input
            processUserInput();

            // step 2: update game state
            long currTime = System.nanoTime();
            timeToUpdate += lastUpdateTime - currTime;
            lastUpdateTime = currTime;

            int updateCount = 0;
            while (timeToUpdate >= UPDATE_STEP_SIZE_NS
                    && updateCount < MAX_UPDATES_PER_RENDER) {
                advanceStateForTime(UPDATE_STEP_SIZE_SEC);
                timeToUpdate -= UPDATE_STEP_SIZE_NS;
                ++updateCount;
            }

            // if we still have a sizable amount of time that we need to update
            // the state for, just give it up (the game will appear to lag)
            if (timeToUpdate >= UPDATE_STEP_SIZE_NS) {
                timeToUpdate = 0L;
            }

            // TODO: make thread yield to save CPU?

            // step 3: update UI
            updateUI();
        }
    }

    private void processUserInput() {
        // TODO Auto-generated method stub

    }

    /**
     * Update the state of all groups/creatures/entities in the game for a
     * specified amount of time. This sentients make decisions and events
     * are generated in carrying out this function.
     *
     * The following order is followed in one execution of the function:
     * 1) sentients make decisions
     * 2) game state progresses
     * 3) events are generated
     *
     * @param timeInSecs
     *            time to advance the state, in seconds
     */
    private void advanceStateForTime(long timeInSecs) {
        makeDecisions();

        // update remaining times (TODO: implement how to update state!)
        for (Sentient participant : gameParticipants) {
            participant.getAction().reduceRemainingDurationInSecs(timeInSecs);
        }

        this.timeInSecs += timeInSecs;

        // for testing
        System.out.println("game time is now: " + this.timeInSecs);

        generateEvents();
    }

    private void updateUI() {
        // TODO Auto-generated method stub

    }

    /**
     * look at the current state of the world and determine whether certain
     * events should take place, thus changing the world state
     */
    private void generateEvents() {
        // =====================================================================================
        // for each structure, determine whether groups in the structure
        // encounter each other
        for (Structure structure : structureMap.values()) {
            // loop over each pair of Sentients in structure (s_i, s_j), where i
            // < j
            for (int i = 0; i < structure.getSentients().size() - 1; ++i) {
                for (int j = i + 1; j < structure.getSentients().size(); ++j) {
                    Sentient sentientI, sentientJ;
                    sentientI = structure.getSentients().get(i);
                    sentientJ = structure.getSentients().get(j);

                    if (GameUtil.chancePercentage(sentientI.getEncounterProbabiltyWith(sentientJ))) {
                        // update sentientI's state
                        if (sentientI.getAction().getType() != Action.ActionType.ENCOUNTERED) {
                            sentientI.setAction(
                                    new Action(Action.ActionType.ENCOUNTERED, Action.ENCOUNTER_DURATION_IN_SECS));
                        }
                        sentientI.getAction().getEncounteredSentients().add(sentientJ);

                        // update sentientJ's state
                        if (sentientJ.getAction().getType() != Action.ActionType.ENCOUNTERED) {
                            sentientJ.setAction(
                                    new Action(Action.ActionType.ENCOUNTERED, Action.ENCOUNTER_DURATION_IN_SECS));
                        }
                        sentientJ.getAction().getEncounteredSentients().add(sentientI);
                    }
                }
            } // end of: loop over each pair of Sentients in structure (s_i,
              // s_j), where i < j
        }
        // =====================================================================================
    }

    /**
     *  go through the Sentient list, find the one has time remaining equal to zero and let them make decisions
     *  Does not advance time.
     */
    private void makeDecisions() {
        if (gameParticipants.isEmpty()) {
            return;
        }

        while (0 == gameParticipants.peek().getAction().getRemainingDurationInSecs()) {
            Sentient temp = gameParticipants.poll();
            temp.makeDecision();
            gameParticipants.add(temp);
        }
    }

    private void constructMap() throws IOException {
        String mapFileName = "res/UW_Map.txt"; // TODO: figure out where to put
                                               // this file

        FileReader input = new FileReader(mapFileName);
        BufferedReader bufRead = new BufferedReader(input);
        try {
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
                if (0 == lineItems[2].compareTo(new String("B"))) {
                    newStructure = new Building(Integer.parseInt(lineItems[1]), lineItems[0]);
                } else if (0 == lineItems[2].compareTo(new String("L"))) {
                    newStructure = new Link(Integer.parseInt(lineItems[1]), lineItems[0]);
                } else {
                    throw new RuntimeException(
                            "Incorrect format for structure line! " + "Expected either 'B' or 'L' at the end of line!");
                }

                structureMap.put(newStructure.name, newStructure);
                if (newStructure instanceof Building) {
                    buildingList.add((Building) newStructure);
                }

            }

            // link the nodes
            line = bufRead.readLine();
            int numOfConnections = Integer.parseInt(line);
            for (int i = 0; i < numOfConnections; ++i) {
                line = bufRead.readLine();
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

    public void populateMap(int zombieNum, int humanNum) {
        // create initial creature and possibly group
        for (int n = 0; n < zombieNum; n++) {
            Group temp = new Group();
            temp.addMember(new Zombie());
            temp.setAction(new Action(Action.ActionType.IDLE, 60)); // TODO: don't hardcode this later
            gameParticipants.add(temp);
        }
        for (int n = 0; n < humanNum; n++) {
            Group temp = new Group();
            temp.addMember(new Human());
            temp.setAction(new Action(Action.ActionType.IDLE, 60)); // TODO: don't hardcode this later
            gameParticipants.add(temp);
        }

        placeParticipants();
    }

    /**
     * put gameParticipants in randomized locations
     */
    private void placeParticipants() {
        // for testing
        int gameParticipantsSize = gameParticipants.size();

        int numOfBuildings = buildingList.size();
        for (Sentient participant : gameParticipants) {
            Structure targetBuilding = buildingList.get(GameUtil.nextInt(numOfBuildings));

            participant.setLocation(targetBuilding);
            targetBuilding.addSentient(participant);
        }

        // for testing
        assert (gameParticipantsSize == gameParticipants.size());
    }

    /**
     * prints out information about the map and the things in the map For
     * testing purposes for now
     */
    public void printMap() {
        for (Structure structure : structureMap.values()) {
            System.out.println("In " + structure.name + ":");
            for (Sentient sentient : structure.getSentients()) {
                System.out.println("\t" + sentient.getName());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("HELLO WORLD");

        Game game = new Game();
        game.constructMap();
        game.populateMap(5, 5);
        game.runGame();
    }
}
