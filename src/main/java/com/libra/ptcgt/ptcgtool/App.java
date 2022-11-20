package com.libra.ptcgt.ptcgtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Khalil "AmLibra" M'hirsi
 * <p>
 * The entry point of the application, simply loading the fxml sheet and starting the JavaFx Scene
 */
public class App extends Application {

    // Screen Sizes Supported
    final static int[][] SCREEN_SIZES = {
            {800, 600},
    };
    private final static int CURRENT_SCREEN_SIZE = 0;
    private final static int WIDTH = 0;
    private final static int HEIGHT = 1;

    private final static String APP_NAME = "PTCGTool";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("appLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), SCREEN_SIZES[CURRENT_SCREEN_SIZE][WIDTH], SCREEN_SIZES[CURRENT_SCREEN_SIZE][HEIGHT]);
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}