package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;

public class Group implements Sentient
{
	// constants:
	private static final float ENCOUNTER_PROB = (float) 1.0; // in percentage per minute

	Action action;
	ArrayList<Creature> members = new ArrayList<Creature>();
	Structure location;
    Group target;

    // for now, true means group is active, false means group no longer exist,
    // need to be removed from game participant
    boolean status;

    //TODO Inventory class
    //TODO Group Name

    public void setEncounterTarget(Group group)
    {
        this.target = group;
    }

    public void addMember(Creature creature)
    {
		if( !members.isEmpty()){
			if( (creature instanceof Human && members.get(0) instanceof Zombie)
					|| (creature instanceof Zombie && members.get(0) instanceof Human) ){
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
		// TODO Auto-generated method stub

	}

    @Override
    public boolean getStatus()
    {
        return status;
    }

    // Under development
    @Override
    public void ResolveAction()
    {
        switch( action.getType() )
        {
            case FIGHTING:
                if ( action.getStatus() )
                {
                    System.out.print("Congradulation, after hours of brave fight, enemy has been annihilated");
                }
                else
                {
                    System.out.print("After hours of intense fight, enemy won the battle");
                    status = false;
                }
                break;
            case EXPLORING:
                if ( action.getStatus() )
                {
                    System.out.print("After hours of exploring, this group find sth(to be implemented");
                }
                else
                {
                    System.out.print("After hours of exploring, this group find nothing");
                }
                break;
            case ENCOUNTERING:
                System.out.print("Group accidently encounter with another group");
                break;
            case RUNNING:
                if ( action.getStatus() )
                {
                    System.out.print("After hours of escaping, this group successfully escape from enemy");
                }
                else
                {
                    System.out.print("After hours of escaping, unfortunately this group get annihilated");
                    status = false;
                }
                break;
        }
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
