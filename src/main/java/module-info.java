module lucaluetolf.maturaarbeit_lucaluetolf {
    requires javafx.controls;
    requires javafx.fxml;
    requires kernel;
    requires layout;


    opens lucaluetolf.maturaarbeit_lucaluetolf to javafx.fxml;
    exports lucaluetolf.maturaarbeit_lucaluetolf;
}