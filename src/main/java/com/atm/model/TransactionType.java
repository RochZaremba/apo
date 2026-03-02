package com.atm.model;

/**
 * Typ transakcji bankowej.
 */
public enum TransactionType {
    WITHDRAWAL("Wypłata"),
    DEPOSIT("Wpłata"),
    TRANSFER("Przelew"),
    BALANCE_CHECK("Sprawdzenie salda");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
