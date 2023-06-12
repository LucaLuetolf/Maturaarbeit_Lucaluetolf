module lucaluetolf.maturaarbeit_lucaluetolf {
    requires javafx.controls;
    requires javafx.fxml;
    requires kernel;
    requires layout;
    requires io;
    requires java.sql;
    requires com.jfoenix;
    requires java.desktop;

    opens lucaluetolf.maturaarbeit_lucaluetolf to javafx.fxml;
    exports lucaluetolf.maturaarbeit_lucaluetolf;
}