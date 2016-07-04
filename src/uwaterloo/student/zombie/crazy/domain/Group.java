package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uwaterloo.student.zombie.crazy.domain.Action.ActionType;

public class Group implements Sentient {
    // constants:
    private static final float ENCOUNTER_PROB = (float) 1.0; // in percentage
                                                             // per minute

    Action action;
    ArrayList<Creature> members = new ArrayList<Creature>();
    Structure location;

    public void addMember(Creature creature)
    {
        if (!members.isEmpty()) {
            if ((creature instanceof Human && members.get(0) instanceof Zombie)
                    || (creature instanceof Zombie && members.get(0) instanceof Human)) {
                throw new RuntimeException("incompatible type");
            }
        }
        members.add(creature);

    }

    public void removeMember(Creature creature)
    {
        members.remove(creature);
    }

    @Override
    public void makeDecision()
    {
        if (members.isEmpty()) { return; }

        List<Incentive.ActionWeightPair> actionWeightPairs  = members.stream()
                .flatMap(m -> m.incentives.stream())
                .flatMap(incentive -> incentive.getActionWeightPairs().stream())
                .collect(Collectors.toList());
        List<Integer> weights = actionWeightPairs.stream()
                .mapToInt(pair -> pair.weight)
                .boxed()
                .collect(Collectors.toList());
        final int chosenActionIdx = GameUtil.randomlyChooseByWeight(weights);

        ActionType chosenActionType = actionWeightPairs.get(chosenActionIdx)
                .getActionType();
        // TODO: figure out how to determine length of action later
        final long DEFAULT_ACTION_DURATION_SECS = 60 * 60;
        Action action = new Action(chosenActionType, DEFAULT_ACTION_DURATION_SECS);

        this.setAction(action);
    }

    @Override
    public Action getAction()
    {
        return action;
    }

    @Override
    public float getEncounterProbabiltyWith(Sentient other)
    {
        return ENCOUNTER_PROB;
    }

    @Override
    public void setAction(Action action)
    {
        this.action = action;
    }

    @Override
    public Structure getLocation()
    {
        return location;
    }

    @Override
    public void setLocation(Structure location)
    {
        this.location = location;
    }

    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void advanceStateForTime(int secs)
    {
        // TODO Auto-generated method stub

    }

}
