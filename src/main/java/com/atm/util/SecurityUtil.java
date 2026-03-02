package com.atm.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Narzędzie bezpieczeństwa — hashowanie PIN-u algorytmem SHA-256.
 */
public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Hashuje PIN za pomocą SHA-256.
     * 
     * @param pin surowy PIN (4 cyfry)
     * @return hash w formacie hex
     */
    public static String hashPin(String pin) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pin.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 niedostępny", e);
        }
    }

    /**
     * Weryfikuje surowy PIN względem zapisanego hashu.
     * 
     * @param rawPin    surowy PIN wpisany przez użytkownika
     * @param hashedPin zapisany hash PIN-u
     * @return true jeśli PIN jest prawidłowy
     */
    public static boolean verifyPin(String rawPin, String hashedPin) {
        return hashPin(rawPin).equals(hashedPin);
    }
}
