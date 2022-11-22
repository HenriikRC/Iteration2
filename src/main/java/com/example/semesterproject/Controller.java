package com.example.semesterproject;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;


public class Controller {

    @FXML
    private Circle myCircle;
    private double x;
    private double y;

    public void up() {
        //System.out.println("UP");
        if (y >= -320) {
            myCircle.setCenterY(y-=10);
        }
    }
    public void down() {
        //System.out.println("DOWN");
        if (y <= 320){
            myCircle.setCenterY(y+=10);
        }
    }
    public void left() {
        //System.out.println("LEFT");
        if (x >= -320){
            myCircle.setCenterX(x-=10);
        }
    }
    public void right() {
        //System.out.println("RIGHT");
        if (x <= 320){
            myCircle.setCenterX(x+=10);
        }
    }

}