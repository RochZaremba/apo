package com.atm.controller;

import com.atm.model.Account;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuController {

    @FXML
    private Label welcomeLabel;

    private Account account;

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        welcomeLabel.setText("Witaj, " + account.getOwnerName() + "!");
    }

    @FXML
    private void onWithdraw() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("withdraw.fxml");
    }

    @FXML
    private void onDeposit() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("deposit.fxml");
    }

    @FXML
    private void onBalance() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("balance.fxml");
    }

    @FXML
    private void onTransfer() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("transfer.fxml");
    }

    @FXML
    private void onLogout() {
        SceneManager.getInstance().switchScene("welcome.fxml");
    }
}
