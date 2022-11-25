module com.libra.ptcgt.ptcgtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires json.simple;

    opens com.libra.ptcgt.ptcgtool to javafx.fxml;
    exports com.libra.ptcgt.ptcgtool;
    exports com.libra.ptcgt.ptcgtool.controllers;
}