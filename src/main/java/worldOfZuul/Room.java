package worldOfZuul;

import java.util.Set;
import java.util.HashMap;


public class Room 
{
    private String description;
    private String whereToSailNext;
    private HashMap<String, Room> exits;
    private Plastic currentPlastic;
    private int amountPlastic;
    private DeadFish deadFishDeath;
    private boolean isFishDead;
    private int minXValue;
    private int maxXValue;
    private int minYValue;
    private int maxYValue;
    private int mapMarkerX;
    private int mapMarkerY;
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

    private String mapDirectory;
    public String getMapDirectory(){
        return mapDirectory;
    }
    public Room(){
        this.description = "Dette rum er tomt";
    }
    public Room(String description)
    {
        this.description = description;
        exits = new HashMap<String, Room>();
    }
    public Room(String description, String whereToSailNext,String mapDirectory)
    {
        this.whereToSailNext = whereToSailNext;
        this.description = description;
        this.mapDirectory = mapDirectory;
        exits = new HashMap<String, Room>();
    }
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

    boolean spawned;
    public boolean spawnPlastic(){
//      Metode der laver plastik objekt, kører spawnchance og herefter gemmer tilfældig mængde i "currentPlastic" og retunere "amount".
        Plastic plastic = new Plastic();
        if(plastic.spawnChance() == true){
            plastic.spawn();
            this.amountPlastic = plastic.getAmount();
            this.currentPlastic = plastic;
            return true;
        }
        else if (plastic.spawnChance() == false){
            this.amountPlastic = 0;
            this.currentPlastic = null;
            return false;
        }
        return false;
    }
    public Plastic getCurrentPlastic() {return currentPlastic;}
    public String getWhereToSailNext() {
        return "Den hurtigste vej til havnen er: " + whereToSailNext + ", det tog lang tid at undersøge.";}

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

    public DeadFish getDeadFishDeath() {return deadFishDeath;}
    public void setExit(String direction, Room neighbor) {exits.put(direction, neighbor);}
    public String getShortDescription() {return description;}

    public String getLongDescription() {
        int plastic = this.amountPlastic;
        boolean fish = this.isFishDead;
//      Hvis man er på havnen
        if (checkRoom()) {
            return "Du er " + description + "\n" + getExitString(); }
//        Hvis der er hverken fisk eller plast
        else if(plastic <= 0 && !fish){
            return "Du er " + description + ". Der er intet andet end vand" +"\n" + getExitString();}
//        Hvis der er fisk men ikke plast
        else if (plastic <= 0 && fish) {
            return "Du er " + description + ". Der er en død fisk. For at undersøge skriv >info< "
                    +"\n" + getExitString();}
//        Hvis der er fisk og plast
        else if (plastic > 0 && fish) {
            return "Du er " + description + ". Der er en død fisk. For at undersøge skriv >info<"
                    +"\n" +"Der er " +plastic+ " tons plastik i vandet. >indsaml< "+"\n" + getExitString();}
//        Hvis der ikke er fisk men der er plastik
        else if (plastic > 0 && !fish) {
            return "Du er " + description + ". Der er " +plastic+ " tons plastik i vandet. >indsaml< "
                    +"\n" + getExitString();}

        return "fejl i indlæsning af område";
    }


    public boolean isHarbor() {
        return false;
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

    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    public boolean checkRoom(){
    if(getShortDescription()=="nu i havnen") {
            return true;
        } else {
            return false;
        }
    }
}

