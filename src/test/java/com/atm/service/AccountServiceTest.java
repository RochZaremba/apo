package com.atm.service;

import com.atm.model.Account;
import com.atm.model.Transaction;
import com.atm.model.TransactionType;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla AccountService.
 *
 * @author Kornel Szkutnik
 */
class AccountServiceTest {

    private AccountService accountService;
    private Account testAccount;
    private AuthService authService;

    @BeforeEach
    void setUp() throws AuthService.AuthException {
        authService = new AuthService();
        accountService = new AccountService();
        testAccount = authService.authenticate("1111-2222-3333-4444", "1234");
    }

    @Test
    @DisplayName("Pobieranie salda")
    void shouldReturnCorrectBalance() {
        double balance = accountService.getBalance(testAccount);
        assertEquals(5000.00, balance);
    }

    @Test
    @DisplayName("Wypłata prawidłowej kwoty")
    void shouldWithdrawValidAmount() {
        Transaction tx = accountService.withdraw(testAccount, 200);
        assertNotNull(tx);
        assertEquals(TransactionType.WITHDRAWAL, tx.getType());
        assertEquals(200, tx.getAmount());
        assertEquals(4800.00, testAccount.getBalance());
    }

    @Test
    @DisplayName("Odrzucenie wypłaty niebędącej wielokrotnością 10")
    void shouldRejectNonMultipleOf10() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw(testAccount, 55));
    }

    @Test
    @DisplayName("Odrzucenie wypłaty przekraczającej saldo")
    void shouldRejectOverdraft() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw(testAccount, 10000));
    }

    @Test
    @DisplayName("Wpłata prawidłowej kwoty")
    void shouldDepositValidAmount() {
        Transaction tx = accountService.deposit(testAccount, 500);
        assertNotNull(tx);
        assertEquals(TransactionType.DEPOSIT, tx.getType());
        assertEquals(5500.00, testAccount.getBalance());
    }

    @Test
    @DisplayName("Odrzucenie wpłaty ujemnej kwoty")
    void shouldRejectNegativeDeposit() {
        assertThrows(IllegalArgumentException.class,
                () -> accountService.deposit(testAccount, -100));
    }

    @Test
    @DisplayName("Odrzucenie wypłaty przekraczającej limit dzienny")
    void shouldRejectOverDailyLimit() {
        accountService.withdraw(testAccount, 3000);
        accountService.withdraw(testAccount, 2000);
        // Limit 5000 PLN wyczerpany
        assertThrows(IllegalArgumentException.class,
                () -> accountService.withdraw(testAccount, 10));
    }
}
