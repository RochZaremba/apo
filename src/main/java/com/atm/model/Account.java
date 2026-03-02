package com.atm.model;

import com.atm.util.SecurityUtil;

import java.time.LocalDate;

/**
 * Reprezentuje konto bankowe użytkownika.
 * <p>
 * Konto przechowuje saldo, dane właściciela, zaszyfrowany PIN
 * oraz powiązaną kartę bankową. Obsługuje operacje wpłaty i wypłaty
 * z walidacją kwot oraz dziennym limitem wypłat.
 * </p>
 *
 * @author Roch Zaremba
 * @version 2.0
 */
public class Account {

    /** Maksymalny dzienny limit wypłat w PLN. */
    public static final double MAX_DAILY_LIMIT = 5000.0;

    private final String accountNumber;
    private final String ownerName;
    private double balance;
    private String pinHash;
    private final Card card;

    /** Kwota wypłacona w bieżącym dniu. */
    private double dailyWithdrawnAmount;
    /** Data ostatniej wypłaty (do resetu limitu dziennego). */
    private LocalDate lastWithdrawalDate;

    /**
     * Tworzy nowe konto bankowe.
     *
     * @param accountNumber unikalny numer konta
     * @param ownerName     imię i nazwisko właściciela
     * @param balance       początkowe saldo
     * @param rawPin        surowy PIN (zostanie zahashowany)
     * @param card          powiązana karta bankowa
     */
    public Account(String accountNumber, String ownerName, double balance, String rawPin, Card card) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.pinHash = SecurityUtil.hashPin(rawPin);
        this.card = card;
        this.dailyWithdrawnAmount = 0;
        this.lastWithdrawalDate = LocalDate.now();
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

    /**
     * Zwraca hash PIN-u (SHA-256).
     * 
     * @return hash PIN-u
     */
    public String getPinHash() {
        return pinHash;
    }

    /**
     * Ustawia nowy PIN (hashowany).
     * 
     * @param newRawPin nowy PIN w postaci surowej
     */
    public void setPinHash(String newRawPin) {
        this.pinHash = SecurityUtil.hashPin(newRawPin);
    }

    /**
     * Weryfikuje podany PIN.
     * 
     * @param rawPin surowy PIN do weryfikacji
     * @return true jeśli PIN jest prawidłowy
     */
    public boolean verifyPin(String rawPin) {
        return SecurityUtil.verifyPin(rawPin, this.pinHash);
    }

    public Card getCard() {
        return card;
    }

    /**
     * Zwraca kwotę wypłaconą w bieżącym dniu.
     * Automatycznie resetuje licznik przy nowym dniu.
     * 
     * @return kwota wypłacona dziś
     */
    public double getDailyWithdrawnAmount() {
        resetDailyLimitIfNewDay();
        return dailyWithdrawnAmount;
    }

    /**
     * Zwraca pozostały dostępny limit dzienny.
     * 
     * @return dostępna kwota do wypłaty w ramach limitu dziennego
     */
    public double getRemainingDailyLimit() {
        resetDailyLimitIfNewDay();
        return MAX_DAILY_LIMIT - dailyWithdrawnAmount;
    }

    /**
     * Wpłata na konto.
     *
     * @param amount kwota do wpłaty (musi być &gt; 0)
     * @throws IllegalArgumentException jeśli kwota &lt;= 0
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota wpłaty musi być większa od zera.");
        }
        this.balance += amount;
    }

    /**
     * Wypłata z konta z uwzględnieniem dziennego limitu.
     *
     * @param amount kwota do wypłaty
     * @throws IllegalArgumentException jeśli kwota nieprawidłowa, brak środków lub
     *                                  przekroczony limit
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Kwota wypłaty musi być większa od zera.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Niewystarczające środki na koncie.");
        }
        resetDailyLimitIfNewDay();
        if (dailyWithdrawnAmount + amount > MAX_DAILY_LIMIT) {
            throw new IllegalArgumentException(
                    String.format("Przekroczono dzienny limit wypłat (%.0f PLN).\nPozostało: %.2f PLN.",
                            MAX_DAILY_LIMIT, MAX_DAILY_LIMIT - dailyWithdrawnAmount));
        }
        this.balance -= amount;
        this.dailyWithdrawnAmount += amount;
        this.lastWithdrawalDate = LocalDate.now();
    }

    private void resetDailyLimitIfNewDay() {
        if (!LocalDate.now().equals(lastWithdrawalDate)) {
            dailyWithdrawnAmount = 0;
            lastWithdrawalDate = LocalDate.now();
        }
    }
}
