package worldOfZuul;
import java.util.ArrayList;
import java.util.Random;

public class DeadFish implements Spawnable {
    private String deathReason;
    private ArrayList<String> reasons = new ArrayList<>(); {    //Creates an arraylist for the causes of death
        reasons.add("Fisken er viklet ind i en fiskesnor og er druknet");
        reasons.add("Fisken har fået plastik i spiserøret og er død af sult");
        reasons.add("Fisken er fanget i et uloveligt fiskenet");
        reasons.add("Fisken har viklet sig ind i en plasitkpose og er druknet");
        reasons.add("Fisken har troet plastik var mad og er død af sult");
        reasons.add("Fisken har noget om kroppen og er blevet for tung til at følge med sin stime");
    }


    public DeadFish() {                     //Constructor for the DeadFish class
        this.deathReason = "";
    }
    public String getDeathReason() {        //Accesor method for deathReason
        return deathReason;
    }
    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;     //Mutator method for deathReason
    }


                                            /* Methods and Funtions */
    /** Calculates the spawnChance of a DeadFish object in a room */
    @Override
    public boolean spawnChance(){       //Calculates the spawnchance of a DeadFish object in a room
        Random random = new Random();
        int chance = random.nextInt(101);
        chance = chance +1;
        if(chance<50){
            return true;
        }
        else return false;
    }

    /** Handles the random picking of a deathreason for the DeadFish object */
    @Override
    public void spawn() {
        Random rng = new Random();
        int random = rng.nextInt(6);
        setDeathReason(reasons.get(random));
    }
    @Override
    public String toString(){
        return "" +getDeathReason();
    }
}
