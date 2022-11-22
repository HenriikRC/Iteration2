package worldOfZuul;
import java.util.Random;

public class Plastic implements Spawnable{

    private int amount;

                        /* Constructor, Accesor and Mutator Methods */
    //Constructor for Plastic objects
    public Plastic (){
        this.amount = 0;
    }
    //Mutator method for Plastic object
    public void setAmount(int amount) {
        this.amount = amount;
    }
    //Accesor method for Plastic object
    public int getAmount() {
        return amount;
    }


                                /* Methods and Functions */
    /** Calculates the spawnChance of a Plastic object in a room */
    @Override
    public boolean spawnChance(){
        Random random = new Random();
        int chance = random.nextInt(101);
        if(chance<65){
            return true;
        }
        else return false;
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

