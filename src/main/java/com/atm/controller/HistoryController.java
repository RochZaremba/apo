package com.atm.controller;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.store.DataStore;
import com.atm.ui.SceneManager;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class HistoryController {

    @FXML
    private Label accountLabel;
    @FXML
    private VBox historyContainer;
    @FXML
    private Label emptyLabel;

    private Account account;

    @FXML
    public void initialize() {
        account = SceneManager.getInstance().getControllerData();
        accountLabel.setText("Konto: " + account.getAccountNumber());

        List<Transaction> transactions = DataStore.getInstance()
                .getTransactionsForAccount(account.getAccountNumber());

        if (transactions.isEmpty()) {
            emptyLabel.setVisible(true);
            emptyLabel.setManaged(true);
        } else {
            for (Transaction tx : transactions) {
                historyContainer.getChildren().add(createTransactionCard(tx));
                historyContainer.getChildren().add(new Separator());
            }
        }
    }

    private VBox createTransactionCard(Transaction tx) {
        VBox card = new VBox(4);
        card.getStyleClass().add("history-card");
        card.setPadding(new Insets(10));

        // Wiersz 1: typ + kwota
        HBox row1 = new HBox();
        String icon = getIcon(tx.getType());
        Label typeLabel = new Label(icon + " " + tx.getType().getDisplayName());
        typeLabel.getStyleClass().add("history-type");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String sign = getSign(tx);
        Label amountLabel = new Label(sign + String.format("%.2f PLN", tx.getAmount()));
        amountLabel.getStyleClass().add(sign.equals("+") ? "history-amount-positive" : "history-amount-negative");

        row1.getChildren().addAll(typeLabel, spacer, amountLabel);

        // Wiersz 2: data + saldo po
        HBox row2 = new HBox();
        Label dateLabel = new Label(tx.getFormattedTimestamp());
        dateLabel.getStyleClass().add("history-date");

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Label balanceLabel = new Label("Saldo: " + String.format("%.2f PLN", tx.getBalanceAfter()));
        balanceLabel.getStyleClass().add("history-balance");

        row2.getChildren().addAll(dateLabel, spacer2, balanceLabel);

        // Wiersz 3: ID transakcji
        Label idLabel = new Label("#" + tx.getId());
        idLabel.getStyleClass().add("history-id");

        card.getChildren().addAll(row1, row2, idLabel);
        return card;
    }

    private String getIcon(TransactionType type) {
        return switch (type) {
            case WITHDRAWAL -> "💰";
            case DEPOSIT -> "💵";
            case TRANSFER -> "🔄";
            case BALANCE_CHECK -> "📊";
            case PIN_CHANGE -> "🔑";
        };
    }

    private String getSign(Transaction tx) {
        if (tx.getType() == TransactionType.DEPOSIT)
            return "+";
        if (tx.getType() == TransactionType.TRANSFER
                && tx.getTargetAccountNumber() != null
                && tx.getTargetAccountNumber().equals(account.getAccountNumber())) {
            return "+";
        }
        if (tx.getType() == TransactionType.BALANCE_CHECK || tx.getType() == TransactionType.PIN_CHANGE)
            return "";
        return "-";
    }

    @FXML
    private void onBack() {
        SceneManager.getInstance().setControllerData(account);
        SceneManager.getInstance().switchScene("menu.fxml");
    }
}
