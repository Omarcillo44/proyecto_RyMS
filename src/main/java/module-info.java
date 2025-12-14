module com.omarcisho.proyecto_ryms {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.omarcisho.proyecto_ryms to javafx.fxml;
    exports com.omarcisho.proyecto_ryms;
}