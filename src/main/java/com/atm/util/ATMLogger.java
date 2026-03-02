package com.atm.util;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Centralna konfiguracja logowania aplikacji ATM.
 * Loguje operacje bankowe, uwierzytelnianie i błędy.
 */
public class ATMLogger {

    private static boolean configured = false;

    private ATMLogger() {
    }

    /**
     * Tworzy logger dla danej klasy z ustawioną konfiguracją.
     * 
     * @param clazz klasa, dla której tworzymy logger
     * @return skonfigurowany Logger
     */
    public static Logger getLogger(Class<?> clazz) {
        if (!configured) {
            configure();
        }
        return Logger.getLogger(clazz.getName());
    }

    private static synchronized void configure() {
        if (configured)
            return;
        Logger rootLogger = Logger.getLogger("com.atm");
        rootLogger.setLevel(Level.ALL);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(handler);
        configured = true;
    }
}
