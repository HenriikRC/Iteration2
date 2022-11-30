package com.example.semesterproject;


import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import worldOfZuul.*;

import java.lang.reflect.Array;
import java.util.*;


public class Controller {

    @FXML
    private Label dateLabel, scoreLabel, infoLabel;
    @FXML
    private ImageView background, ship, minimap, viewPlastic, viewFish, infoBox,mapMarker;
    private Group group;
    private int x = 0, y = 0;
    private Game game;

    public void setGame(Game game){
        this.game = game;
        dateLabel.setText(game.getGameDateMessage());
    }

    public void up() {
        //System.out.println("UP");
        if (y >= -290 && (game.getCurrentRoom()).getMinYValue()<y) {
            ship.setRotate(0);
            ship.setY(y-=10);
        }
        if (y <= -291){
            game.goRoom(game.getCommand("sejl", "nord"));
            move(this.x,290);
        }
    }
    public void down() {
        //System.out.println("DOWN");
        if (y <= 290 && (game.getCurrentRoom()).getMaxYValue()>y){
            ship.setRotate(180);
            ship.setY(y+=10);
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
        //System.out.println("LEFT");
        if (x >= -290 && (game.getCurrentRoom()).getMinXValue()<x){
            ship.setRotate(270);
            ship.setX(x-=10);
        }
        if (x <=-291){
            game.goRoom(game.getCommand("sejl","vest"));
            move(290,this.y);
        }
    }
    public void right() {
        //System.out.println("RIGHT");
        if (x <= 290 && (game.getCurrentRoom()).getMaxXValue()>x){
            ship.setRotate(90);
            ship.setX(x+=10);
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
            moveMapMarker(game.getCurrentRoom());
            group.getChildren().addAll(dateLabel, scoreLabel, minimap,mapMarker);
            ship.setY(y);
            this.y = y;
            ship.setX(x);
            this.x = x;
            Scene scene = new Scene(group);
            scene.setOnKeyPressed(this::handle);
            (HelloApplication.getStage()).setScene(scene);
            (HelloApplication.getStage()).setResizable(false);
            (HelloApplication.getStage()).show();
            dateLabel.setText(game.getGameDateMessage());
            System.out.println(game.getRoomDescription());
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
            }
        } else if (game.isHarbor()){
            disposeDone();
            game.dispose();
            if(game.getScore() == 100_000){
                quit();
            } else {
                System.out.println(game.getScore());
            }
        }
        updateScoreLabel();
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
        } else if(game.getDeadFishInteracted() && viewFish==null){
            deadFishInfoRemove(infoLabel,infoBox);
        }
    }
    public void deadFishInfoBoxShow(){
        Image info = new Image("file:src/main/resources/Sprites/dialogbox.png", 768, 150, true, true);

        infoBox = new ImageView(info);

        infoBox.setX(0);
        infoBox.setY(0);
        infoLabel = new Label();
        infoLabel.setText(game.getCurrentRoom().getDeadFishDeath().getDeathReason());
        infoLabel.setAlignment(Pos.CENTER);
        infoLabel.setLayoutY(20);
        infoLabel.setLayoutX(160);

        infoLabel.setFont(new Font("System Bold", 22));
        group.getChildren().addAll(infoBox,infoLabel);
    }
    public void deadFishInfoRemove(Label label, ImageView im){
        group.getChildren().removeAll(label,im);
    }


    public boolean checkFishPlacement(){
        return ship.getBoundsInParent().intersects(viewFish.getBoundsInParent());
    }

    public boolean checkPlasticPlacement() {
        return ship.getBoundsInParent().intersects(viewPlastic.getBoundsInParent());
    }
    public void disposeDone(){
        Text disposed = new Text("Du har genbrugt " + game.getShipCapacity()+ " tons plastik. Din score i alt er nu: "
                +(game.getScore()+ game.getShipCapacity()));
        disposed.setFill(Color.web("#FFFFFF"));
        disposed.setStrokeWidth(1);
        disposed.setStroke(Color.web("000000"));
        disposed.setLayoutY(605);
        disposed.setLayoutX(80);
        disposed.setFont(new Font("System Bold", 22));
        group.getChildren().add(disposed);
    }
    public void upgradeDone(){
        Text upgraded = new Text(
                "Du har gjordt et fantastisk arbejde!" +"\n"+"FN har sponsorert en opgradering til dit skib."+ "\n"+
                        "Du kan nu laste dit skib med " +game.getShipCapacityMax()+ " tons");
        upgraded.setFill(Color.web("#FFFFFF"));
        upgraded.setStrokeWidth(1);
        upgraded.setStroke(Color.web("000000"));
        upgraded.setLayoutY(655);
        upgraded.setLayoutX(200);
        upgraded.setFont(new Font("System Bold", 22));
        group.getChildren().add(upgraded);
    }

    public void upgradeShip(){
        game.upgradeShip();
    }

    public void interact(){
        if(!game.isHarbor()){
            interactWithDeadFish();
        } else {
            upgradeShip();
            updateScoreLabel();
            upgradeDone();
        }
    }
    public void moveMapMarker(Room currentRoom){
        mapMarker.setLayoutX(game.getCurrentRoom().getMapMarkerX());
        mapMarker.setLayoutY(game.getCurrentRoom().getMapMarkerY());
    }

    public void quit(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        if(game.isIt2050()){
            ImageView skipperSkrald = new ImageView(new Image("file:src/main/resources/Sprites/Skipper Skrald1.png"));
            skipperSkrald.setX(318);
            skipperSkrald.setY(250);
            skipperSkrald.resize(200,200);
            ship.setX(0);
            ship.setY(0);
            /* Text labels for end screen (lost) */
            Text text1 = new Text(19,23,"Du nåede desværre ikke at indsamle 100.000 tons plastik inden år 2050");
            Text text2 = new Text(174,55,"Havene er derfor stadig fyldt med plast");
            Text text3 = new Text(227,87,"Og alle fisk i vandet er døde");
            Text text4 = new Text(187,637,"Du nåede at indsamle " + game.getScore() + " tons plast");
            Text[] endTexts = {text1, text2, text3, text4};

            group.getChildren().addAll(background);
            ImageView[] smallPlastic = generateImageView("skraldS",100,120,200);
            ImageView[] mediumPlastic = generateImageView("skraldM",10,120,200);
            ImageView[] largePlastic = generateImageView("skraldL",10,120,200);
            ImageView[] deadFish = generateImageView("fish",30,64,200);
            for(ImageView imageView:smallPlastic){
               group.getChildren().add(imageView);
            }
            for(ImageView imageView:mediumPlastic){
                group.getChildren().add(imageView);
            }
            for(ImageView imageView:largePlastic){
                group.getChildren().add(imageView);
            }
            for(ImageView imageView:deadFish){
                group.getChildren().add(imageView);
            }
            for(Text text:endTexts){
                text.setFill(Color.web("#FFFFFF"));
                text.setStrokeWidth(1);
                text.setStroke(Color.web("000000"));
                text.setFont(new Font("System Bold", 22));
                group.getChildren().add(text);
            }
            group.getChildren().addAll(ship,skipperSkrald);
            Scene scene = new Scene(group);
            (HelloApplication.getStage()).setScene(scene);
            (HelloApplication.getStage()).show();

        } else if (game.getScore() >= 100_000) {

        }
    }
    private void generateAnimation(ImageView imageView, int x, int y) {
        imageView.setLayoutY(-200);
        imageView.setLayoutX(x-100);
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(imageView);
        translateTransition.setDuration(Duration.millis(5000));
        translateTransition.setCycleCount(1);
        translateTransition.setByY(y+150);
        translateTransition.play();
    }

    private ImageView[] generateImageView(String sourceDirectory, int amount, int imageHeight, int imageWidth){
        ImageView[] imageViews = new ImageView[amount];

        for (int i = 0; i < imageViews.length; i++){
            Random rng = new Random();
            int rngX = rng.nextInt(game.getCurrentRoom().getMinXValue()+(imageWidth*2)
                    ,game.getCurrentRoom().getMaxXValue()*2-(imageWidth-(imageWidth/2)));
            int rngY = rng.nextInt(game.getCurrentRoom().getMinYValue()+(imageHeight)
                    ,game.getCurrentRoom().getMaxYValue()*2-(imageHeight-(imageHeight/2)));
            imageViews[i] = new ImageView(new Image("file:src/main/resources/Sprites/"+sourceDirectory+".png"));
            generateAnimation(imageViews[i],rngX,rngY);

        } return imageViews;
    }
}