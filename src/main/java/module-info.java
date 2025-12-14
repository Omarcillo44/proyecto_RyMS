module com.omarcisho.proyecto_ryms {
    requires javafx.controls;
    requires javafx.fxml;

    // Abrir paquetes a JavaFX
    opens com.omarcisho.proyecto_ryms to javafx.fxml;
    opens controller to javafx.fxml;
    opens modelo to javafx.base; // Vital para las TableView

    exports com.omarcisho.proyecto_ryms;
}