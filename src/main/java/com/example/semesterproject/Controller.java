package com.example.semesterproject;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import worldOfZuul.*;
import java.util.*;


public class Controller {

    @FXML
    private Label dateLabel, scoreLabel, infoLabel, upgradeAvailable;
    @FXML
    private ImageView background, ship, minimap, viewPlastic, viewFish, mapMarker, dialogBox;
    private Group group;
    private Stage stage;
    private int x = 0, y = 0, z;
    private Game game;
    public void setGame(Game game){
        this.game = game;
        dateLabel.setText(game.getGameDateMessage());
        z = game.getZ();
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void up() {
        if (y >= -290 && (game.getCurrentRoom()).getMinYValue()<y) {
            ship.setRotate(0);
            ship.setY(y-=z);
        }
        if (y <= -291){
            game.goRoom(game.getCommand("sejl", "nord"));
            move(this.x,290);
        }
    }
    public void down() {
        if (y <= 290 && (game.getCurrentRoom()).getMaxYValue()>y){
            ship.setRotate(180);
            ship.setY(y+=z);
        }
        if (y >= 291 && game.getCurrentRoom()!=game.getAboveHarbor()){
            ship.setRotate(180);
            game.goRoom(game.getCommand("sejl", "syd"));
            move(this.x,-290);
        } else if (y>291 && -100<x && x<124){
            ship.setRotate(180);
            game.goRoom(game.getCommand("sejl", "syd"));
            move(this.x,-290);
        }
    }
    public void left() {
        if (x >= -290 && (game.getCurrentRoom()).getMinXValue()<x){
            ship.setRotate(270);
            ship.setX(x-=z);
        }
        if (x <=-291){
            game.goRoom(game.getCommand("sejl","vest"));
            move(290,this.y);
        }
    }
    public void right() {
        if (x <= 290 && (game.getCurrentRoom()).getMaxXValue()>x){
            ship.setRotate(90);
            ship.setX(x+=z);
        }
        if (x >= 291){
            game.goRoom(game.getCommand("sejl","øst"));
            move(-290,this.y);
        }
    }


    public void handle(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP -> up();
            case DOWN -> down();
            case RIGHT -> right();
            case LEFT -> left();
            case ENTER -> collect();
            case SPACE -> interact();
            case I -> deadFishInfoRemove(infoLabel);
            default -> {
            }
        }
    }

    public void move(int x, int y){
        game.newMove();
        if(game.isIt2050()){
            quit();
        } else {
            viewFish = null;
            viewPlastic = null;
            background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
            group = new Group();
            group.getChildren().addAll(background,ship);
            if (game.getCurrentRoom().spawnPlastic() && !game.isHarbor()) {
                trashShow(group);
            }
            if (game.getCurrentRoom().spawnDeadFish() && !game.isHarbor()) {
                deadFishShow(group);
            }
            moveMapMarker();
            group.getChildren().addAll(dateLabel, scoreLabel, minimap, mapMarker,dialogBox);
            ship.setY(y);
            this.y = y;
            ship.setX(x);
            this.x = x;
            Scene scene = new Scene(group);
            scene.setOnKeyPressed(this::handle);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            dateLabel.setText(game.getGameDateMessage());
            if (game.isHarbor()){
                group.getChildren().add(upgradeAvailable());
            }
        }
    }

    private void trashShow(Group group) {
        Image plastic;
        if(game.getCurrentRoom().getCurrentPlastic().getAmount()<400){
            plastic = new Image("file:src/main/resources/Sprites/skraldS.png");
        }
        else if (game.getCurrentRoom().getCurrentPlastic().getAmount()<900){
            plastic = new Image("file:src/main/resources/Sprites/skraldM.png");
        } else {
            plastic = new Image("file:src/main/resources/Sprites/skraldL.png");
        }
        viewPlastic = new ImageView(plastic);
        Random rng = new Random();
        int rngX = rng.nextInt(48,(game.getCurrentRoom().getMaxXValue()*2)-200);
        int rngY = rng.nextInt(48,(game.getCurrentRoom().getMaxXValue()*2)-120);
        viewPlastic.setLayoutX(rngX);
        viewPlastic.setLayoutY(rngY);
        group.getChildren().add(viewPlastic);
    }

    private void deadFishShow(Group group) {
        Image fish = new Image("file:src/main/resources/Sprites/fish.png");
        viewFish = new ImageView(fish);
        viewFish.resize(64,64);
        Random rng = new Random();
        int rngX = rng.nextInt(48,((game.getCurrentRoom().getMaxXValue()*2)-64));
        int rngY = rng.nextInt(48,((game.getCurrentRoom().getMaxYValue()*2)-64));
        viewFish.setX(rngX);
        viewFish.setY(rngY);
        group.getChildren().add(viewFish);
    }

    public void collect() {
        if(!game.isHarbor() && viewPlastic!=null && checkPlasticPlacement()){
            if(game.collect()){
                removePlasticUI();
                viewPlastic = null;
            } else {
                fullCapacityMessage();
            }
        } else if (game.isHarbor()){
            disposeDone(infoLabel);
            game.dispose();
            group.getChildren().add(upgradeAvailable());
            if(game.getScore() >= 100_000){
                quit();
            } else {
                System.out.println(game.getScore());
            }
        }
        updateScoreLabel();
    }

    private void fullCapacityMessage() {
        infoLabel= new Label();
        infoLabel.setText("Du har ikke mere kapacitet.\nBortskaf plast i havnen.");
        infoLabel.setLayoutY(662);
        infoLabel.setLayoutX(423);
        infoLabel.setWrapText(true);
        infoLabel.setMaxWidth(330);

        infoLabel.setFont(new Font("System Bold", 15));
        group.getChildren().add(infoLabel);
    }

    public void removePlasticUI(){
        if(viewPlastic!=null) {
            group.getChildren().remove(viewPlastic);
        }
    }

    public void removeDeadFishUI(){
        if(viewFish!=null) {
            group.getChildren().remove(viewFish);
            deadFishInfoBoxShow();
        }
    }

    public void updateScoreLabel(){
        scoreLabel.setText(game.getShipCapacity() + " / " + game.getShipCapacityMax() + " tons");
    }

    public void interactWithDeadFish(){
        if(!game.getDeadFishInteracted() && viewFish!=null && checkFishPlacement()){
            game.getDeathReason();
            removeDeadFishUI();
            viewFish = null;
            game.incrementAmountOfInteractions();
        } else if(game.getDeadFishInteracted() && viewFish==null){
            deadFishInfoRemove(infoLabel);
        }
    }
    public void deadFishInfoBoxShow(){
        infoLabel = new Label();
        infoLabel.setText(game.getCurrentRoom().getDeadFishDeath().getDeathReason());
        infoLabel = formatLabel(infoLabel,662);
        group.getChildren().add(infoLabel);

    }
    public void deadFishInfoRemove(Label label){
        group.getChildren().removeAll(label);
    }


    public boolean checkFishPlacement(){
        return ship.getBoundsInParent().intersects(viewFish.getBoundsInParent());
    }

    public boolean checkPlasticPlacement() {
        return ship.getBoundsInParent().intersects(viewPlastic.getBoundsInParent());
    }

    public void disposeDone(Label label){
        if(game.getShipCapacity()>0) {
            group.getChildren().remove(upgradeAvailable);
            group.getChildren().remove(label);
            infoLabel = new Label();
            infoLabel.setText("Du har genbrugt " + game.getShipCapacity() + " tons plastik. Din score i alt er nu: "
                    + (game.getScore() + game.getShipCapacity()));
            infoLabel = formatLabel(infoLabel, 662);
            group.getChildren().add(infoLabel);
            animateDispose();

        }
    }

    private void animateDispose() {
        ImageView imageView = new ImageView(new Image("file:src/main/resources/Sprites/skraldL.png"));
        imageView.setLayoutY(ship.getLayoutY());
        imageView.setLayoutX(ship.getLayoutX());
        group.getChildren().add(imageView);

        /* Animation of the ImageView */
        Path path = new Path();
        path.getElements().add(new MoveTo(x ,y));
        ArcTo arc = new ArcTo();
        arc.setX(107-ship.getLayoutX());
        arc.setY(245-ship.getLayoutY());
        arc.setRadiusX(50);
        arc.setRadiusY(50);
        path.getElements().add(arc);
        PathTransition pt = new PathTransition();
        pt.setDuration(Duration.millis(1750));
        pt.setNode(imageView);
        pt.setCycleCount(1);
        pt.setPath(path);

        ScaleTransition st = new ScaleTransition(Duration.millis(1750), imageView);
        st.setCycleCount(1);
        st.setToX(0.5);
        st.setToY(0.5);

        FadeTransition fade = new FadeTransition(Duration.millis(2500));
        fade.setCycleCount(1);
        fade.setToValue(0);
        fade.setNode(imageView);

        /* Starts the animation */
        pt.play();
        st.play();
        fade.play();
    }

    public Label formatLabel(Label label, int y){
        label.setLayoutY(y);
        label.setLayoutX(423);
        label.setWrapText(true);
        label.setMaxWidth(330);
        label.setFont(new Font("System Bold", 15));
        return label;
    }

    public Label upgradeAvailable() {
        upgradeAvailable = new Label();
        upgradeAvailable.setText("Du kan opgradere dit skib! Tryk >mellemrum< for at opgrade");
        formatLabel(upgradeAvailable,698);

        if(game.isHarbor() && game.getAmountOfInteractions()>=22 && game.getShipCapacityMax() < 14_000){
            return upgradeAvailable;
        } else if (game.isHarbor() && game.getAmountOfInteractions()>=14 && game.getShipCapacityMax() < 12_000){
            return upgradeAvailable;
        } else if (game.isHarbor() && game.getAmountOfInteractions()>=8 && game.getShipCapacityMax() < 10_000){
            return upgradeAvailable;
        } else if (game.isHarbor() && game.getAmountOfInteractions()>=3 && game.getShipCapacityMax() < 8_000){
            return upgradeAvailable;
        } else return new Label("");
    }
    public void upgradeDone(Label label){
        group.getChildren().remove(label);
        infoLabel = new Label();
        infoLabel.setText("Du har gjordt et fantastisk arbejde!" +"\n"+"FN har sponsorert en opgradering til dit skib."
                + "\n"+ "Du kan nu laste dit skib med " +game.getShipCapacityMax()+ " tons."
                + "\nDesuden er din motor blevet tunet!");
        infoLabel = formatLabel(infoLabel,662);

        group.getChildren().remove(upgradeAvailable);
        group.getChildren().add(infoLabel);
        upgradeAvailable = null;
    }

    public void interact(){
        if(!game.isHarbor()){
            interactWithDeadFish();
        } else {
            game.upgradeShip();
            z = game.getZ();
            updateScoreLabel();
            upgradeDone(infoLabel);
        }
    }
    /** Moves the mapMarker to the coordinates associated with the current room. */
    public void moveMapMarker(){
        mapMarker.setLayoutX(game.getCurrentRoom().getMapMarkerX());
        mapMarker.setLayoutY(game.getCurrentRoom().getMapMarkerY());
    }

    /** Creating either a losing screen or a victory screen **/
    public void quit(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        /* Generating ImageView from the Image of Skipper Skrald */
        ImageView skipperSkrald = new ImageView(new Image("file:src/main/resources/Sprites/Skipper Skrald1.png"));

        /* Sets the position and size of Skipper Skrald */
        skipperSkrald.setX(318);
        skipperSkrald.setY(250);
        skipperSkrald.resize(200,200);

        /* Sets the position of the ship to the center */
        ship.setX(0);
        ship.setY(0);

        /* Adds background of the lost-game screen (Current room is the background */
        group.getChildren().addAll(background);
        if(game.isIt2050()){
            /* Generating ImageView[] of the different plastic icons and the dead fish */
            ImageView[] smallPlastic = generateImageView("skraldS",100,120,200);
            ImageView[] mediumPlastic = generateImageView("skraldM",10,120,200);
            ImageView[] largePlastic = generateImageView("skraldL",10,120,200);
            ImageView[] deadFish = generateImageView("fish",30,64,200);

            /* Merging the generated ImageView[] into one */
            ImageView[] mergedImageView = uniteImageViews(smallPlastic,mediumPlastic,largePlastic,deadFish);

            /* Adds all the ImageView objects to the group */
            for(ImageView imageView:mergedImageView){
                group.getChildren().add(imageView);
            }

            /* Text labels for end screen (lost) */
            Text text1 = new Text(19,23,"Du nåede desværre ikke at indsamle 100.000 tons plastik inden år 2050");
            Text text2 = new Text(174,55,"Havene er derfor stadig fyldt med plast");
            Text text3 = new Text(227,87,"Og alle fisk i vandet er døde");
            Text text4 = new Text(187,637,"Du nåede at indsamle " + game.getScore() + " tons plast");
            Text[] endTexts = {text1, text2, text3, text4};

            /* Styling for text and adding them to the group */
            for(Text text:endTexts){
                text.setFill(Color.web("#ffffff"));
                text.setStrokeWidth(1);
                text.setStroke(Color.web("#000000"));
                text.setFont(new Font("System Bold", 22));
                group.getChildren().add(text);
            }
        } else if (game.getScore() >= 100_000) {
            ImageView[] happyFish = generateImageView("happyFish",100,64,64);
            for(ImageView imageView:happyFish){
                group.getChildren().add(imageView);
            }
            Text text1 = new Text(80,680,"Du nåede at indsamle 100.000 tons plastik inden år 2050!" +
                                                 "\n Allerede i " + game.getGameDateMessage() + " havde du indsamlet " + game.getScore() + " tons!");
            text1.setFill(Color.web("#ffffff"));
            text1.setStrokeWidth(1);
            text1.setStroke(Color.web("#000000"));
            text1.setFont(new Font("System Bold", 22));
            group.getChildren().add(text1);
        }
        /* Adds ship and character Skipper Skrald to the group */
        group.getChildren().addAll(ship,skipperSkrald);

        /* New scene of the group */
        Scene scene = new Scene(group);
        /* Set the scene of the stage */
        stage.setScene(scene);
        /* Shows the stage */
        stage.show();
    }
    /** Animates the ImageView to fall down from above the window into the scene */
    private void generateAnimation(ImageView imageView, int x, int y) {
        /* Places the ImageView above the window */
        imageView.setLayoutY(-200);
        imageView.setLayoutX(x-100);

        /* Animation of the ImageView */
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(imageView);
        translateTransition.setDuration(Duration.millis(5000));
        translateTransition.setCycleCount(1);
        translateTransition.setByY(y+150);

        /* Starts the animation */
        translateTransition.play();
    }

    /** Generates an ImageView[] with ImageView objects containing the image from sourceDirectory.
     *  The amount of objects is set by amount and the image height and width is passed.          */
    private ImageView[] generateImageView(String sourceDirectory, int amount, int imageHeight, int imageWidth){
        /* ImageView[] with length of amount*/
        ImageView[] imageViews = new ImageView[amount];

        for (int i = 0; i < imageViews.length; i++){
            Random rng = new Random();
            if(!Objects.equals(sourceDirectory, "happyFish") && !game.isHarbor()) {
                /* Random number generation for x and y coordinates.
                 *  Number is between min/max value and Image size   */
                int rngX = rng.nextInt(game.getCurrentRoom().getMinXValue() + (imageWidth * 2)
                        , game.getCurrentRoom().getMaxXValue() * 2 - (imageWidth - (imageWidth / 2)));
                int rngY = rng.nextInt(game.getCurrentRoom().getMinYValue() + (imageHeight)
                        , game.getCurrentRoom().getMaxYValue() * 2 - (imageHeight - (imageHeight / 2)));
                /* ImageView object created with the given sourceDirectory as Image */
                imageViews[i] = new ImageView(new Image("file:src/main/resources/Sprites/" + sourceDirectory + ".png"));
                /* Method to animate ImageViews */
                generateAnimation(imageViews[i], rngX, rngY);
            } else {
                int rngX;
                if(game.isIt2050()){
                    rngX = rng.nextInt(318,538);
                } else {
                    rngX = rng.nextInt(318,604);
                }
                int rngY = rng.nextInt(0,534);
                imageViews[i] = new ImageView(new Image("file:src/main/resources/Sprites/" + sourceDirectory + ".png"));
                generateAnimation(imageViews[i],rngX,rngY);
            }
            } return imageViews;
    }
    /** Method to merge 4 ImageView[] into one ImageView[]
     *  Takes 4 ImageView[] as parameter.                   **/
    private ImageView[] uniteImageViews(ImageView[] smallPlastic,
                                        ImageView[] mediumPlastic,
                                        ImageView[] largePlastic,
                                        ImageView[] deadFish) {
        /* The sum of all array lengths */
        int sumOfArrayLengths =   smallPlastic.length
                                + mediumPlastic.length
                                + largePlastic.length
                                + deadFish.length;
        /* New array with length of all arrays combined */
        ImageView[] collectedImageViews = new ImageView[sumOfArrayLengths];

        /* Copies the input arrays into collectedImageViews */
        System.arraycopy(smallPlastic,0,collectedImageViews,0,
                         smallPlastic.length);
        System.arraycopy(mediumPlastic,0,collectedImageViews,
                         smallPlastic.length,mediumPlastic.length);
        System.arraycopy(largePlastic,0,collectedImageViews,
                  smallPlastic.length+mediumPlastic.length,largePlastic.length);
        System.arraycopy(deadFish,0,collectedImageViews,
                  smallPlastic.length+mediumPlastic.length+largePlastic.length,deadFish.length);

        return collectedImageViews;
    }
}