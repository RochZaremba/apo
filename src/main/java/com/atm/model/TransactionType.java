package com.atm.model;

/**
 * Typ transakcji bankowej wykonywanej w bankomacie.
 *
 * @author Roch Zaremba
 * @version 2.0
 */
public enum TransactionType {
    /** Wypłata gotówki */
    WITHDRAWAL("Wypłata"),
    /** Wpłata gotówki */
    DEPOSIT("Wpłata"),
    /** Przelew na inne konto */
    TRANSFER("Przelew"),
    /** Sprawdzenie salda */
    BALANCE_CHECK("Sprawdzenie salda"),
    /** Zmiana kodu PIN */
    PIN_CHANGE("Zmiana PIN");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    /** @return polska nazwa typu operacji */
    public String getDisplayName() {
        return displayName;
    }
}
