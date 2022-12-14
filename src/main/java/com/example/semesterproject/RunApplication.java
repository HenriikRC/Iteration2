package com.example.semesterproject;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import worldOfZuul.*;

public class RunApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Skipper-Skrald.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.setStage(stage);
        controller.setGame(new Game());

        ImageView mv = new ImageView(new Image("file:src/main/resources/MapFiles/havn.png"));

        Image start = new Image("file:src/main/resources/Misc/start.png");
        ImageView show = new ImageView(start);
        Label startLabel = new Label("",show);
        controller.updateScoreLabel();
        Group group = new Group();

        group.getChildren().addAll(mv,root,startLabel);

        EventHandler<MouseEvent> handleRemove = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                remove(group,startLabel);
            }
        };

        startLabel.setOnMouseClicked(handleRemove);

        Scene sc = new Scene(group);
        stage.setScene(sc);

        stage.setResizable(false);
        stage.setTitle("Skipper Skrald");
        //stage.setScene(sc);
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
    public void remove(Group group, Label label){
        group.getChildren().remove(label);
    }
    public static void main(String[] args) {
        launch();
    }
}