
/**
 * Object class for the objects that can be stored in the rooms
 * 
 * @author Daan Lockhorst, Steffie Imbuzan, Ella katasijto
 * @version 03.05.2018
 */
public class Item
{
    String Name;
    String Description;
    int Weight;
    Room whereIn;
    
    public Item(Room whereIn, String Name, String Description, int Weight)
    {
        this.whereIn = whereIn;
        this.Description = Description;
        this.Name = Name;
        this.Weight = Weight;
    }
    /**
    * @return int object weight
    */
    public int getWeight()
    {
        return Weight;
    }
     /**
    * @returns a room Object where the item is currently in
    */
    public Room getWhereIn()
    {
        return whereIn;
    }
    public void setWhereIn(Room w)
    {
        whereIn = w;
    }
     /**
    * @return String containing the object description
    */
    public String getDescription()
    {
        return Description;
    }
     /**
    * @return String containing the object description
    */
    public String getName()
    {
        return Name;
    }
}
