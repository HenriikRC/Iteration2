package worldOfZuul;

import java.util.ArrayList;

public class Ship {

                                    /* Attributes */
    private ArrayList<Spawnable> inventory;
    private int capacityMax;
    private int capacity;


                                    /* Accessor Methods */

    public int getCapacity(){
        return this.capacity;
    }
    public int getCapacityMax(){
        return this.capacityMax;
    }



                                    /* Mutator Methods */
    public void setCapacityMax(int capacityMax){
        this.capacityMax = capacityMax;
    }


                                /* Methods and Functions */

    public Ship() {
        this.inventory = new ArrayList<Spawnable>();
        this.capacityMax = 6000;
        this.capacity = 0;
    }

    /** Method to reset the current capcity of the ship to 0 */
    public void resetCapacity() {
        this.capacity = 0;
    }

    /** Carries the logic to collect a given object of Plastics amount of plastic */
    public void collectPlastic(Plastic plastic) {
        if ((this.capacity + plastic.getAmount()) < this.capacityMax){
            this.capacity += plastic.getAmount();
            inventory.add(plastic);
            System.out.println( "Du har indsamlet " + plastic.getAmount() + " tons plast." +
                                "\n Skibet er nu lastet med " + capacity + "/"+ capacityMax + " tons");
        }
        else System.out.println("Du har ikke kapacitet nok, bortskaf dit plast i havnen");
    }

    /** Handles the disposal of plastic from the players inventory */
    public int disposePlastic() {
        int amountOfPlastic = 0;
        for (Spawnable item : inventory) {
            if (item instanceof Plastic) {
                amountOfPlastic = amountOfPlastic + ((Plastic)item).getAmount();
            }
        }
        inventory.clear();
        return amountOfPlastic;
    }
}
