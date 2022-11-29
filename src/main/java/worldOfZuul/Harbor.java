package worldOfZuul;

public class Harbor extends Room {

    private long score;

    public Harbor(String description,String whereToSailNext,String mapDirectory,int minXValue,int maxXValue, int minYValue, int maxYValue,int mapMarkerX, int mapMarkerY) {
        super(description,whereToSailNext,mapDirectory,minXValue,maxXValue,minYValue,maxYValue,mapMarkerX,mapMarkerY);
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
