package com.example.semesterproject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;
import worldOfZuul.*;

public class HelloApplication extends Application {
    private static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        this.stage = stage;
        controller.setGame(new Game());

        Image bg = new Image("file:src/main/resources/MapFiles/havn.png");
        ImageView mv = new ImageView(bg);
        mv.setId("background");
        //Image shipImage = new Image("file:src/main/resources/Misc/skib.png");
        //ImageView ship = new ImageView(shipImage);
        //ship.setY(284);
        //ship.setX(320);
        //controller.setImageView(ship);
        controller.updateScoreLabel();
        Group group = new Group();
        group.getChildren().add(mv);
        group.getChildren().add(root);
       //g.getChildren().add(shipViewer);
        Scene sc = new Scene(group);
        stage.setScene(sc);

        stage.setResizable(false);
        stage.setTitle("Skipper Skrald");
        stage.setScene(sc);
        stage.show();

        sc.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case UP -> controller.up();
                    case DOWN -> controller.down();
                    case RIGHT -> controller.right();
                    case LEFT -> controller.left();
                    case ENTER -> controller.collect();
                    default -> {
                    }
                }
            }
        });
    }

    public static Stage getStage() {
        return stage;
    }
    public static void main(String[] args) {
        launch();
    }
}