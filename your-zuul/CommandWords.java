/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class holds an enumeration of all command words known to the game.
 * It is used to recognise commands as they are typed in.
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */


public class CommandWords
{
    validWords valid; 
    public CommandWords(){};
     /**
     * check if one the command given is valid
     * Returns a boolean value if it's valid
     */
    public boolean isCommand(String aString)
    {
        for(validWords v: valid.values())
        {
            if(v.name().equals(aString))
            {
                return true;
            }
        }
        return false;
    }
}
