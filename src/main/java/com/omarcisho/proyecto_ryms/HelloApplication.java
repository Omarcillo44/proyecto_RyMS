package com.omarcisho.proyecto_ryms;

import analitico.*;
import simulacion.*;
import util.ValidadorParametros;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Carga la vista del Menú Principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sistema de Colas - UPIICSA");
        // stage.setMaximized(true); // Opcional
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {

        launch();

    }
}