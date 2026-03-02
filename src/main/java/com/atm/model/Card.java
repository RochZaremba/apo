package com.atm.model;

/**
 * Reprezentuje kartę bankową.
 */
public class Card {
    private final String cardNumber;
    private final CardType cardType;
    private final String linkedAccountNumber;
    private boolean blocked;

    public Card(String cardNumber, CardType cardType, String linkedAccountNumber) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.linkedAccountNumber = linkedAccountNumber;
        this.blocked = false;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public String getLinkedAccountNumber() {
        return linkedAccountNumber;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
