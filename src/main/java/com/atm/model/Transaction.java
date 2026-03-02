package com.atm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Reprezentuje pojedynczą transakcję bankową.
 * <p>
 * Każda transakcja otrzymuje unikalny identyfikator (UUID),
 * znacznik czasu oraz informacje o koncie źródłowym i docelowym.
 * </p>
 *
 * @author Roch Zaremba
 * @version 2.0
 */
public class Transaction {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final double balanceAfter;

    /**
     * Tworzy nową transakcję.
     *
     * @param type                typ transakcji
     * @param amount              kwota operacji
     * @param sourceAccountNumber numer konta źródłowego (null dla wpłat)
     * @param targetAccountNumber numer konta docelowego (null dla wypłat)
     * @param balanceAfter        saldo po operacji
     */
    public Transaction(TransactionType type, double amount, String sourceAccountNumber,
            String targetAccountNumber, double balanceAfter) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
        this.balanceAfter = balanceAfter;
    }

    /** @return unikalny identyfikator transakcji */
    public String getId() {
        return id;
    }

    /** @return typ transakcji */
    public TransactionType getType() {
        return type;
    }

    /** @return kwota operacji */
    public double getAmount() {
        return amount;
    }

    /** @return znacznik czasu transakcji */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /** @return sformatowana data i czas (dd.MM.yyyy HH:mm:ss) */
    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    /** @return numer konta źródłowego */
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    /** @return numer konta docelowego */
    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    /** @return saldo konta po wykonaniu transakcji */
    public double getBalanceAfter() {
        return balanceAfter;
    }
}
