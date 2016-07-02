package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;
import java.util.List;

import uwaterloo.student.zombie.crazy.domain.Action.ActionType;

/**
 * Represents a drive to do something, affects decision making;
 * An incentive may encourage multiple actions, for example: hunger may
 */
public class Incentive
{
    List<ActionWeightPair> actionWeightPairs = new ArrayList<ActionWeightPair>();
    String flavorText;

    Incentive(List<ActionWeightPair> actionWeightPairs, String flavorText)
    {
        this.actionWeightPairs = actionWeightPairs;
        this.flavorText = flavorText;
    }

    Incentive(List<ActionWeightPair> actionWeightPairs)
    {
        this(actionWeightPairs, "Unspecified incentive");
    }

    /**
     * Proportionally scales all IncentivePairs so that their total weight (sum)
     * becomes the given value.
     * @param sum
     */
    public void scaleToTotalWeight(int totalWeight)
    {
        if (actionWeightPairs.isEmpty()) {
            return;
        }

        Double targetToCurrRatio = totalWeight / (getTotalWeight() * 1.0);
        // scale up all actionWeightPairs except for the last one. Tweak the last
        // one to make sure the new total weight is exactly as specified.
        for (int i = 0; i < actionWeightPairs.size() - 1; i++) {
            actionWeightPairs.get(i).weight *= targetToCurrRatio;
        }
        actionWeightPairs.get(actionWeightPairs.size() - 1).weight
                += (totalWeight - getTotalWeight());
    }

    public List<ActionWeightPair> getActionWeightPairs()
    {
        return actionWeightPairs;
    }

    public int getTotalWeight()
    {
        return actionWeightPairs.stream()
                .mapToInt((actionWeightPair) -> actionWeightPair.weight).sum();
    }

    /**
     * Plain old data type representing incentive for exactly one specific action
     *
     */
    public class ActionWeightPair
    {
        // TODO: actionType is really generic, eventually, we should have
        // incentives for more specific actions like "move to XYZ" rather than just "move".
        ActionType actionType;
        int weight;

        public ActionWeightPair(ActionType actionType, int weight) {
            this.actionType = actionType;
            this.weight = weight;
        }

        public ActionType getActionType() {
            return actionType;
        }

        public void setActionType(ActionType actionType) {
            this.actionType = actionType;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
}
