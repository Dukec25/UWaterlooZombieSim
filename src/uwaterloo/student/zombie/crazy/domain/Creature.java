package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;
import java.util.List;

import uwaterloo.student.zombie.crazy.domain.Action.ActionType;
import uwaterloo.student.zombie.crazy.domain.Incentive.ActionWeightPair;

public class Creature
{
    List<Incentive> incentives = new ArrayList<Incentive>();

    int hp;
    int hunger = 0; // 0 means not hungry, 100 means starving

    int speed;
    int strength;

    public List<Incentive> getIncentives() {
        return incentives;
    }

    /**
     * Generate incentives based on current state.
     */
    public void generateIncentives() {
        incentives.clear();

        {
            List<ActionWeightPair> awpairs = new ArrayList<ActionWeightPair>();
            awpairs.add(new ActionWeightPair(ActionType.EATING, hunger * 2));
            awpairs.add(new ActionWeightPair(ActionType.EXPLORING, hunger));
            Incentive hungerIncentive = new Incentive(awpairs, "motivated by hunger");
            incentives.add(hungerIncentive);
        }

        {
            int curiosity = 10; // TODO: making this hardcoded for now
            List<ActionWeightPair> awpairs = new ArrayList<ActionWeightPair>();
            awpairs.add(new ActionWeightPair(ActionType.EXPLORING, curiosity * 2));
            awpairs.add(new ActionWeightPair(ActionType.MOVING, curiosity));
            Incentive hungerIncentive = new Incentive(awpairs, "driven by curiosity");
            incentives.add(hungerIncentive);
        }
    }
}
