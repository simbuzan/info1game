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
    private Room player;
    Stack<Room> history = new Stack<Room>();
    public List<Item> itemList = new ArrayList<Item>();
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
        Item lecture, riddle, paper, usb, egg;
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
        
        // set all the exits of the rooms
        outside.setExits(null, theater, lab, pub);
        gaurdedGarden.setExits(theater, null, null, null);
        theater.setExits(null, null, gaurdedGarden, outside);
        pub.setExits(null, outside, null, null);
        lab.setExits(outside, office, null, null);
        office.setExits(null, null, computer, lab);
        computer.setExits(null, null, office, gaurded);
        gaurded.setExits(computer, null, tresure, null);
        tresure.setExits(tresure, null, gaurded, null);
        
        // create the Items used in the game
        lecture = new Item(theater, "knowledge", "some advanced code cracking knowledge", 0);
        riddle = new Item(office, "riddle", "some crazy riddle, might come in handy", 0);
        paper = new Item(computer, "paper", "A piece of paper with some code on it", 0);
        usb = new Item(lab, "usbStick", "a advanced looking usb stick", 0);
        egg = new Item(tresure, "fabergeEgg", "a beautifull faberge egg", 4);
        itemList.add(lecture);
        itemList.add(riddle);
        itemList.add(paper);
        itemList.add(usb);
        itemList.add(egg);
        
        
        
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
    /**
     * Prints out where you can go
     */
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
        else if(commandWord.equals("drop")) {
            result = drop(command);
        }
        else if(commandWord.equals("dropall")) {
            result = drop(command);
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
        result += "in this room there is, " + currentRoom.getDescription() +"\n";
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
            result += "in this room there is, " + whatsInCurrentRoom() +"\n";
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
        String out = "";
        for(Item i : playersItems)
        {
            out += i.getDescription();
            out += "\n";
        }
        if(out.length() > 1)
        {
            return "you have: " + (playerStrength - 1) + " piece/s of information, you need 4 and than find the egg to win "
            + "\n"
            + "You are carrying: " + out;
        }
            return "you haven't got anything yet";
        }
     /**
      * returns String
     * loops through items if in the room it adds it to a return string
     * if not than returns a pre given string
     */
    
    private String whatsInCurrentRoom()
    {   
        String out = "";
        for(Item i : itemList)
        {
            if (i.getWhereIn() == currentRoom)
            {
                out += i.getDescription();
            }
        }
        if (out.length() > 1){
            return out;
        }
        else{
            return "nothing here";
        }
    }
    /**
     * @param a Command object
     * 
     * returns String saying what you see 
     */
    private String look(Command command)
    {
        return currentRoom.getDescription() 
        + " there is " 
        + whatsInCurrentRoom();
    }
    /**
     * @param a Command object
     * picks up items and adds them to the playersItems stack
     * returns String for giving the player infomation on the game state
     */
    private String pickUp(Command command)
    {   
        
        for(Item i : itemList){
            if (i.getWhereIn() == currentRoom)
            {
                if(playerStrength > i.getWeight())
                {
                    playerStrength += 1;
                    itemList.remove(i);
                    playersItems.add(i);
                    i.setWhereIn(player);
                    if(playerStrength > 4)
                    {
                        win();
                    }
                    return "succes you picked up " + i.getDescription();
                }
                else
                {
                    System.out.println("you got caught the queen is angry");
                    timeBomb(1000);
                    return "you got caught the queen is angry";
                }
            }
        }
        return "nothing here";
    }
    /**
     * @param Command object
     * drops first thing picked up
     * returns String with succes or not info
     */
    private String drop(Command command)
    {
        if(playersItems.size() > 0)
        {
            Item i = playersItems.get(0);
            playersItems.remove(0);
            playerStrength -= 1;
            i.setWhereIn(currentRoom);
            itemList.add(i);
            return "dropped " + i.getDescription();
        }
        return "you have nothing to drop";
    }
    /**
     * @param Command object
     * drops everything
     * returns String with succes info or not
     */
    private String dropAll(Command command)
    {
        if(playersItems.size() > 0)
        {
            for(Item i : playersItems)
            {
                i.setWhereIn(currentRoom);
                itemList.add(i);
                playerStrength -= 1;
                playersItems.remove(i);
            }
            return "dropped everything";
        }
        return "you have nothing to drop";
    }
    /**
     * win function ends the game upon completion
     */
    private void win(){
        for(int i = 0; i == 10; i++)
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
    public void timeBomb(int time)
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
