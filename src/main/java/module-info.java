module module.name {
    exports se.lu.ics;
    exports se.lu.ics.controllers;

    opens se.lu.ics.controllers to javafx.fxml;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;

    requires java.sql;
}
