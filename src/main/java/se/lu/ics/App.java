package se.lu.ics;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lu.ics.controllers.AppController;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppController appController = new AppController(primaryStage);
        appController.showLoginDialog();
    }
}