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
    public void getStatus()
    {
        return status;
    }

    @Override
    public void SetEncounterTarget(Group group)
    {
        this.group = group; 
    }
        
    // Under development 
    @Override 
    public void ResolveAction() 
    {
        switch( action.GetType() )
        {
            case FIGHTING:
                if ( action.GetStatus() )
                {
                    System.printout.ln("Congradulation, after hours of brave fight, enemy has been annihilated");
                }
                else
                {
                    System.printout.ln("After hours of intense fight, enemy won the battle");
                    status = false;
                }
                break;
            case EXPLORING:
                if ( action.GetStatus() )
                {
                    System.printout.ln("After hours of exploring, this group find sth(to be implemented");
                }
                else
                {
                    System.printout.ln("After hours of exploring, this group find nothing");
                }
                break;
            case ENCOUNTERING:
                System.printout.ln("Group accidently encounter with another group");
                break;
            case RUNNING:
                if ( action.GetStatus() )
                {
                    System.printout.ln("After hours of escaping, this group successfully escape from enemy");
                }
                else
                {
                    System.printout.ln("After hours of escaping, unfortunately this group get annihilated");
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
