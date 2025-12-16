package controller;

import analitico.MM1Calculadora;
import analitico.ResultadoAnaliticoMM1;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.ValidadorParametros;

import java.io.IOException;
import java.util.Objects;

public class MM1AnaliticoController {

    @FXML private TextField txtLambda, txtMu, txtK, txtT;
    @FXML private CheckBox chkCalcularK, chkCalcularT;
    @FXML private VBox vboxResultados;
    @FXML private Label lblEstado, lblRho, lblP0, lblLq, lblL, lblWq, lblW, lblPw;
    @FXML private Label lblPnMasK, lblTituloPnMasK, lblPWqMayorT, lblTituloPWqMayorT, lblPWsMayorT, lblTituloPWsMayorT;

    private ResultadoAnaliticoMM1 resultadoActual;
    private double lambda, mu;

    @FXML
    void calcular(ActionEvent event) {
        try {
            lambda = Double.parseDouble(txtLambda.getText());
            mu = Double.parseDouble(txtMu.getText());
            
            Double k = chkCalcularK.isSelected() ? Double.parseDouble(txtK.getText()) : null;
            Double t = chkCalcularT.isSelected() ? Double.parseDouble(txtT.getText()) : null;

            if (ValidadorParametros.validarMM1(lambda, mu)) {
                resultadoActual = MM1Calculadora.calcular(lambda, mu, k, t);
                mostrarResultados(resultadoActual);
            } else {
                mostrarAlerta("Parámetros inválidos", "Revise que λ < μ y sean positivos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Por favor ingrese números válidos.");
        }
    }

    private void mostrarResultados(ResultadoAnaliticoMM1 r) {
        lblEstado.setText(r.isEstable() ? "ESTABLE" : "INESTABLE");
        lblRho.setText(String.format("%.4f", r.getRho()));
        lblP0.setText(String.format("%.4f", r.getP0()));
        lblLq.setText(String.format("%.4f", r.getLq()));
        lblL.setText(String.format("%.4f", r.getL()));
        lblWq.setText(String.format("%.4f", r.getWq()));
        lblW.setText(String.format("%.4f", r.getW()));
        lblPw.setText(String.format("%.4f", r.getPw()));

        // Opcionales
        if (r.getPNMasK() != null) {
            lblTituloPnMasK.setVisible(true);
            lblPnMasK.setVisible(true);
            lblPnMasK.setText(String.format("%.4f", r.getPNMasK()));
        }
        
        // ... (Configurar visibilidad de T de forma similar)

        vboxResultados.setVisible(true);
    }

    @FXML
    void irSimulacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MM1Simulacion.fxml"));
        Parent root = loader.load();

        // Pasar datos al controlador de simulación
        MM1SimulacionController controller = loader.getController();
        controller.initData(resultadoActual, lambda, mu);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);
        stage.setScene(new Scene(root));
    }
    
    @FXML
    void volver(ActionEvent event) throws IOException {
        // Lógica para regresar al Menu
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/omarcisho/proyecto_ryms/MenuPrincipal.fxml")));
        ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root));
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}