package com.atm;

import com.atm.ui.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Punkt wejścia aplikacji ATM.
 */
public class ATMApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bankomat ATM");
        primaryStage.setResizable(false);

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setPrimaryStage(primaryStage);
        sceneManager.switchScene("welcome.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
