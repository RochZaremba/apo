package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.store.DataStore;

/**
 * Serwis operacji na koncie — saldo, wypłata, wpłata.
 */
public class AccountService {
    private final DataStore dataStore;

    public AccountService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Pobiera saldo konta.
     */
    public double getBalance(Account account) {
        Transaction tx = new Transaction(TransactionType.BALANCE_CHECK, 0, account.getAccountNumber(), null);
        dataStore.addTransaction(tx);
        return account.getBalance();
    }

    /**
     * Wypłata z konta.
     * 
     * @param account konto źródłowe
     * @param amount  kwota (musi być wielokrotnością 10)
     * @return potwierdzenie transakcji
     */
    public Transaction withdraw(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota musi być większa od zera.");
        }
        if (amount % 10 != 0) {
            throw new IllegalArgumentException("Kwota musi być wielokrotnością 10 PLN.");
        }
        if (amount > account.getBalance()) {
            throw new IllegalArgumentException("Niewystarczające środki na koncie.\nDostępne: " +
                    String.format("%.2f PLN", account.getBalance()));
        }

        account.withdraw(amount);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, amount, account.getAccountNumber(), null);
        dataStore.addTransaction(tx);
        return tx;
    }

    /**
     * Wpłata na konto.
     */
    public Transaction deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota musi być większa od zera.");
        }

        account.deposit(amount);
        Transaction tx = new Transaction(TransactionType.DEPOSIT, amount, null, account.getAccountNumber());
        dataStore.addTransaction(tx);
        return tx;
    }
}
