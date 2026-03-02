package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import com.atm.store.DataStore;
import com.atm.util.ATMLogger;

import java.util.logging.Logger;

/**
 * Serwis przelewów między kontami z pełną walidacją.
 *
 * @author Kornel Szkutnik
 * @version 2.0
 */
public class TransactionService {
    private static final Logger logger = ATMLogger.getLogger(TransactionService.class);
    private final DataStore dataStore;

    public TransactionService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Wykonuje przelew z konta źródłowego na konto docelowe.
     *
     * @param fromAccount     konto nadawcy
     * @param toAccountNumber numer konta odbiorcy
     * @param amount          kwota przelewu
     * @return potwierdzenie transakcji
     * @throws IllegalArgumentException jeśli walidacja się nie powiodła
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
                fromAccount.getAccountNumber(), toAccountNumber, fromAccount.getBalance());
        dataStore.addTransaction(tx);
        logger.info("Przelew: " + amount + " PLN z " + fromAccount.getAccountNumber()
                + " na " + toAccountNumber);
        return tx;
    }
}
