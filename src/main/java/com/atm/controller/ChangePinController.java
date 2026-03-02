package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.service.AuthService;
import com.atm.store.DataStore;
import com.atm.ui.DialogHelper;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class ChangePinController {

    @FXML
    private Label infoLabel;
    @FXML
    private PasswordField oldPinField;
    @FXML
    private PasswordField newPinField;
    @FXML
    private PasswordField confirmPinField;
    @FXML
    private Label errorLabel;

    private Account account;
    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
    }

    @FXML
    private void onChangePin() {
        String oldPin = oldPinField.getText();
        String newPin = newPinField.getText();
        String confirmPin = confirmPinField.getText();

        try {
            authService.changePin(account, oldPin, newPin, confirmPin);

            // Zarejestruj operację
            Transaction tx = new Transaction(TransactionType.PIN_CHANGE, 0,
                    account.getAccountNumber(), null, account.getBalance());
            DataStore.getInstance().addTransaction(tx);

            DialogHelper.info("Sukces", "PIN został pomyślnie zmieniony!");

            SceneManager.getInstance().setControllerData(account);
            SceneManager.getInstance().switchScene("menu.fxml");
        } catch (AuthService.AuthException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("menu.fxml");
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
