# 🏦 Bankomat ATM — Symulator

Aplikacja symulująca działanie bankomatu, zbudowana w **Java 17** z interfejsem graficznym **JavaFX 21**.

## ✨ Funkcjonalności

- **Wkładanie karty** — wprowadzenie numeru karty bankowej (kredytowa, płatnicza, bankomatowa)
- **Weryfikacja PIN** — klawiatura numeryczna, blokada karty po 3 nieudanych próbach
- **Wypłata gotówki** — predefiniowane kwoty (50–2000 PLN) lub kwota własna (wielokrotność 10)
- **Wpłata gotówki** — dowolna kwota z potwierdzeniem
- **Sprawdzanie salda** — wyświetlenie stanu konta
- **Przelew** — transfer środków na inne konto z walidacją
- **Historia transakcji** — przegląd wszystkich operacji na koncie
- **Zmiana PIN** — zmiana kodu PIN z weryfikacją starego
- **Potwierdzenie transakcji** — dialog potwierdzenia przed operacjami + pokwitowanie

## 🔒 Bezpieczeństwo

- **Hashowanie PIN (SHA-256)** — PINy nie są przechowywane w postaci jawnej
- **Blokada karty** — po 3 nieudanych próbach PIN
- **Timeout sesji** — automatyczne wylogowanie po 60s bezczynności
- **Dzienny limit wypłat** — maksymalnie 5000 PLN dziennie
- **Dialogi potwierdzenia** — przed każdą operacją finansową

## 🏗️ Architektura

```
com.atm
├── model/          # Account, Card, Transaction, enumy
├── store/          # DataStore — singleton, dane in-memory + 3 konta demo
├── service/        # AuthService, AccountService, TransactionService, SessionManager
├── controller/     # 10 kontrolerów JavaFX
├── ui/             # SceneManager (animacje fade, motywy), DialogHelper
├── util/           # SecurityUtil (SHA-256), ATMLogger
└── ATMApplication  # Punkt wejścia
```

## 🎨 Motywy

Aplikacja obsługuje **dwa motywy** — ciemny (domyślny) i jasny — przełączane przyciskiem w menu głównym.

## 🚀 Uruchomienie

### Wymagania
- **Java 17+** (JDK)
- **Maven 3.8+**

### Kompilacja i uruchomienie

```bash
# Kompilacja
mvn clean compile

# Uruchomienie testy
mvn test

# Uruchomienie aplikacji
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
- **JUnit 5** — testy jednostkowe
- **SHA-256** — hashowanie PIN-ów

## 👥 Autorzy

| Osoba | Zakres odpowiedzialności |
|---|---|
| **Roch Zaremba** | Struktura projektu, model danych, DataStore, SecurityUtil, ATMLogger |
| **Kornel Szkutnik** | Warstwa serwisów, logika biznesowa, SessionManager, DialogHelper, testy JUnit |
| **Jan Baczyński** | Interfejs użytkownika, kontrolery, widoki FXML, stylizacja CSS, animacje, motywy |
