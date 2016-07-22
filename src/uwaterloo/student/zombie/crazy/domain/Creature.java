package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;
import java.util.List;

public class Creature
{
    List<Incentive> incentives = new ArrayList<Incentive>();

    int hp;
    int speed;
    int strength;

    public List<Incentive> getIncentives() {
        return incentives;
    }
}
