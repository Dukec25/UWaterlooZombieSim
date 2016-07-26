package uwaterloo.student.zombie.crazy.domain;

import java.util.ArrayList;

public class CreatureInventory 
{
    Item* m_helmet;
    Item* m_lefthand;
    Item* m_righthand;
    Item* m_footwear;
    Item* m_cloth; 
    int capacity; // only useful to calculate sentient inventory capacity  

    ArrayList<Item*> unEquipAll() 
    {
        //TODO, set everything to null
    }

    void equip(Helmet* helmet, Weapon* weapon, Weapon* weapon2, Footwear* footwear, Cloth* cloth)
    {
        //TODO
    }

    //TODO, Method determine which equipment is missing 
}
