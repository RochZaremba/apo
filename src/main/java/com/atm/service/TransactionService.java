package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.store.DataStore;

/**
 * Serwis przelewów między kontami.
 */
public class TransactionService {
    private final DataStore dataStore;

    public TransactionService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Wykonuje przelew z konta źródłowego na konto docelowe.
     */
    public Transaction transfer(Account fromAccount, String toAccountNumber, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota przelewu musi być większa od zera.");
        }
        if (fromAccount.getAccountNumber().equals(toAccountNumber)) {
            throw new IllegalArgumentException("Nie można wykonać przelewu na to samo konto.");
        }

        var targetOpt = dataStore.findByAccountNumber(toAccountNumber);
        if (targetOpt.isEmpty()) {
            throw new IllegalArgumentException("Konto docelowe '" + toAccountNumber + "' nie istnieje.");
        }

        if (amount > fromAccount.getBalance()) {
            throw new IllegalArgumentException("Niewystarczające środki na koncie.\nDostępne: " +
                    String.format("%.2f PLN", fromAccount.getBalance()));
        }

        Account toAccount = targetOpt.get();
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        Transaction tx = new Transaction(TransactionType.TRANSFER, amount,
                fromAccount.getAccountNumber(), toAccountNumber);
        dataStore.addTransaction(tx);
        return tx;
    }
}
