// FORSE NON SERVE
module smu.Progetto.OO {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires org.postgresql.jdbc;

    opens smu.Controller to javafx.fxml;
}
