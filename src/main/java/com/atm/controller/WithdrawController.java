package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.service.AccountService;
import com.atm.ui.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WithdrawController {

    @FXML
    private Label balanceLabel;
    @FXML
    private TextField customAmountField;
    @FXML
    private Label errorLabel;

    private Account account;
    private final AccountService accountService = new AccountService();

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        updateBalanceLabel();
    }

    @FXML
    private void onPresetAmount(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String text = btn.getText().replace(" PLN", "").trim();
        double amount = Double.parseDouble(text);
        doWithdraw(amount);
    }

    @FXML
    private void onWithdrawCustom() {
        String text = customAmountField.getText().trim();
        if (text.isEmpty()) {
            showError("Proszę wprowadzić kwotę.");
            return;
        }
        try {
            double amount = Double.parseDouble(text);
            doWithdraw(amount);
        } catch (NumberFormatException e) {
            showError("Nieprawidłowa kwota.");
        }
    }

    private void doWithdraw(double amount) {
        try {
            Transaction tx = accountService.withdraw(account, amount);
            SceneManager.getInstance().setControllerData(new Object[] { account, tx });
            SceneManager.getInstance().switchScene("receipt.fxml");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("menu.fxml");
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(String.format("Dostępne środki: %.2f PLN", account.getBalance()));
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
