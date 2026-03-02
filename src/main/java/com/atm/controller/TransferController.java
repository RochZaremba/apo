package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.service.TransactionService;
import com.atm.ui.DialogHelper;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransferController {

    @FXML
    private Label balanceLabel;
    @FXML
    private TextField targetAccountField;
    @FXML
    private TextField amountField;
    @FXML
    private Label errorLabel;

    private Account account;
    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        balanceLabel.setText(String.format("Dostępne środki: %.2f PLN", account.getBalance()));
    }

    @FXML
    private void onTransfer() {
        String targetAccount = targetAccountField.getText().trim();
        String amountText = amountField.getText().trim();

        if (targetAccount.isEmpty()) {
            showError("Proszę wprowadzić numer konta odbiorcy.");
            return;
        }
        if (amountText.isEmpty()) {
            showError("Proszę wprowadzić kwotę przelewu.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);

            boolean confirmed = DialogHelper.confirm("Potwierdzenie przelewu",
                    "Czy na pewno chcesz wykonać przelew?",
                    String.format("Na konto: %s\nKwota: %.2f PLN", targetAccount, amount));
            if (!confirmed)
                return;

            Transaction tx = transactionService.transfer(account, targetAccount, amount);
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
