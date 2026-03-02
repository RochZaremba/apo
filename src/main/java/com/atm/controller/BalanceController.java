package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.AccountService;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BalanceController {

    @FXML
    private Label ownerLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label balanceLabel;

    private Account account;
    private final AccountService accountService = new AccountService();

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        double balance = accountService.getBalance(account);

        ownerLabel.setText(account.getOwnerName());
        accountLabel.setText("Nr konta: " + account.getAccountNumber());
        balanceLabel.setText(String.format("%.2f", balance));
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("menu.fxml");
    }
}
