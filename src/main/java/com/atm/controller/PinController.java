package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AuthService;
import com.atm.ui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class PinController {

    @FXML
    private PasswordField pinField;
    @FXML
    private Label cardInfoLabel;
    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();
    private String cardNumber;

    @FXML
    public void initialize() {
        cardNumber = SceneManager.getInstance().getControllerData();
        cardInfoLabel.setText("Karta: " + maskCardNumber(cardNumber));
    }

    @FXML
    private void onNumPad(ActionEvent event) {
        Button btn = (Button) event.getSource();
        if (pinField.getText().length() < 4) {
            pinField.appendText(btn.getText());
        }
        hideError();
    }

    @FXML
    private void onBackspace() {
        String text = pinField.getText();
        if (!text.isEmpty()) {
            pinField.setText(text.substring(0, text.length() - 1));
        }
        hideError();
    }

    @FXML
    private void onConfirmPin() {
        String pin = pinField.getText();

        if (pin.length() != 4) {
            showError("PIN musi składać się z 4 cyfr.");
            return;
        }

        try {
            Account account = authService.authenticate(cardNumber, pin);
            SceneManager.getInstance().setControllerData(account);
            SceneManager.getInstance().switchScene("menu.fxml");
        } catch (AuthService.AuthException e) {
            showError(e.getMessage());
            pinField.clear();
        }
    }

    @FXML
    private void onCancel() {
        SceneManager.getInstance().switchScene("welcome.fxml");
    }

    private String maskCardNumber(String card) {
        if (card.length() > 4) {
            return "****-****-****-" + card.substring(card.length() - 4);
        }
        return card;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
