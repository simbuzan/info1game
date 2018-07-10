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
import java.util.*;

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room prevRoom;
    Stack<Room> history = new Stack<Room>();
    Stack<Item> playersItems = new Stack<Item>();
    validWords valid;
    int playerStrength = 1;
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
        Room outside, theater, pub, lab, office, tresure, gaurded, computer, gaurdedGarden;
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        tresure = new Room("in a beautifull room full of amazing stuff");
        gaurded = new Room("in a place which looks like it full of gaurds");
        computer = new Room("in the maintainance room, I can see a computer");
        gaurdedGarden = new Room("in the gaurdhaus in the garden");
        
        /**
         * @param Room northexit
         * @param Room westexit
         * @param Room southexit
         * @param room eastexit 
         * @param place in objectArray -1 is no objects there to pickup
         * @param String object description
         * @param int if the object is pickup able by the player +100 
         */
        outside.setExits(null, theater, lab, pub, -1, "nothing to see here", 0);
        gaurdedGarden.setExits(theater, null, null, null, 2, "a piece of paper with some wierd scribbles on it", 0);
        theater.setExits(null, null, gaurdedGarden, outside, -1, "a guard walking around better leave soon", 0);
        pub.setExits(null, outside, null, null, -1, "nothing there", 0);
        lab.setExits(outside, office, null, null, -1, "reasearch and stuff", 0);
        office.setExits(null, null, computer, lab, 3, "a piece of paper with a code of some kind", 0);
        computer.setExits(null, null, office, gaurded, 4, "a mainframecomputer with numbers on them", 0);
        gaurded.setExits(computer, null, tresure, null, 5, "hey another piece of codepaper", 0);
        tresure.setExits(tresure, null, gaurded, null, 6, "the magical looking fabere egg", 4);
        
        // makes the lastRoom for going back in time
        history.push(outside);
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
        System.out.println("Welcome to the A Fancy omlet!");
        System.out.println("It is your goal to steal enough infomation so you can hack the safe");
        System.out.println("Inside of the safe is the queen of englands faberge egg collection");
        System.out.println("It is your goal to get the eggs and make yourself a fancy omelet");
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
        else if (commandWord.equals("status")) {
            result = status(command);
        }
        else if (commandWord.equals("look")) {
            result = look(command);
        }
        else if (commandWord.equals("pickup")) {
            result = pickUp(command);
        }
        else if(commandWord.equals("back")) {
            result = back(command);
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
     * Command to go back uses the global lastRoom Room 
     * where the previous room is saved and than goes "back" to there
     * @return String with the room description 
     */
    private String back(Command command)
    {
        String result = ""; 
        currentRoom = history.peek();
        history.pop();
        result += "You are " + currentRoom.getDescription()+"\n";
        result += "in this room there is, " + currentRoom.getObjectDescription() +"\n";
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
        return result;
    }
    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private String goRoom(Command command) 
    {   
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            return "please tell where to go";
        }
        Room nextRoom = null;
        history.push(currentRoom);
        String direction = command.getSecondWord();
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
            result += "There is no door, Auww my head!";
        }
        else {
            currentRoom = nextRoom;
            result += "You are " + currentRoom.getDescription()+"\n";
            result += "in this room there is, " + currentRoom.getObjectDescription() +"\n";
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
     * streamcode example from youtube video about javaStreams 
     * returns String saying that you have eaten
     */
    private String status(Command command)
    {
        String resu = "";
        if (playerStrength  <= 1){
            return "you have no items yet";
        }
        else{
            for(Item i : playersItems){
                resu += i.getDescription();
                resu += "\n";
            }
            return "you have: " + (playerStrength -1) + " piece/s of information, you need 5 and than find the egg to win "
            + "\n"
            + "You are carrying: " + resu;
        }
        
    }
    /**
     * @param a Command object
     * 
     * returns String saying what you see 
     */
    private String look(Command command)
    {
        return currentRoom.getObjectDescription();
    }
    /**
     * @param a Command object
     * picks up items and adds them to the playersItems stack
     * returns String for giving the player infomation on the game state
     */
    private String pickUp(Command command)
    {   
        if(currentRoom.getObjectWeight() > playerStrength)
        {
            timeBomb(1000);
            System.out.println("AAAAAAGH the alarms run try again tomorrow!");
            return "you loose";
        }
        if(currentRoom.getObjectWeight() < playerStrength)
        {
            playersItems.push(currentRoom.getItem());
            playerStrength += 1;
            if(playerStrength == 6)
            {
                win();
            }
            return "succes picked up "
            + currentRoom.getObjectDescription() 
            + "\n"
            + " you still need: " + (5 - playerStrength)
            + " pieces of information to get the egg"; 
        }
        else
        {
            return "there is nothing here";
        }
    }
    //win function runs when the player wins
    private void win(){
        for(int i = 0; i == 1000; i++)
        {
            System.out.println("yeeeeeeeeeeeeeeeeeeeeeey your rich !!!!!");
            System.out.println("/n");
        }
    }
    /**
     * @param Time paused before main event in MS
     * 
     * sets the selfdestruction event up for alarms in the game
     */
    private void timeBomb(int time)
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
