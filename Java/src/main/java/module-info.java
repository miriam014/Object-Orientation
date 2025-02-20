module smu.Progetto.OO {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires org.postgresql.jdbc;

    opens smu to javafx.graphics, javafx.fxml;
    opens smu.Controller to javafx.fxml;
    opens smu.DTO to javafx.base;
    exports smu;
}
