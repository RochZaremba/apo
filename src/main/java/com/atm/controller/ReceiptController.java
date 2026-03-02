package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ReceiptController {

    @FXML
    private Label typeLabel;
    @FXML
    private Label amountLabel;
    @FXML
    private Label targetLabel;
    @FXML
    private Label targetValueLabel;
    @FXML
    private Label txIdLabel;
    @FXML
    private Label timestampLabel;
    @FXML
    private Label newBalanceLabel;

    private Account account;

    @FXML
    public void initialize() {
        Object[] data = SceneManager.getInstance().getControllerData();
        account = (Account) data[0];
        Transaction tx = (Transaction) data[1];

        typeLabel.setText(tx.getType().getDisplayName());
        amountLabel.setText(String.format("%.2f PLN", tx.getAmount()));
        txIdLabel.setText("#" + tx.getId());
        timestampLabel.setText(tx.getFormattedTimestamp());
        newBalanceLabel.setText(String.format("%.2f PLN", account.getBalance()));

        // Pokaż konto docelowe dla przelewów
        if (tx.getType() == TransactionType.TRANSFER && tx.getTargetAccountNumber() != null) {
            targetLabel.setText("Konto odbiorcy:");
            targetLabel.setVisible(true);
            targetLabel.setManaged(true);
            targetValueLabel.setText(tx.getTargetAccountNumber());
            targetValueLabel.setVisible(true);
            targetValueLabel.setManaged(true);
        }
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("menu.fxml");
    }
}
