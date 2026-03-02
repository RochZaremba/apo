package com.atm.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

/**
 * Helper do wyświetlania okien dialogowych potwierdzenia operacji.
 *
 * @author Kornel Szkutnik
 * @version 1.0
 */
public class DialogHelper {

    private DialogHelper() {
    }

    /**
     * Wyświetla dialog potwierdzenia operacji.
     *
     * @param title   tytuł okna
     * @param header  nagłówek
     * @param content treść z detalami operacji
     * @return true jeśli użytkownik potwierdził
     */
    public static boolean confirm(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType confirmBtn = new ButtonType("Potwierdź", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmBtn, cancelBtn);

        styleDialog(alert);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == confirmBtn;
    }

    /**
     * Wyświetla dialog informacyjny o sukcesie.
     *
     * @param title   tytuł okna
     * @param content treść
     */
    public static void info(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleDialog(alert);
        alert.showAndWait();
    }

    /**
     * Wyświetla dialog z komunikatem o błędzie.
     *
     * @param title   tytuł okna
     * @param content treść błędu
     */
    public static void error(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        styleDialog(alert);
        alert.showAndWait();
    }

    private static void styleDialog(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: #1a1a2e;" +
                        "-fx-font-family: System;");
        dialogPane.lookup(".content.label").setStyle(
                "-fx-text-fill: #c0c0ee; -fx-font-size: 14px;");
        dialogPane.lookup(".header-panel").setStyle(
                "-fx-background-color: #12123a;");
    }
}
