package worldOfZuul;

import java.util.Set;
import java.util.HashMap;


public class Room 
{
                                        /* Attributes */
    private String description, whereToSailNext, mapDirectory;
    private HashMap<String, Room> exits;
    private Plastic currentPlastic;
    private DeadFish deadFishDeath;
    private boolean isFishDead;
    private int minXValue, maxXValue, minYValue, maxYValue, mapMarkerX, mapMarkerY, amountPlastic;


                                        /* Accessor Methods */
    public int getMapMarkerX(){
        return mapMarkerX;
    }
    public int getMapMarkerY(){
        return mapMarkerY;
    }
    public int getMaxXValue() {
        return maxXValue;
    }
    public int getMaxYValue() {
        return maxYValue;
    }
    public int getMinXValue() {
        return minXValue;
    }
    public int getMinYValue() {
        return minYValue;
    }
    public String getMapDirectory(){
        return mapDirectory;
    }
    public DeadFish getDeadFishDeath() {return deadFishDeath;}
    public Plastic getCurrentPlastic() {return currentPlastic;}
    public String getShortDescription() {return description;}
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }
    public String getWhereToSailNext() {
        return "Den hurtigste vej til havnen er: " + whereToSailNext + ", det tog lang tid at undersøge.";}
    public String getLongDescription() {
        int plastic = this.amountPlastic;
        boolean fish = this.isFishDead;
        if (checkRoom()) {
            return "Du er " + description + "\n" + getExitString(); }
        else if(plastic <= 0 && !fish){
            return "Du er " + description + ". Der er intet andet end vand" +"\n" + getExitString();}
        else if (plastic <= 0 && fish) {
            return "Du er " + description + ". Der er en død fisk. For at undersøge skriv >info< "
                    +"\n" + getExitString();}
        else if (plastic > 0 && fish) {
            return "Du er " + description + ". Der er en død fisk. For at undersøge skriv >info<"
                    +"\n" +"Der er " +plastic+ " tons plastik i vandet. >indsaml< "+"\n" + getExitString();}
        else if (plastic > 0 && !fish) {
            return "Du er " + description + ". Der er " +plastic+ " tons plastik i vandet. >indsaml< "
                    +"\n" + getExitString();}

        return "fejl i indlæsning af område";
    }
    private String getExitString()
    {
        String returnString = "Udveje:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }



                                            /* Mutator Methods */

    public void setExit(String direction, Room neighbor) {exits.put(direction, neighbor);}



                                            /* Methods and Functions */
    public Room(String description, String whereToSailNext,String mapDirectory,int minXValue, int maxXValue, int minYValue, int maxYValue, int mapMarkerX, int mapMarkerY)
    {
        this.whereToSailNext = whereToSailNext;
        this.description = description;
        this.mapDirectory = mapDirectory;
        this.minXValue = minXValue;
        this.maxXValue = maxXValue;
        this.minYValue = minYValue;
        this.maxYValue = maxYValue;
        this.mapMarkerX = mapMarkerX;
        this.mapMarkerY = mapMarkerY;
        exits = new HashMap<String, Room>();
    }

    public boolean spawnPlastic(){
        Plastic plastic = new Plastic();
        if(plastic.spawnChance()){
            plastic.spawn();
            this.amountPlastic = plastic.getAmount();
            this.currentPlastic = plastic;
            return true;
        }
        else if (!plastic.spawnChance()){
            this.amountPlastic = 0;
            this.currentPlastic = null;
            return false;
        }
        return false;
    }

    public boolean spawnDeadFish(){
        DeadFish fish = new DeadFish();
        if (fish.spawnChance()){
            fish.spawn();
            this.deadFishDeath = fish;
            this.isFishDead = true;
            return true;
        }
        else{
            this.deadFishDeath = null;
            this.isFishDead = false;
            return false;}
    }

    public boolean isHarbor() {
        return false;
    }

    public boolean checkRoom(){
        return getShortDescription() == "nu i havnen";
    }
}

