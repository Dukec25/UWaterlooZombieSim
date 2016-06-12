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
    final int UPDATE_STEP_SIZE_SEC = 60; // how much game time each
                                         // update will advance for
    final long UPDATE_STEP_SIZE_NS = UPDATE_STEP_SIZE_SEC * 1000000000;

    /////////////////////////////////////////////////////////
    // Game level variables
    final SpeedConfig gameSpeed;
    int timeInSecs = 0; // how much game time has elapsed since the start of the game

    boolean yieldingMode = true; // whether we yield the CPU (thus consuming less
        // power) when we are running sufficiently fast. Try turning this off if
        // you find the game lagging horribly

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

    Game() {
        gameSpeed = new SpeedConfig(60, 120.0);
    }

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
        long lastUpdateTime = System.nanoTime(); // the last (real) time we
                                        // updated the game state
        long lastRenderTime = lastUpdateTime;
        long gameTimeToUpdate = 0L;

        isRunning = true;

        while (isRunning) {
            // step 1: deal with user input
            processUserInput();

            // step 2: update game state
            long currTime = System.nanoTime();
            gameTimeToUpdate += (lastUpdateTime - currTime) * gameSpeed.gameSpeedRatio;
            long targetRenderTime = lastRenderTime + gameSpeed.nsBetweenFrames;

            while (gameTimeToUpdate >= UPDATE_STEP_SIZE_NS && currTime <= targetRenderTime) {
                updateStateOneStep();
                gameTimeToUpdate -= UPDATE_STEP_SIZE_NS;
                currTime = System.nanoTime();
            }
            lastUpdateTime = currTime;

            // if we still have a sizable amount of time that we need to update
            // the state for, just give it up (the game will slow down)
            if (gameTimeToUpdate >= UPDATE_STEP_SIZE_NS) {
                gameTimeToUpdate = 0L;
            }

            // step 3: if we're going really fast, wait a little to not burn the CPU!
            if (yieldingMode){
                while (currTime < targetRenderTime)
                {
                   Thread.yield();

                   // (from http://www.java-gaming.org/index.php?topic=24220.0)
                   //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                   //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                   //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                   try {Thread.sleep(1);} catch(Exception e) {}

                   currTime = System.nanoTime();
                }
            }

            // step 4: update UI (render)
            updateUI();
            lastRenderTime = System.nanoTime();
        }
    }

    private void processUserInput() {
        // TODO Auto-generated method stub

    }

    /**
     * Update the state of the game for one step.
     *
     * The following happens (in order):
     * 1) sentients make decisions
     * 2) game state progresses
     * 3) events are generated
     *
     */
    private void updateStateOneStep() {
        makeDecisions();

        // update remaining times (TODO: implement how to update state!)
        for (Sentient participant : gameParticipants) {
            participant.getAction().reduceRemainingDurationInSecs(UPDATE_STEP_SIZE_SEC);
        }

        this.timeInSecs += UPDATE_STEP_SIZE_SEC;

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

    /**
     * Stores data on how fast the game is running and some other timing
     * configuration
     *
     * Only use the methods to edit your speed configuration, but you may access
     * configuration values by evaluating the variables directly.
     */
    class SpeedConfig {
        int targetFPS;
        long nsBetweenFrames;

        /**
         * Ratio of game time to update vs. real time passed; defines game speed
         * e.g. a ratio of 10.0 means the game time should advance 10 minutes
         * every minute (the game is 10 times as fast as real life)
         *
         * Note that this game speed may not be met if your machine is slow...
         */
        double gameSpeedRatio;

        SpeedConfig(int targetFPS, double gameSpeedRatio) {
            setTargetFPS(targetFPS);
            setGameSpeedRatio(gameSpeedRatio);
        }

        public void setTargetFPS(int targetFPS) {
            this.targetFPS = targetFPS;
            nsBetweenFrames = 1000000000 / targetFPS;
        }

        public void setGameSpeedRatio(double gameSpeedRatio) {
            this.gameSpeedRatio = gameSpeedRatio;
        }

    }

}
