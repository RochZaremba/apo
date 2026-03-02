package com.atm.service;

import com.atm.model.Account;
import com.atm.store.DataStore;
import com.atm.util.ATMLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Serwis uwierzytelniania — weryfikacja PIN z hashowaniem SHA-256,
 * blokada karty po 3 nieudanych próbach, zmiana kodu PIN.
 *
 * @author Kornel Szkutnik
 * @version 2.0
 */
public class AuthService {
    private static final Logger logger = ATMLogger.getLogger(AuthService.class);
    private static final int MAX_ATTEMPTS = 3;

    private final DataStore dataStore;
    private final Map<String, Integer> failedAttempts = new HashMap<>();

    public AuthService() {
        this.dataStore = DataStore.getInstance();
    }

    /**
     * Weryfikuje kartę i PIN (porównanie z hashem SHA-256).
     *
     * @param cardNumber numer karty
     * @param pin        surowy PIN
     * @return konto powiązane z kartą
     * @throws AuthException jeśli karta nie istnieje, jest zablokowana lub PIN jest
     *                       nieprawidłowy
     */
    public Account authenticate(String cardNumber, String pin) throws AuthException {
        var accountOpt = dataStore.findByCardNumber(cardNumber);

        if (accountOpt.isEmpty()) {
            logger.warning("Próba logowania z nieznaną kartą: " + cardNumber);
            throw new AuthException("Nie rozpoznano karty. Sprawdź numer karty.");
        }

        Account account = accountOpt.get();

        if (account.getCard().isBlocked()) {
            logger.warning("Próba logowania na zablokowaną kartę: " + cardNumber);
            throw new AuthException("Karta została zablokowana. Skontaktuj się z bankiem.");
        }

        if (!account.verifyPin(pin)) {
            int attempts = failedAttempts.getOrDefault(cardNumber, 0) + 1;
            failedAttempts.put(cardNumber, attempts);

            logger.warning("Nieudana próba PIN #" + attempts + " dla karty: " + cardNumber);

            if (attempts >= MAX_ATTEMPTS) {
                account.getCard().setBlocked(true);
                failedAttempts.remove(cardNumber);
                logger.severe("Karta ZABLOKOWANA po " + MAX_ATTEMPTS + " nieudanych próbach: " + cardNumber);
                throw new AuthException("Karta została zablokowana po " + MAX_ATTEMPTS + " nieudanych próbach.");
            }

            int remaining = MAX_ATTEMPTS - attempts;
            throw new AuthException("Nieprawidłowy PIN. Pozostało prób: " + remaining);
        }

        // Reset na sukces
        failedAttempts.remove(cardNumber);
        logger.info("Pomyślne uwierzytelnienie: " + account.getOwnerName());
        return account;
    }

    /**
     * Zmienia PIN konta.
     *
     * @param account    konto użytkownika
     * @param oldPin     aktualny PIN
     * @param newPin     nowy PIN
     * @param confirmPin potwierdzenie nowego PIN
     * @throws AuthException jeśli stary PIN jest nieprawidłowy lub nowe PIN-y się
     *                       nie zgadzają
     */
    public void changePin(Account account, String oldPin, String newPin, String confirmPin) throws AuthException {
        if (!account.verifyPin(oldPin)) {
            throw new AuthException("Aktualny PIN jest nieprawidłowy.");
        }
        if (newPin.length() != 4 || !newPin.matches("\\d{4}")) {
            throw new AuthException("Nowy PIN musi składać się z dokładnie 4 cyfr.");
        }
        if (!newPin.equals(confirmPin)) {
            throw new AuthException("Nowy PIN i potwierdzenie nie są zgodne.");
        }
        if (newPin.equals(oldPin)) {
            throw new AuthException("Nowy PIN musi być inny niż aktualny.");
        }

        account.setPinHash(newPin);
        logger.info("Zmiana PIN dla konta: " + account.getAccountNumber());
    }

    /**
     * Resetuje licznik nieudanych prób dla danej karty.
     * 
     * @param cardNumber numer karty
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
