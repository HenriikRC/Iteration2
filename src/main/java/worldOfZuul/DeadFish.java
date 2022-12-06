package worldOfZuul;
import java.util.ArrayList;
import java.util.Random;

public class DeadFish implements Spawnable {

                                        /* Attributes */
    private String deathReason;
    private ArrayList<String> reasons = new ArrayList<>(); {
        reasons.add("Estimeret er der 300k hvaler, delfiner og marsvin der dør årligt grundet forladte fiskenet" +
                "\nfauna-flora.org");
        reasons.add("I 2019 blev en strandet hval fundet med 40kg. plastik i maven " +
                "(Hovedsagligt bestående af engangsplastikposer)\nfauna-flora.org");
        reasons.add("Havskildpadder indtager ofte fejl af plastikposer og fiskenet da det ligner deres vandte kost" +
                "\nfauna-flora.org");
        reasons.add("Når havskildpadder spiser plastik får det dem til at føle sig mæt som " +
                "resultere i sult\nfauna-flora.org");
        reasons.add("Ifølge en samling af 100 undersøgelser indeholdende 500 arter af fisk har 2/3 " +
                "indtaget mikroplastik\nfauna-flora.org");
        reasons.add("100% af muslinger fanget i United Kingdoms hav indeholder mikroplastik\nfauna-flora.org");
        reasons.add("100 millioner havdyr dør årligt grundet plastik\ncondorferries.co.uk");
        reasons.add("I de sidste 10 år har vi fremstillet mere plastik end det sidste århundrede! " +
                    "I år 2050 vil der estimeret være mere plastik end fisk i havet\ncondorferries.co.uk");
        reasons.add("Der er estimeret til at være 5,25 trillioner stykker plastik i havet\ncondorferries.co.uk");
        reasons.add("70% af plastik synker ned i havets økosystem, 15% flyder på havoverfladen og 15% lander på " +
                "vores strande igen\ncondorferries.co.uk");
        reasons.add("Plastik tager mellem 500-1000 år for at nedbryde. Vi sender 79% af alt plastikaffald til " +
                "losseplader i havet eller på land\ncondorferries.co.uk");
        reasons.add("Plastforurening kan også have økonomiske konsekvenser, da det kan beskadige fiskegarn og udstyr, " +
                "og det kan gøre visse områder af havet farligt for fiskeri.");
        reasons.add("Udover den plast, der direkte smides i havet, havner en stor mængde plast i havet gennem floder " +
                "og andre vandveje. Det betyder, at problemet med plastforurening ikke kun er begrænset til " +
                "kystområder, men kan påvirke indlandet også.");
        reasons.add("Nedbrydningen af plast i havet kan frigive giftige kemikalier, som kan være skadelige for " +
                "havets dyreliv og endda for mennesker, der spiser fisk og skaldyr.");
        reasons.add("500 marine områder er nu registreret som døde zoner globalt, i øjeblikket på størrelsen med " +
                "Storbritanniens overflade (245.000 km²)");
        reasons.add("Det største skraldepunkt på planeten er Great Pacific Garbage Patch, der er dobbelt " +
                "så stor som Texas, og der er 6 gange flere affaldsgenstande end der er liv i havet der.");
        reasons.add("Med hensyn til plastik smides der årligt 8,3 millioner tons i havet. Heraf er 236.000 " +
                "indtagelig mikroplast, som havdyr forveksler med føde.");

    }


                                            /* Accessor Methods */
    public String getDeathReason() {
        return deathReason;
    }


                                            /* Mutator Methods */
    public void setDeathReason(String deathReason) {
        this.deathReason = deathReason;
    }


                                            /* Methods and Functions */
    public DeadFish() {
        this.deathReason = "";
    }

    /** Calculates the spawnChance of a DeadFish object in a room */
    @Override
    public boolean spawnChance(){
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
