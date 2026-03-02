package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.service.AccountService;
import com.atm.ui.DialogHelper;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepositController {

    @FXML
    private Label balanceLabel;
    @FXML
    private TextField amountField;
    @FXML
    private Label errorLabel;

    private Account account;
    private final AccountService accountService = new AccountService();

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        balanceLabel.setText(String.format("Aktualne saldo: %.2f PLN", account.getBalance()));
    }

    @FXML
    private void onDeposit() {
        String text = amountField.getText().trim();
        if (text.isEmpty()) {
            showError("Proszę wprowadzić kwotę.");
            return;
        }

        try {
            double amount = Double.parseDouble(text);

            boolean confirmed = DialogHelper.confirm("Potwierdzenie wpłaty",
                    "Czy na pewno chcesz wpłacić?",
                    String.format("Kwota: %.2f PLN", amount));
            if (!confirmed)
                return;

            Transaction tx = accountService.deposit(account, amount);
            SceneManager.getInstance().setControllerData(new Object[] { account, tx });
            SceneManager.getInstance().switchScene("receipt.fxml");
        } catch (NumberFormatException e) {
            showError("Nieprawidłowa kwota.");
        } catch (IllegalArgumentException e) {
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
