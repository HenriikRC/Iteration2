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
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();

        Image bg = new Image("file:src/main/resources/MapFiles/havn.png");
        ImageView mv = new ImageView(bg);
        Group g = new Group();
        g.getChildren().addAll(mv);
        g.getChildren().addAll(root);
        Scene sc = new Scene(g);
        stage.setScene(sc);

        stage.setResizable(false);
        stage.setTitle("Skipper Skrald");
        stage.setScene(sc);
        stage.show();

        sc.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case UP:
                        controller.up();
                        break;
                    case DOWN:
                        controller.down();
                        break;
                    case RIGHT:
                        controller.right();
                        break;
                    case LEFT:
                        controller.left();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}