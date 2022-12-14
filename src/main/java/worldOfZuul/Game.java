package worldOfZuul;

import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class Game {
                                        /* Attributes */
    private Room currentRoom;                           // Points towards the current room object.
    private CommandWords commands;
    private boolean isCollected;                        // True if plastic in current room has been collected once.
    private boolean deadFishInteracted = false;
    private Date gameDate = new Date(122,         // Sets the date for the start of the game to October 2022.
            Calendar.OCTOBER,0);
    private Room gameHarbor;
    private Ship skipperSkrald = new Ship();
    private Room aboveHarbor;
    private int amountOfInteractions;
    private int z = 10;


                                        /* Accessor Methods */
    public boolean getDeadFishInteracted(){
        return deadFishInteracted;
    }
    public int getZ(){
        return z;
    }
    public Room getAboveHarbor() {
        return aboveHarbor;
    }
    public String getRoomDescription() {                    //Accessor method for the description of a room
        return currentRoom.getLongDescription();
    }
    public String getNavigation() {                         //Accessor method for the navigation
        return currentRoom.getWhereToSailNext();
    }
    public List<String> getCommandDescriptions() {          //Accessor method for commands
        return commands.getCommandWords();
    }
    public int getAmountOfInteractions() {
        return amountOfInteractions;
    }
    public void incrementAmountOfInteractions() {
        this.amountOfInteractions++;
    }
    public int getShipCapacity(){                           //Accessor method to return the current used capacity on the ship.
        return skipperSkrald.getCapacity();
    }
    public int getShipCapacityMax(){
        return skipperSkrald.getCapacityMax();
    }
    public boolean getIsCollected(){                        //Accessor method for the is collected boolean
        return isCollected;
    }
    public Room getCurrentRoom(){
        return currentRoom;
    }
    public String getCurrentRoomMapDirectory(){
        return currentRoom.getMapDirectory();
    }
    public long getScore(){                                 // Returns the score
        Harbor currentHarbor = (Harbor)gameHarbor;
        if(currentRoom.isHarbor()){
            currentHarbor = (Harbor)currentRoom;
        } return ((long)currentHarbor.getScore());
    }
    public String getGameDateMessage(){
        String[] months = {"Januar", "Februar", "Marts", "April", "Maj", "Juni", "Juli",  // String array of all the months
                "August", "September", "Oktober", "November", "December"};
        Calendar oneMonth = Calendar.getInstance();                                       // Making calender object oneMonth
        oneMonth.setTime(gameDate);                                                       // Setting time of the object to current gameDate
        oneMonth.add(Calendar.MONTH,+1);                                           // Increments with one month
        String message = months[oneMonth.get(Calendar.MONTH)] + " " + oneMonth.get(Calendar.YEAR); // Prints current month
        return message;
    }
    public void getDeathReason() {           //Accessor method for deathReason of a DeadFish object
        System.out.println(currentRoom.getDeadFishDeath());
        deadFishInteracted = true;
    }
    public Command getCommand(String word1, String word2) { //???
        return new CommandImplementation(commands.getCommand(word1), word2);
    }





                                        /* Mutator Methods */
    public void setIsCollected(boolean isCollected) {       //Mutator method for IsCollected
        this.isCollected = isCollected;
    }





                                    /* Methods and Functions */

    public Game() {                                     // Constructor for the game class
        createRooms();                                  // Creates the rooms in the game
        commands = new CommandWordsImplementation();
    }
    public void upgradeShip() {

        if (amountOfInteractions >= 22 && skipperSkrald.getCapacity()<14_000) {
            skipperSkrald.setCapacityMax(14_000);
            z = 24;
        } else if (amountOfInteractions >= 14 && skipperSkrald.getCapacity()<12_000) {
            skipperSkrald.setCapacityMax(12_000);
            z = 20;
        } else if (amountOfInteractions >= 8 && skipperSkrald.getCapacity()<10_000) {
            skipperSkrald.setCapacityMax(10_000);
            z = 16;
        } else if (amountOfInteractions >= 3 && skipperSkrald.getCapacity()<8_000) {
            skipperSkrald.setCapacityMax(8_000);
            z = 12;
        }
    }

    /** Creates the rooms of the game, 19 of which are ocean rooms, 6 are islands and one Harbor */
    private void createRooms() {
        Room A1, A2, A3, A4, A5;
        Room B1, B3, B4, B5;
        Room C1, C3;
        Room D1, D3, D4, D5;
        Room E1, E3, E4, E5;
        Room Harbor;

        // 19 ocean rooms as objects of Room.
        A1 = new Room("ude p?? havet", "??st","file:src/main/resources/MapFiles/A1.png",
                -274,384,-274,384,30,663);
        A2 = new Room("ude p?? havet", "??st","file:src/main/resources/MapFiles/A2.png",
                -384,384,-274,274,48,663);
        A3 = new Room("ude p?? havet", "syd","file:src/main/resources/MapFiles/A3-A4-D4.png",
                -384,384,-274,384,66,663);
        A4 = new Room("ude p?? havet", "vest eller syd","file:src/main/resources/MapFiles/A3-A4-D4.png",
                -384,384,-274,384,82,663);
        A5 = new Room("ude p?? havet", "vest eller syd","file:src/main/resources/MapFiles/A5-D5.png",
                -384,274,-274,384,97,663);
        B1 = new Room("ude p?? havet", "nord","file:src/main/resources/MapFiles/B1-C1-C3-D1.png",
                -274,274,-384,384,30,680);
        B3 = new Room("ude p?? havet", "syd","file:src/main/resources/MapFiles/B3-D3.png",
                -274,384,-384,384,66,680);
        B4 = new Room("ude p?? havet", "??st","file:src/main/resources/MapFiles/B4-E4.png",
                -384,384,-384,274,82,680);
        B5 = new Room("ude p?? havet", "??st","file:src/main/resources/MapFiles/B5-E5.png",
                -384,274,-384,274,97,680);
        C1 = new Room("ude p?? havet", "nord","file:src/main/resources/MapFiles/B1-C1-C3-D1.png",
                -274,274,-384,384,30,697);
        C3 = new Room("ude p?? havet", "syd","file:src/main/resources/MapFiles/B1-C1-C3-D1.png",
                -274,274,-384,384,66,697);
        D1 = new Room("ude p?? havet", "nord","file:src/main/resources/MapFiles/B1-C1-C3-D1.png",
                -274,274,-384,384,30,714);
        D3 = new Room("ude p?? havet", "syd","file:src/main/resources/MapFiles/B3-D3.png",
                -274,384,-384,384,66,714);
        D4 = new Room("ude p?? havet", "vest eller syd","file:src/main/resources/MapFiles/A3-A4-D4.png",
                -384,384,-274,384,82,714);
        D5 = new Room("ude p?? havet", "vest eller syd","file:src/main/resources/MapFiles/A5-D5.png",
                -384,274,-274,384,97,714);
        E1 = new Room("ude p?? havet", "nord","file:src/main/resources/MapFiles/E1.png",
                -274,274,-384,274,30,730);
        E3 = new Room("ude p?? havet", "syd","file:src/main/resources/MapFiles/E3.png",
                -274,384,-384,384,66,730);
        E4 = new Room("ude p?? havet", "vest","file:src/main/resources/MapFiles/B4-E4.png",
                -384,384,-384,274,82,730);
        E5 = new Room("ude p?? havet", "vest","file:src/main/resources/MapFiles/B5-E5.png",
                -384,274,-384,274,97,730);

        //Changed Object type from Room to Harbor
        Harbor = new Harbor("nu i havnen","Du er i havnen","file:src/main/resources/MapFiles/havn.png",
                -100,120,-384,150,66,742);
        gameHarbor = Harbor;

        // Setting exits for all the rooms
        Harbor.setExit("nord",E3);

        A1.setExit("syd",B1);
        A1.setExit("??st",A2);

        A2.setExit("vest",A1);
        A2.setExit("??st",A3);

        A3.setExit("vest",A2);
        A3.setExit("syd",B3);
        A3.setExit("??st",A4);

        A4.setExit("vest",A3);
        A4.setExit("syd",B4);
        A4.setExit("??st",A5);

        A5.setExit("vest",A4);
        A5.setExit("syd",B5);

        B1.setExit("nord",A1);
        B1.setExit("syd",C1);

        B3.setExit("nord",A3);
        B3.setExit("??st",B4);
        B3.setExit("syd",C3);

        B4.setExit("nord",A4);
        B4.setExit("vest",B3);
        B4.setExit("??st",B5);

        B5.setExit("nord",A5);
        B5.setExit("vest",B4);

        C1.setExit("nord",B1);
        C1.setExit("syd",D1);

        C3.setExit("nord",B3);
        C3.setExit("syd",D3);

        D1.setExit("nord",C1);
        D1.setExit("syd",E1);

        D3.setExit("nord",C3);
        D3.setExit("??st",D4);
        D3.setExit("syd",E3);

        D4.setExit("vest",D3);
        D4.setExit("syd",E4);
        D4.setExit("??st",D5);

        D5.setExit("vest",D4);
        D5.setExit("syd",E5);

        E1.setExit("nord",D1);

        E3.setExit("nord",D3);
        E3.setExit("syd",Harbor);
        E3.setExit("??st",E4);

        E4.setExit("vest",E3);
        E4.setExit("nord",D4);
        E4.setExit("??st",E5);

        E5.setExit("nord",D5);
        E5.setExit("vest",E4);

        // Current rooms starts with Harbor
        currentRoom = Harbor;
        aboveHarbor = E3;
    }
    public boolean isHarbor() {                             //???
        return currentRoom.isHarbor();
    }

    public CommandWords getCommands() {
        return commands;
    }

    /** Carries the logic for the GO command */
    public boolean goRoom(Command command) {
        if (!command.hasCommandValue()) {
            //No direction on command.
            //Can't continue with GO command.
            return false;
        }

        String direction = command.getCommandValue();
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            return false;
        } else {
            currentRoom = nextRoom;
            return true;
        }
    }

    /** Carries the logic for the QUIT command */
    public boolean quit(Command command) {
        return !command.hasCommandValue();
    }

    /** Carries the logic for the DISPOSE command */
    public boolean dispose() {
        if (currentRoom.isHarbor()) {
            int score = skipperSkrald.disposePlastic();
            ((Harbor)currentRoom).setScore(score);
            getScore();
            skipperSkrald.resetCapacity();
            return true;
        }
        return false;
    }

    /** Carries the logic for the COLLECT command */
    public boolean collect(){
        if (!isCollected && currentRoom.getCurrentPlastic().getAmount() <
                            (skipperSkrald.getCapacityMax()- skipperSkrald.getCapacity())) {
            skipperSkrald.collectPlastic(currentRoom.getCurrentPlastic());
            isCollected = true;
            currentRoom.getCurrentPlastic().setSpawnChance(false);
            return true;
        } else return false;
    }

    /** Function to check if the gameDate is currently 2050 or the current move will make it so */
    public boolean isIt2050() {
        return gameDate.compareTo(new Date(149, 10, 29)) >= 0;
    }

    /** Method for next move used when a player moves on the ocean */
    public void newMove() {
        String[] months = {"januar", "februar", "marts", "april", "maj", "juni", "juli",  // String array of all the months
                            "august", "september", "oktober", "november", "december"};
        Calendar oneMonth = Calendar.getInstance();                                       // Making calender object oneMonth
        oneMonth.setTime(gameDate);                                                       // Setting time of the object to current gameDate
        oneMonth.add(Calendar.MONTH,+1);                                           // Increments with one month
        System.out.println("Det er nu " + months[oneMonth.get(Calendar.MONTH)] + " i ??r "
                + oneMonth.get(Calendar.YEAR)); // Prints current month
        gameDate = oneMonth.getTime();                                                    // Sets the gameDate to the new date
        isCollected = false;
        deadFishInteracted = false;
    }
}

