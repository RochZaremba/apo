package com.atm.service;

import com.atm.model.Account;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla AuthService.
 *
 * @author Kornel Szkutnik
 */
class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    @DisplayName("Pomyślne uwierzytelnienie z prawidłowym PIN")
    void shouldAuthenticateWithCorrectPin() throws AuthService.AuthException {
        Account account = authService.authenticate("1111-2222-3333-4444", "1234");
        assertNotNull(account);
        assertEquals("Jan Kowalski", account.getOwnerName());
    }

    @Test
    @DisplayName("Odrzucenie nieprawidłowego PIN")
    void shouldRejectWrongPin() {
        AuthService.AuthException ex = assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("1111-2222-3333-4444", "9999"));
        assertTrue(ex.getMessage().contains("Nieprawidłowy PIN"));
    }

    @Test
    @DisplayName("Odrzucenie nieznanej karty")
    void shouldRejectUnknownCard() {
        AuthService.AuthException ex = assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("0000-0000-0000-0000", "1234"));
        assertTrue(ex.getMessage().contains("Nie rozpoznano karty"));
    }

    @Test
    @DisplayName("Blokada karty po 3 nieudanych próbach")
    void shouldBlockCardAfterThreeFailedAttempts() {
        assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("5555-6666-7777-8888", "0000"));
        assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("5555-6666-7777-8888", "0000"));

        AuthService.AuthException ex = assertThrows(AuthService.AuthException.class,
                () -> authService.authenticate("5555-6666-7777-8888", "0000"));
        assertTrue(ex.getMessage().contains("zablokowana"));
    }

    @Test
    @DisplayName("Zmiana PIN z prawidłowym starym PIN")
    void shouldChangePinSuccessfully() throws AuthService.AuthException {
        Account account = authService.authenticate("9999-0000-1111-2222", "0000");
        authService.changePin(account, "0000", "1111", "1111");

        // Nowy PIN powinien działać
        assertDoesNotThrow(() -> authService.authenticate("9999-0000-1111-2222", "1111"));
    }

    @Test
    @DisplayName("Odrzucenie zmiany PIN z nieprawidłowym starym PIN")
    void shouldRejectChangePinWithWrongOldPin() throws AuthService.AuthException {
        Account account = authService.authenticate("1111-2222-3333-4444", "1234");

        assertThrows(AuthService.AuthException.class,
                () -> authService.changePin(account, "9999", "5555", "5555"));
    }

    @Test
    @DisplayName("Odrzucenie zmiany PIN gdy nowe PIN-y się nie zgadzają")
    void shouldRejectChangePinWithMismatch() throws AuthService.AuthException {
        Account account = authService.authenticate("1111-2222-3333-4444", "1234");

        assertThrows(AuthService.AuthException.class,
                () -> authService.changePin(account, "1234", "5555", "6666"));
    }
}
