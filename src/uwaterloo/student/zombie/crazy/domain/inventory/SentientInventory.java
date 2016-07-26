package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;

public class SentientInventory 
{
    int m_capacity;  
    int m_weightLimit;

    ArrayList<Item*> m_food;
    ArrayList<Item*> m_medcine;
    ArrayList<Item*> m_Weapon;
    ArrayList<Item*> m_Helmet;
    ArrayList<Item*> m_footwear;

    void calculateCapacity(int numCreature)
    {
        limit = numCreature * 15; // hardcode 15 for now 
    }

    void merge()
    {
        //TODO, when merge the Inventory of two Sentient 
    }
}
