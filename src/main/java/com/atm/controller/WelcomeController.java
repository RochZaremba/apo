package com.atm.controller;

import com.atm.store.DataStore;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WelcomeController {

    @FXML
    private TextField cardNumberField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onInsertCard() {
        String cardNumber = cardNumberField.getText().trim();

        if (cardNumber.isEmpty()) {
            showError("Proszę wprowadzić numer karty.");
            return;
        }

        var accountOpt = DataStore.getInstance().findByCardNumber(cardNumber);
        if (accountOpt.isEmpty()) {
            showError("Nie rozpoznano karty. Sprawdź numer.");
            return;
        }

        if (accountOpt.get().getCard().isBlocked()) {
            showError("Ta karta jest zablokowana. Skontaktuj się z bankiem.");
            return;
        }

        SceneManager.getInstance().setControllerData(cardNumber);
        SceneManager.getInstance().switchScene("pin.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
