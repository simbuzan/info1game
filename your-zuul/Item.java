
/**
 * Object class for the objects that can be stored in the rooms
 * 
 * @author Daan Lockhorst, Steffie Imbuzan, Ella katasijto
 * @version 03.05.2018
 */
public class Item
{
    int objectNR;
    String objectDescription;
    int objectWeight;
    
    
    public Item(String objectDescription, int objectWeight)
    {
        this.objectDescription = objectDescription;
        this.objectWeight = objectWeight;
    }
    /**
    * @return int object weight
    */
    public int getWeight()
    {
        return objectWeight;
    }
     /**
    * @return String containing the object description
    */
    public String getDescription()
    {
        return objectDescription;
    }
}
