package worldOfZuul;

public class Harbor extends Room {

    private long score;

    public Harbor(String description) {
        super(description);
        this.score = 0;
    }

    public double getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score += score;
    }
    @Override
    public boolean isHarbor() {
        return true;
    }
}
