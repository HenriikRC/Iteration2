package com.example.semesterproject;


import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import worldOfZuul.*;

import java.util.Random;


public class Controller {

    @FXML
    private Label dateLabel, scoreLabel;
    @FXML
    private ImageView background, ship, arrowUp, arrowDown,arrowRight,arrowLeft, minimap, viewPlastic, viewFish, infoBox;
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
            Group group = new Group();
            group.getChildren().addAll(background,ship);
            if (game.getCurrentRoom().spawnPlastic() && !game.isHarbor()) {
                trashShow(group);
            }
            if (game.getCurrentRoom().spawnDeadFish() && !game.isHarbor()) {
                deadFishShow(group);
            }
            group.getChildren().addAll(dateLabel, scoreLabel, arrowUp, arrowDown, arrowRight, arrowLeft,minimap);
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
        int rngX = rng.nextInt(0,((game.getCurrentRoom().getMaxXValue()*2)-64));
        int rngY = rng.nextInt(0,((game.getCurrentRoom().getMaxYValue()*2)-64));
        viewPlastic.setLayoutX(rngX);
        viewPlastic.setLayoutX(rngY);
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
                if(game.getScore() == 100_000){
                    quit();
                }
            }


        } else if (game.isIt2050()){
            game.dispose();
            System.out.println(game.getScore());
        }
        updateScoreLabel();
    }

    public void removePlasticUI(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        group.getChildren().addAll(background,ship);
        if(viewFish!=null && !game.getDeadFishInteracted()){
            group.getChildren().add(viewFish);
        }
        group.getChildren().addAll(dateLabel,scoreLabel,arrowUp,arrowDown,arrowRight,arrowLeft,minimap);
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
        Group group = new Group();
        group.getChildren().addAll(background,ship);
        if(viewPlastic!=null && !game.getIsCollected()){
            group.getChildren().add(viewPlastic);
        }
        group.getChildren().addAll(dateLabel,scoreLabel,arrowUp,arrowDown,arrowRight,arrowLeft,minimap);
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

    public boolean checkFishPlacement(){
        return ship.getBoundsInParent().intersects(viewFish.getBoundsInParent());
    }

    public boolean checkPlasticPlacement() {
        return ship.getBoundsInParent().intersects(viewPlastic.getBoundsInParent());
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

    }



}