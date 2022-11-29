package com.example.semesterproject;


import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.util.Duration;
import worldOfZuul.*;

import java.util.Random;


public class Controller {

    @FXML
    private Label dateLabel, scoreLabel, infoLabel;
    @FXML
    private ImageView background, ship, minimap, viewPlastic, viewFish, infoBox;
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
            case I -> deadFishInfoRemove(infoLabel,infoBox);
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
            group.getChildren().addAll(dateLabel, scoreLabel, minimap);
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
        if(!game.isHarbor() && checkPlasticPlacement()){
            if(game.collect()){
                removePlasticUI();
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
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        group = new Group();
        group.getChildren().addAll(background,ship);
        if(viewFish!=null && !game.getDeadFishInteracted()){
            group.getChildren().add(viewFish);
        }
        group.getChildren().addAll(dateLabel,scoreLabel,minimap);
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this::handle);
        (HelloApplication.getStage()).setScene(scene);
        (HelloApplication.getStage()).setResizable(false);
        (HelloApplication.getStage()).show();
        dateLabel.setText(game.getGameDateMessage());
        viewPlastic = null;
    }

    public void removeDeadFishUI(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        group = new Group();
        group.getChildren().addAll(background,ship);
        if(viewPlastic!=null && !game.getIsCollected()){
            group.getChildren().add(viewPlastic);
        }
        deadFishInfoBoxShow();
        group.getChildren().addAll(dateLabel,scoreLabel,minimap);
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this::handle);
        (HelloApplication.getStage()).setScene(scene);
        (HelloApplication.getStage()).setResizable(false);
        (HelloApplication.getStage()).show();
        dateLabel.setText(game.getGameDateMessage());
    }

    public void updateScoreLabel(){
        scoreLabel.setText(game.getShipCapacity() + " / " + game.getShipCapacityMax() + " tons");
    }

    public void interactWithDeadFish(){
        if(!game.getDeadFishInteracted() && viewFish!=null && checkFishPlacement()){
            game.getDeathReason();

            removeDeadFishUI();
        }
    }
    public void deadFishInfoBoxShow(){
        Image info = new Image("file:src/main/resources/Sprites/dialogbox.png");
        infoBox = new ImageView(info);
        infoBox.setX(0);
        infoBox.setY(0);
        infoLabel = new Label();
        infoLabel.setText(game.getCurrentRoom().getDeadFishDeath().getDeathReason());
        infoLabel.setLayoutY(0);
        infoLabel.setLayoutX(0);

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
        Label disposed = new Label();
        disposed.setText(
                "Du har genbrugt " + game.getShipCapacity()+ " tons plastik. Din score i alt er nu: "
                        +(game.getScore()+ game.getShipCapacity()));
        disposed.setLayoutY(600);
        disposed.setLayoutX(100);

        disposed.setFont(new Font("System Bold", 22));
        group.getChildren().add(disposed);
    }
    public void upgradeDone(){

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
        }
    }

    public void quit(){
        background = new ImageView(new Image("file:src/main/resources/MapFiles/E3.png"));
        Group group = new Group();
        if(game.isIt2050()){
            ImageView skipperSkrald = new ImageView(new Image("file:src/main/resources/Sprites/Skipper Skrald1.png"));
            skipperSkrald.setX(318);
            skipperSkrald.setY(250);
            skipperSkrald.resize(200,200);
            ship.setX(0);
            ship.setY(0);
            ImageView trash1 = new ImageView(new Image("file:src/main/resources/Sprites/skraldL.png"));
            trash1.setX(100);
            trash1.setY(200);
            Path path1 = new Path();
            CubicCurveTo cubicCurveTo = new CubicCurveTo(100,-400,175,250,100,100);
            path1.getElements().add(new MoveTo(100,100));
            path1.getElements().add(cubicCurveTo);
            PathTransition pathTransition1 = new PathTransition();
            pathTransition1.setDuration(Duration.millis(4000));
            pathTransition1.setNode(trash1);
            pathTransition1.setPath(path1);
            pathTransition1.setCycleCount(1);
            pathTransition1.setAutoReverse(false);
            pathTransition1.play();
            Label text1 = new Label();
            text1.setText("Du nåede desværre ikke at indsamle 100.000 tons plastik inden år 2050");
            text1.setLayoutX(19);
            text1.setLayoutY(23);
            text1.setFont(new Font("System Bold",22));
            Label text2 = new Label();
            text2.setText("Havene er derfor stadig fyldt med plast");
            text2.setFont(new Font("System Bold",22));
            text2.setLayoutX(174);
            text2.setLayoutY(55);
            Label text3 = new Label();
            text3.setText("Og alle fisk i vandet er døde");
            text3.setLayoutX(227);
            text3.setLayoutY(87);
            text3.setFont(new Font("System Bold",22));
            group.getChildren().addAll(background,ship,skipperSkrald,text1,text2,text3,trash1);
            Scene scene = new Scene(group);
            (HelloApplication.getStage()).setScene(scene);
            (HelloApplication.getStage()).show();

            System.out.println("Hej");

        } else if (game.getScore() >= 100_000) {

        }
    }
}