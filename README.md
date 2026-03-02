# 🏦 Bankomat ATM — Symulator

Aplikacja symulująca działanie bankomatu, zbudowana w **Java 17** z interfejsem graficznym **JavaFX 21**.

## ✨ Funkcjonalności

- **Wkładanie karty** — wprowadzenie numeru karty bankowej (kredytowa, płatnicza, bankomatowa)
- **Weryfikacja PIN** — klawiatura numeryczna, blokada karty po 3 nieudanych próbach
- **Wypłata gotówki** — predefiniowane kwoty (50–2000 PLN) lub kwota własna (wielokrotność 10)
- **Wpłata gotówki** — dowolna kwota
- **Sprawdzanie salda** — wyświetlenie stanu konta
- **Przelew** — transfer środków na inne konto z walidacją
- **Potwierdzenie transakcji** — numer operacji, data, nowe saldo

## 🏗️ Architektura

```
com.atm
├── model/          # Account, Card, Transaction, enumy (CardType, TransactionType)
├── store/          # DataStore — singleton, dane in-memory + 3 konta demo
├── service/        # AuthService, AccountService, TransactionService
├── controller/     # 8 kontrolerów JavaFX (Welcome, Pin, Menu, Withdraw, Deposit, Balance, Transfer, Receipt)
├── ui/             # SceneManager — nawigacja między ekranami
└── ATMApplication  # Punkt wejścia
```

## 🚀 Uruchomienie

### Wymagania
- **Java 17+** (JDK)
- **Maven 3.8+**

### Kompilacja i uruchomienie

```bash
# Kompilacja
mvn clean compile

# Uruchomienie
mvn javafx:run
```

## 🧪 Konta testowe

| Numer karty | PIN | Właściciel | Saldo | Typ karty |
|---|---|---|---|---|
| `1111-2222-3333-4444` | `1234` | Jan Kowalski | 5 000,00 PLN | Debetowa |
| `5555-6666-7777-8888` | `5678` | Anna Nowak | 12 500,00 PLN | Kredytowa |
| `9999-0000-1111-2222` | `0000` | Piotr Wiśniewski | 800,00 PLN | Bankomatowa |

Numery kont do przelewów: `PL10-1234-0001`, `PL10-1234-0002`, `PL10-1234-0003`

## 🛠️ Technologie

- **Java 17** — język programowania
- **JavaFX 21** — interfejs graficzny (FXML + CSS)
- **Maven** — zarządzanie projektem i zależnościami

## 👥 Autorzy

| Osoba | Zakres odpowiedzialności |
|---|---|
| **Roch Zaremba** | Struktura projektu, model danych, DataStore |
| **Kornel Szkutnik** | Warstwa serwisów, logika biznesowa, punkt wejścia aplikacji |
| **Jan Baczyński** | Interfejs użytkownika (kontrolery, widoki FXML, stylizacja CSS) |
