module com.example.monopoly2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;


    opens com.example.monopoly2 to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.monopoly2;
}