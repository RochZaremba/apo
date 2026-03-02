package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla TransactionService.
 *
 * @author Kornel Szkutnik
 */
class TransactionServiceTest {

    private TransactionService transactionService;
    private Account senderAccount;
    private AuthService authService;

    @BeforeEach
    void setUp() throws AuthService.AuthException {
        authService = new AuthService();
        transactionService = new TransactionService();
        senderAccount = authService.authenticate("1111-2222-3333-4444", "1234");
    }

    @Test
    @DisplayName("Przelew pomiędzy kontami")
    void shouldTransferBetweenAccounts() {
        double initialBalance = senderAccount.getBalance();
        Transaction tx = transactionService.transfer(senderAccount, "PL10-1234-0002", 100);

        assertNotNull(tx);
        assertEquals(TransactionType.TRANSFER, tx.getType());
        assertEquals(100, tx.getAmount());
        assertEquals(initialBalance - 100, senderAccount.getBalance());
    }

    @Test
    @DisplayName("Odrzucenie przelewu na to samo konto")
    void shouldRejectSelfTransfer() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(senderAccount, "PL10-1234-0001", 100));
    }

    @Test
    @DisplayName("Odrzucenie przelewu na nieistniejące konto")
    void shouldRejectTransferToUnknownAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(senderAccount, "PL99-9999-9999", 100));
    }

    @Test
    @DisplayName("Odrzucenie przelewu bez wystarczających środków")
    void shouldRejectTransferWithInsufficientFunds() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(senderAccount, "PL10-1234-0002", 999999));
    }

    @Test
    @DisplayName("Odrzucenie przelewu z kwotą zero")
    void shouldRejectZeroAmountTransfer() {
        assertThrows(IllegalArgumentException.class,
                () -> transactionService.transfer(senderAccount, "PL10-1234-0002", 0));
    }
}
