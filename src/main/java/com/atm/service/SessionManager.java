package com.atm.service;

import com.atm.ui.SceneManager;
import com.atm.util.ATMLogger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

import java.util.logging.Logger;

/**
 * Zarządza sesją użytkownika — automatyczne wylogowanie po okresie
 * bezczynności.
 *
 * @author Kornel Szkutnik
 * @version 1.0
 */
public class SessionManager {
    private static final Logger logger = ATMLogger.getLogger(SessionManager.class);

    /** Czas bezczynności przed wylogowaniem (w sekundach). */
    private static final int TIMEOUT_SECONDS = 60;

    private static SessionManager instance;
    private PauseTransition sessionTimer;
    private boolean active = false;

    private SessionManager() {
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Rozpoczyna sesję — uruchamia timer bezczynności.
     */
    public void startSession() {
        if (sessionTimer != null) {
            sessionTimer.stop();
        }
        sessionTimer = new PauseTransition(Duration.seconds(TIMEOUT_SECONDS));
        sessionTimer.setOnFinished(event -> {
            logger.warning("Sesja wygasła po " + TIMEOUT_SECONDS + "s bezczynności.");
            endSession();
            Platform.runLater(() -> SceneManager.getInstance().switchScene("welcome.fxml"));
        });
        sessionTimer.playFromStart();
        active = true;
        logger.info("Sesja rozpoczęta (timeout: " + TIMEOUT_SECONDS + "s).");
    }

    /**
     * Resetuje timer bezczynności — wywoływane przy każdej interakcji użytkownika.
     */
    public void resetTimer() {
        if (active && sessionTimer != null) {
            sessionTimer.playFromStart();
        }
    }

    /**
     * Kończy sesję — zatrzymuje timer.
     */
    public void endSession() {
        if (sessionTimer != null) {
            sessionTimer.stop();
            sessionTimer = null;
        }
        active = false;
        logger.info("Sesja zakończona.");
    }

    /**
     * Sprawdza czy sesja jest aktywna.
     * 
     * @return true jeśli jest aktywna sesja
     */
    public boolean isActive() {
        return active;
    }
}
