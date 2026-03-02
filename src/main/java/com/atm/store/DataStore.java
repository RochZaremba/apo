package com.atm.store;

import com.atm.model.*;

import java.util.*;

/**
 * Singleton in-memory data store.
 * Przechowuje konta i karty z danymi demonstracyjnymi.
 */
public class DataStore {
    private static DataStore instance;

    private final Map<String, Account> accountsByCardNumber = new HashMap<>();
    private final Map<String, Account> accountsByAccountNumber = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    private DataStore() {
        seedDemoData();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void seedDemoData() {
        // Konto 1: Jan Kowalski
        Card card1 = new Card("1111-2222-3333-4444", CardType.DEBIT, "PL10-1234-0001");
        Account acc1 = new Account("PL10-1234-0001", "Jan Kowalski", 5000.00, "1234", card1);

        // Konto 2: Anna Nowak
        Card card2 = new Card("5555-6666-7777-8888", CardType.CREDIT, "PL10-1234-0002");
        Account acc2 = new Account("PL10-1234-0002", "Anna Nowak", 12500.00, "5678", card2);

        // Konto 3: Piotr Wiśniewski
        Card card3 = new Card("9999-0000-1111-2222", CardType.ATM, "PL10-1234-0003");
        Account acc3 = new Account("PL10-1234-0003", "Piotr Wiśniewski", 800.00, "0000", card3);

        registerAccount(acc1);
        registerAccount(acc2);
        registerAccount(acc3);
    }

    private void registerAccount(Account account) {
        accountsByCardNumber.put(account.getCard().getCardNumber(), account);
        accountsByAccountNumber.put(account.getAccountNumber(), account);
    }

    public Optional<Account> findByCardNumber(String cardNumber) {
        return Optional.ofNullable(accountsByCardNumber.get(cardNumber));
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accountsByAccountNumber.get(accountNumber));
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> accountNumber.equals(t.getSourceAccountNumber())
                        || accountNumber.equals(t.getTargetAccountNumber()))
                .toList();
    }
}
