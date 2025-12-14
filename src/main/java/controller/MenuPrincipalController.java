package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    void abrirMM1(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/omarcisho/proyecto_ryms/MM1Analitico.fxml");
    }

    @FXML
    void abrirMMs(ActionEvent event) throws IOException {
        cambiarEscena(event, "/com/omarcisho/proyecto_ryms/MMsAnalitico.fxml");
    }

    @FXML
    void salir(ActionEvent event) {
        System.exit(0);
    }

    private void cambiarEscena(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}