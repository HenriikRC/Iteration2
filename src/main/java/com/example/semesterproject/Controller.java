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
    private Label dateLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private ImageView background;
    @FXML
    private ImageView ship;
    private ImageView viewPlastic;
    private int x = 0;
    private int y = 0;
    private Game game;

    public void setGame(Game game){
        this.game = game;
        dateLabel.setText(game.getGameDate());
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
            case SPACE -> interactWithDeadFish();
            default -> {
            }
        }
    }

    public void move(int x, int y){
        game.newMove();
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        group.getChildren().addAll(background,ship,dateLabel,scoreLabel);
        if (game.getCurrentRoom().spawnPlastic() && !game.isHarbor())  {
            trashShow(group);
        }
        if(game.getCurrentRoom().spawnDeadFish() && !game.isHarbor()){
            deadFishShow(group);
        }
        ship.setY(y);
        this.y = y;
        ship.setX(x);
        this.x = x;
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this::handle);
        (HelloApplication.getStage()).setScene(scene);
        (HelloApplication.getStage()).setResizable(false);
        (HelloApplication.getStage()).show();
        dateLabel.setText(game.getGameDate());
        System.out.println(game.getRoomDescription());
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
        int rngX = rng.nextInt(0, 540);
        int rngY = rng.nextInt(0, 540);
        viewPlastic.setLayoutX(rngX);
        viewPlastic.setLayoutX(rngY);

        group.getChildren().add(viewPlastic);
    }

    private void deadFishShow(Group group) {
        Image fish = new Image("file:src/main/resources/Sprites/fish.png");
        ImageView viewFish = new ImageView(fish);
        Random rng = new Random();
        int rngX = rng.nextInt(200,201);
        int rngY = rng.nextInt(200,201);
        viewFish.setX(rngX);
        viewFish.setY(rngY);
        group.getChildren().add(viewFish);
    }

    public void collect() {
        if(!game.isHarbor()){
            game.collect();
            removePlasticUI();

        } else{
            game.dispose();
            System.out.println(game.getScore());
        }
        updateScoreLabel();
    }

    public void removePlasticUI(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        group.getChildren().addAll(background,ship,dateLabel,scoreLabel);
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this::handle);
        (HelloApplication.getStage()).setScene(scene);
        (HelloApplication.getStage()).setResizable(false);
        (HelloApplication.getStage()).show();
        dateLabel.setText(game.getGameDate());
        viewPlastic = null;
    }

    public void removeDeadFishUI(){
        background = new ImageView(new Image(game.getCurrentRoomMapDirectory()));
        Group group = new Group();
        group.getChildren().addAll(background,ship,dateLabel,scoreLabel);
        if(viewPlastic!=null){
            group.getChildren().add(viewPlastic);
        }
        Scene scene = new Scene(group);
        scene.setOnKeyPressed(this::handle);
        (HelloApplication.getStage()).setScene(scene);
        (HelloApplication.getStage()).setResizable(false);
        (HelloApplication.getStage()).show();
        dateLabel.setText(game.getGameDate());
    }

    public void updateScoreLabel(){
        scoreLabel.setText(game.getShipCapacity() + " / " + game.getShipCapacityMax() + " tons");
    }

    public void interactWithDeadFish(){
        if(!game.getDeadFishInteracted()){
            game.getDeathReason();
            removeDeadFishUI();
        }
    }




}