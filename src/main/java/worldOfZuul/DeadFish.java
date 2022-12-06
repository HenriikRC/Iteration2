package worldOfZuul;
import java.util.ArrayList;
import java.util.Random;

public class DeadFish implements Spawnable {
    private String deathReason;
    private ArrayList<String> reasons = new ArrayList<>(); {    //Creates an arraylist for plastic information
        reasons.add("Estimeret er der 300k hvaler, delfiner og marsvin der dør årligt grundet forladte fiskenet\nfauna-flora.org");
        reasons.add("I 2019 blev en strandet hval fundet med 40kg. plastik i maven (Hovedsagligt bestående af engangsplastikposer)\nfauna-flora.org");
        reasons.add("Havskildpadder tager ofte fejl af plastikposer og fiskenet da det ligner vandmænd og tang som er deres mest normale kost\nfauna-flora.org");
        reasons.add("Når havskildpadder spiser plastik får det dem til at føle sig mæt som resultere i sult\nfauna-flora.org");
        reasons.add("Ifølge en samling af 100 undersøgelser indeholdende 500 arter af fisk har 2/3 indtaget mikroplastik\nfauna-flora.org");
        reasons.add("100% af muslinger fanget i United Kingdoms hav indeholder mikroplastik\nfauna-flora.org");
        reasons.add("100 millioner havdyr dør årligt grundet plastik\ncondorferries.co.uk");
        reasons.add("I de sidste 10 år har vi fremstillet mere plastik end det sidste århundrede! " +
                    "I år 2050 vil der estimeret være mere plastik end fisk i havet\ncondorferries.co.uk");
        reasons.add("Der er estimeret til at være 5,25 trillioner stykker plastik i havet\ncondorferries.co.uk");
        reasons.add("70% af plastik synker ned i havets økosystem, 15% flyder på havoverfladen og 15% lander på vores strande igen\ncondorferries.co.uk");
        reasons.add("Plastik tager mellem 500-1000 år for at nedbryde. Vi sender 79% af alt plastikaffald til losseplader i havet eller på land\ncondorferries.co.uk");
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
        return chance < 50;
    }

    /** Handles the random picking of a deathreason for the DeadFish object */
    @Override
    public void spawn() {
        Random rng = new Random();
        int random = rng.nextInt(reasons.size());
        setDeathReason(reasons.get(random));
    }
    @Override
    public String toString(){
        return "" +getDeathReason();
    }
}
