package com.atm.model;

/**
 * Typ karty bankowej obsługiwanej przez bankomat.
 *
 * @author Roch Zaremba
 * @version 2.0
 */
public enum CardType {
    /** Karta debetowa (płatnicza) */
    DEBIT("Debetowa"),
    /** Karta kredytowa */
    CREDIT("Kredytowa"),
    /** Karta bankomatowa */
    ATM("Bankomatowa");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    /** @return polska nazwa typu karty */
    public String getDisplayName() {
        return displayName;
    }
}
