package com.atm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Reprezentuje transakcję bankową.
 */
public class Transaction {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final String id;
    private final TransactionType type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final String sourceAccountNumber;
    private final String targetAccountNumber;

    public Transaction(TransactionType type, double amount, String sourceAccountNumber, String targetAccountNumber) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(FORMATTER);
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }
}
