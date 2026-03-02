package com.atm.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Zarządza nawigacją między ekranami ATM.
 */
public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;
    private Object currentControllerData;

    private SceneManager() {
    }

    public static synchronized SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Przekazuje dane do następnego kontrolera.
     */
    public void setControllerData(Object data) {
        this.currentControllerData = data;
    }

    @SuppressWarnings("unchecked")
    public <T> T getControllerData() {
        return (T) currentControllerData;
    }

    /**
     * Przełącza na podany ekran FXML.
     * 
     * @param fxmlFile nazwa pliku FXML (np. "welcome.fxml")
     */
    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                scene = new Scene(root, 480, 700);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                primaryStage.setScene(scene);
            } else {
                scene.setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Nie udało się załadować ekranu: " + fxmlFile, e);
        }
    }
}
