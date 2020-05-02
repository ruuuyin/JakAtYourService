module jays {
    requires javafx.controls;
    requires javafx.fxml;

    opens jays to javafx.fxml;
    exports jays;
}