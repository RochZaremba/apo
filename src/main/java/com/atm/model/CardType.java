package com.atm.model;

/**
 * Typ karty bankowej.
 */
public enum CardType {
    DEBIT("Debetowa"),
    CREDIT("Kredytowa"),
    ATM("Bankomatowa");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
