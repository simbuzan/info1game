/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */
public class Room 
{
    public String description;
    public int objectNR;
    public Room northExit;
    public Room southExit;
    public Room eastExit;
    public Room westExit;
    public Item[] ob = new Item[10];
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
    }
    /**
     * Define the exits of this room.  Every direction either leads
     * to another room or is null (no exit there).
     * @param north The north exit.
     * @param east The east east.
     * @param south The south exit.
     * @param west The west exit.
     * @param objectNR
      */
    public void setExits(Room north, Room east, Room south, Room west,
                    int objectNR, String objectDescription, int objectWeight)
    {
        if(north != null) {
            northExit = north;
        }
        if(east != null) {
            eastExit = east;
        }
        if(south != null) {
            southExit = south;
        }
        if(west != null) {
            westExit = west;
        }
        this.objectNR = objectNR;
        if (objectNR != -1)
        {
            addItem(objectNR, objectDescription, objectWeight);
        }
    }
    /**
     * add item to the item array 
     * @param objectNR the index to link to rooms
     * @param String objectDescription tells what the object is and does
     * @param int objectWeigth checks if player stregth is enought to pick up the objects
     */
    public void addItem(int objectNR, String objectDescription, int objectWeight)
    {
        ob[objectNR] = new Item(objectDescription, objectWeight);
    }
    /**
     * 
     * @Returns a object description of whats in the room
     */
    public String getObjectDescription()
    {
        if (objectNR == -1)
        {
            return "nothing here";
        }
        else 
        {
            return ob[objectNR].getDescription();
        }
    }
    public int getObjectWeight()
    {
        if(objectNR == -1)
        {
            return -1;
        }
        else
        {
            return ob[objectNR].getWeight();
        }
    }
    public Item getItem()
    {
        return ob[objectNR];
    }
    /**    
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

}
