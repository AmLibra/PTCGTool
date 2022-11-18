package com.libra.ptcgt.ptcgtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // Screen Sizes Supported
    final static int[][] SCREEN_SIZES = {
            {800, 600},
    };

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("layout.fxml"));
        int size = 0;
        Scene scene = new Scene(fxmlLoader.load(), SCREEN_SIZES[size][0], SCREEN_SIZES[size][1]);
        stage.setTitle("PTCGTool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}