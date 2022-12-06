package worldOfZuul;
import java.util.Random;

public class Plastic implements Spawnable{

    private int amount;
    private boolean spawnChanceBoolean;

                                /* Accessor Methods */

    public int getAmount() {
        return amount;
    }



                               /* Mutator Methods */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setSpawnChance(boolean bool){
        spawnChanceBoolean = bool;
    }



                                /* Methods and Functions */

    public Plastic (){
        this.amount = 0;
    }
    /** Calculates the spawnChance of a Plastic object in a room */
    @Override
    public boolean spawnChance(){
        Random random = new Random();
        int chance = random.nextInt(101);
        if(chance<65){
            spawnChanceBoolean = true;
            return true;
        }
        else {
            spawnChanceBoolean = false;
            return false;
        }
    }

    /** Creates a random amount of plastic for the Plastic object */
    @Override
    public void spawn() {
        int min = 100;
        int max = 1400;
        int amount = (int) (Math.random() * ((max - min) + 1)) + min;

        setAmount(amount);
    }
}

