package com.libra.ptcgt.ptcgtool;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Khalil "AmLibra" M'hirsi
 * For fxml loading for each tab:
 * <a href="https://stackoverflow.com/questions/39164050/javafx-8-tabpanes-and-tabs-with-separate-fxml-and-controllers-for-each-tab">...</a>
 * The entry point of the application, simply loading the fxml sheet and starting the JavaFx Scene
 */
public class App extends Application {
    private final static String APP_NAME = "PTCGTool";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("appLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_NAME);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}