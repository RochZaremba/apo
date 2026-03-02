package com.atm.service;

import com.atm.model.Account;
import com.atm.store.DataStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Serwis uwierzytelniania — weryfikacja PIN, blokada karty po 3 nieudanych
 * próbach.
 */
public class AuthService {
    private static final int MAX_ATTEMPTS = 3;

    private final DataStore dataStore;
    private final Map<String, Integer> failedAttempts = new HashMap<>();

    public AuthService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Weryfikuje kartę i PIN.
     * 
     * @return konto powiązane z kartą
     * @throws AuthException jeśli karta nie istnieje, jest zablokowana lub PIN jest
     *                       nieprawidłowy
     */
    public Account authenticate(String cardNumber, String pin) throws AuthException {
        var accountOpt = dataStore.findByCardNumber(cardNumber);

        if (accountOpt.isEmpty()) {
            throw new AuthException("Nie rozpoznano karty. Sprawdź numer karty.");
        }

        Account account = accountOpt.get();

        if (account.getCard().isBlocked()) {
            throw new AuthException("Karta została zablokowana. Skontaktuj się z bankiem.");
        }

        if (!account.getPin().equals(pin)) {
            int attempts = failedAttempts.getOrDefault(cardNumber, 0) + 1;
            failedAttempts.put(cardNumber, attempts);

            if (attempts >= MAX_ATTEMPTS) {
                account.getCard().setBlocked(true);
                failedAttempts.remove(cardNumber);
                throw new AuthException("Karta została zablokowana po " + MAX_ATTEMPTS + " nieudanych próbach.");
            }

            int remaining = MAX_ATTEMPTS - attempts;
            throw new AuthException("Nieprawidłowy PIN. Pozostało prób: " + remaining);
        }

        // Reset na sukces
        failedAttempts.remove(cardNumber);
        return account;
    }

    /**
     * Resetuje licznik prób dla danej karty.
     */
    public void resetAttempts(String cardNumber) {
        failedAttempts.remove(cardNumber);
    }

    /**
     * Wyjątek uwierzytelniania.
     */
    public static class AuthException extends Exception {
        public AuthException(String message) {
            super(message);
        }
    }
}
