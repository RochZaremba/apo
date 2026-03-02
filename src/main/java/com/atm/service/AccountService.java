package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.store.DataStore;
import com.atm.util.ATMLogger;

import java.util.logging.Logger;

/**
 * Serwis operacji na koncie — saldo, wypłata, wpłata.
 * Obsługuje walidację kwot, wielokrotności 10 i dziennego limitu.
 *
 * @author Kornel Szkutnik
 * @version 2.0
 */
public class AccountService {
    private static final Logger logger = ATMLogger.getLogger(AccountService.class);
    private final DataStore dataStore;

    public AccountService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Pobiera saldo konta i loguje sprawdzenie.
     *
     * @param account konto do sprawdzenia
     * @return aktualne saldo
     */
    public double getBalance(Account account) {
        Transaction tx = new Transaction(TransactionType.BALANCE_CHECK, 0,
                account.getAccountNumber(), null, account.getBalance());
        dataStore.addTransaction(tx);
        logger.info("Sprawdzenie salda: " + account.getAccountNumber()
                + " → " + String.format("%.2f PLN", account.getBalance()));
        return account.getBalance();
    }

    /**
     * Wypłata z konta.
     * Kwota musi być wielokrotnością 10, nie może przekroczyć salda ani limitu
     * dziennego.
     *
     * @param account konto źródłowe
     * @param amount  kwota wypłaty
     * @return potwierdzenie transakcji
     * @throws IllegalArgumentException jeśli walidacja się nie powiodła
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
        double remaining = account.getRemainingDailyLimit();
        if (amount > remaining) {
            throw new IllegalArgumentException(
                    String.format("Przekroczono dzienny limit wypłat.\nPozostało dziś: %.2f PLN z %.0f PLN.",
                            remaining, Account.MAX_DAILY_LIMIT));
        }

        account.withdraw(amount);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, amount,
                account.getAccountNumber(), null, account.getBalance());
        dataStore.addTransaction(tx);
        logger.info("Wypłata: " + amount + " PLN z konta " + account.getAccountNumber());
        return tx;
    }

    /**
     * Wpłata na konto.
     *
     * @param account konto docelowe
     * @param amount  kwota wpłaty (musi być &gt; 0)
     * @return potwierdzenie transakcji
     */
    public Transaction deposit(Account account, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota musi być większa od zera.");
        }

        account.deposit(amount);
        Transaction tx = new Transaction(TransactionType.DEPOSIT, amount,
                null, account.getAccountNumber(), account.getBalance());
        dataStore.addTransaction(tx);
        logger.info("Wpłata: " + amount + " PLN na konto " + account.getAccountNumber());
        return tx;
    }
}
