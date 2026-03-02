package com.atm.controller;

import com.atm.model.Account;
import com.atm.service.SessionManager;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Button themeButton;

    private Account account;

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        welcomeLabel.setText("Witaj, " + account.getOwnerName() + "!");
        updateThemeButton();
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
    private void onHistory() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("history.fxml");
    }

    @FXML
    private void onChangePin() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("changepin.fxml");
    }

    @FXML
    private void onToggleTheme() {
        SceneManager.getInstance().toggleTheme();
        updateThemeButton();
    }

    @FXML
    private void onLogout() {
        SessionManager.getInstance().endSession();
        SceneManager.getInstance().switchScene("welcome.fxml");
    }

    private void updateThemeButton() {
        boolean dark = SceneManager.getInstance().isDarkTheme();
        themeButton.setText(dark ? "☀ Jasny" : "🌙 Ciemny");
    }
}
