package worldOfZuul;

import java.util.ArrayList;

public class Ship {


    private ArrayList<Spawnable> inventory;
    private int capacityMax;
    private int capacity;


                        /* Constructor, Accesor and Mutator Methods */
    public Ship() {
        //Creates a list
        this.inventory = new ArrayList<Spawnable>();
        //Sets the maximum capacity of the ship.
        this.capacityMax = 6000;
        //Sets the current capacity of the ship.
        this.capacity = 0;
    }
    public int getCapacity(){
        return this.capacity;
    }


                                /* Methods and Functions */
    public ArrayList<Spawnable> getInventory() {
        return inventory;
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
        for (Spawnable item : inventory) {  // Iterates through a list of Spawnable
            if (item instanceof Plastic) {  // and for every instance of plastic items, it sets the
                                            // amountOfPlastic
                amountOfPlastic = amountOfPlastic + ((Plastic)item).getAmount();
            }
        }
        inventory.clear();
        return amountOfPlastic;
    }
}
