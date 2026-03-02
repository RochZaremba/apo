package com.atm.model;

/**
 * Reprezentuje kartę bankową użytkownika.
 * <p>
 * Karta posiada unikalny numer, typ (debetowa, kredytowa, bankomatowa)
 * oraz powiązanie z kontem bankowym. Może zostać zablokowana
 * po przekroczeniu liczby nieudanych prób PIN.
 * </p>
 *
 * @author Roch Zaremba
 * @version 2.0
 */
public class Card {

    private final String cardNumber;
    private final CardType cardType;
    private final String linkedAccountNumber;
    private boolean blocked;

    /**
     * Tworzy nową kartę bankową.
     *
     * @param cardNumber          unikalny numer karty (format XXXX-XXXX-XXXX-XXXX)
     * @param cardType            typ karty
     * @param linkedAccountNumber numer powiązanego konta bankowego
     */
    public Card(String cardNumber, CardType cardType, String linkedAccountNumber) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.linkedAccountNumber = linkedAccountNumber;
        this.blocked = false;
    }

    /** @return numer karty */
    public String getCardNumber() {
        return cardNumber;
    }

    /** @return typ karty (DEBIT, CREDIT, ATM) */
    public CardType getCardType() {
        return cardType;
    }

    /** @return numer powiązanego konta bankowego */
    public String getLinkedAccountNumber() {
        return linkedAccountNumber;
    }

    /** @return czy karta jest zablokowana */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Ustawia status blokady karty.
     * 
     * @param blocked true aby zablokować kartę
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    /**
     * Zwraca zamaskowany numer karty (np. ****-****-****-4444).
     * 
     * @return zamaskowany numer karty
     */
    public String getMaskedNumber() {
        if (cardNumber.length() > 4) {
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
    }
}
