module com.atm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens com.atm to javafx.fxml;
    opens com.atm.controller to javafx.fxml;

    exports com.atm;
    exports com.atm.model;
    exports com.atm.service;
    exports com.atm.store;
    exports com.atm.ui;
    exports com.atm.util;
    exports com.atm.controller;
}
