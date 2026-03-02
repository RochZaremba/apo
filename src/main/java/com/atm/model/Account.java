package com.atm.model;

/**
 * Reprezentuje konto bankowe.
 */
public class Account {
    private final String accountNumber;
    private final String ownerName;
    private double balance;
    private final String pin;
    private final Card card;

    public Account(String accountNumber, String ownerName, double balance, String pin, Card card) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.pin = pin;
        this.card = card;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
        return balance;
    }

    public String getPin() {
        return pin;
    }

    public Card getCard() {
        return card;
    }

    /**
     * Wpłata na konto.
     * 
     * @param amount kwota do wpłaty (musi być > 0)
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota wpłaty musi być większa od zera.");
        }
        this.balance += amount;
    }

    /**
     * Wypłata z konta.
     * 
     * @param amount kwota do wypłaty
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota wypłaty musi być większa od zera.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Niewystarczające środki na koncie.");
        }
        this.balance -= amount;
    }
}
