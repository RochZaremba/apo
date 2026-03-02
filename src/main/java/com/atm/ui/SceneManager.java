package com.atm.ui;

import com.atm.service.SessionManager;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Zarządza nawigacją między ekranami ATM z animacjami przejścia.
 *
 * @author Jan Baczyński
 * @version 2.0
 */
public class SceneManager {
    private static SceneManager instance;
    private Stage primaryStage;
    private Object currentControllerData;
    private boolean darkTheme = true;

    private static final String DARK_CSS = "/css/styles.css";
    private static final String LIGHT_CSS = "/css/styles-light.css";

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
     * Sprawdza czy aktywny jest ciemny motyw.
     * 
     * @return true jeśli ciemny motyw
     */
    public boolean isDarkTheme() {
        return darkTheme;
    }

    /**
     * Przełącza motyw między ciemnym a jasnym.
     */
    public void toggleTheme() {
        darkTheme = !darkTheme;
        applyTheme();
    }

    /**
     * Przełącza na podany ekran FXML z animacją fade.
     *
     * @param fxmlFile nazwa pliku FXML (np. "welcome.fxml")
     */
    public void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent newRoot = loader.load();

            // Reset timer sesji przy nawigacji
            SessionManager.getInstance().resetTimer();

            Scene scene = primaryStage.getScene();
            if (scene == null) {
                Scene newScene = new Scene(newRoot, 480, 750);
                applyThemeToScene(newScene);
                primaryStage.setScene(newScene);
                fadeIn(newRoot);
            } else {
                Parent oldRoot = scene.getRoot();
                final Scene currentScene = scene;
                fadeOut(oldRoot, () -> {
                    currentScene.setRoot(newRoot);
                    applyThemeToScene(currentScene);
                    fadeIn(newRoot);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Nie udało się załadować ekranu: " + fxmlFile, e);
        }
    }

    private void applyTheme() {
        Scene scene = primaryStage.getScene();
        if (scene != null) {
            applyThemeToScene(scene);
        }
    }

    private void applyThemeToScene(Scene scene) {
        scene.getStylesheets().clear();
        String cssPath = darkTheme ? DARK_CSS : LIGHT_CSS;
        scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
    }

    private void fadeIn(Parent node) {
        FadeTransition ft = new FadeTransition(Duration.millis(250), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void fadeOut(Parent node, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(Duration.millis(150), node);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }
}
