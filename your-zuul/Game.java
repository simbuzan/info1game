/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    validWords valid;
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");

        // initialise room exits
        outside.setExits(null, theater, lab, pub, false);
        theater.setExits(null, null, null, outside, true);
        pub.setExits(null, outside, null, null, true);
        lab.setExits(outside, office, null, null, false);
        office.setExits(null, null, null, lab, true);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            String output = processCommand(command);
            finished = (null == output);
            if (!finished)
            {
                System.out.println(output);
            }
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * This is a further method added by BK to
     * provide a clearer interface that can be tested:
     * Game processes a commandLine and returns output.
     * @param commandLine - the line entered as String
     * @return output of the command
     */
    public String processCommand(String commandLine){
        Command command = parser.getCommand(commandLine);
        return processCommand(command);
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("You are " + currentRoom.getDescription());
        System.out.print("Exits: ");
        printStatus();
        System.out.println();
    }
    
    
    private void printStatus()
    {
        if(currentRoom.northExit != null) {
            System.out.print("north ");
        }
        if(currentRoom.eastExit != null) {
            System.out.print("east ");
        }
        if(currentRoom.southExit != null) {
            System.out.print("south ");
        }
        if(currentRoom.westExit != null) {
            System.out.print("west ");
        }   
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private String processCommand(Command command) 
    {
        boolean wantToQuit = false;
        if(command.isUnknown()) {
            return "I don't know what you mean...";       
        }
        String result = null;
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            result = printHelp();
        }
        else if (commandWord.equals("go")) {
            result = goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            result = quit(command);
        }
        else if (commandWord.equals("eat")) {
            result = eat(command);
        }
        else if (commandWord.equals("look")) {
            result = look(command);
        }
        else if (commandWord.equals("hack")) {
            result = hack(command);
            TimeBomb(5000);
        }
        return result ;
    }
    /**
     * gets all the command options 
     * @return String neatly cut up for use on the user side
     */
    private String getCommandOptions()
    {
        String out = "";
        for (validWords v: valid.values())
        {
            out += v.toString();
            out += ", ";
        }
        // removes the last komma
        out = out.substring(0, out.length() - 2);
        return out;
    }
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private String printHelp() 
    {
      
        return "You are lost. You are alone. You wander"
        +"\n"
        + "around at the university."
        +"\n"
        +"\n"
        +"Your command words are:" 
        +"\n"
        + getCommandOptions() 
        +"\n";
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private String goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            return "Go where?";
        }
        String direction = command.getSecondWord();
        // Try to leave current room.
        Room nextRoom = null;
        if(direction.equals("north")) {
            nextRoom = currentRoom.northExit;
        }
        if(direction.equals("east")) {
            nextRoom = currentRoom.eastExit;
        }
        if(direction.equals("south")) {
            nextRoom = currentRoom.southExit;
        }
        if(direction.equals("west")) {
            nextRoom = currentRoom.westExit;
        }
        String result = "";
        if (nextRoom == null) {
            result += "There is no door!";
        }
        else {
            currentRoom = nextRoom;
            result += "You are " + currentRoom.getDescription()+"\n";
            result += "Exits: ";
            if(currentRoom.northExit != null) {
                result += "north ";
            }
            if(currentRoom.eastExit != null) {
                result += "east ";
            }
            if(currentRoom.southExit != null) {
                result += "south ";
            }
            if(currentRoom.westExit != null) {
                result += "west ";
            }         
        }
        return result + "\n";
    }
    /**
     * @param a Command object
     * 
     * returns String saying that you have eaten
     */
    private String eat(Command command)
    {
        if (currentRoom.containsFood){
            return "You have eaten now and are not hungry any more";
        }
        else{
            return "there is no food here getting hangry";
        }
    }
    /**
     * @param a Command object
     * 
     * returns String saying what you see 
     */
    private String look(Command command)
    {
        return "looking around not really much too see";
    }
    /**
     * @param a Command object
     * 
     * returns String saying what you see 
     */
    private String hack(Command command)
    {
        return "Wrong move, 5 seconds till selfdesctruction";
    }
    /**
     * @param Time paused before main event in MS
     * 
     * sets the selfdestruction event up
     */
    private void TimeBomb(int time)
    {
       try        
       {
           Thread.sleep(time);
       } 
       catch(InterruptedException ex) 
       {
           System.out.println("oops");
       }
       System.exit(1);
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private String quit(Command command) 
    {
        if(command.hasSecondWord()) {
            return "Quit what?";
        }
        else {
            return null;  // signal that we want to quit
        }
    }
}
