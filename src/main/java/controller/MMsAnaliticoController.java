package controller;

import analitico.MMsCalculadora;
import analitico.ResultadoAnaliticoMMs;
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

public class MMsAnaliticoController {

    @FXML private TextField txtLambda, txtMu, txtS, txtK, txtT;
    @FXML private CheckBox chkCalcularK, chkCalcularT;
    @FXML private VBox vboxResultados;
    
    // Etiquetas de resultados (IDs coinciden con MMsAnalitico.fxml)
    @FXML private Label lblEstado, lblRho, lblA, lblP0, lblC, lblLq, lblL, lblWq, lblW, lblU;
    @FXML private Label lblPnMasK, lblTituloPnMasK, lblPWqMayorT, lblTituloPWqMayorT, lblPWsMayorT, lblTituloPWsMayorT;

    private ResultadoAnaliticoMMs resultadoActual;
    private double lambda, mu;
    private int s;

    @FXML
    void calcular(ActionEvent event) {
        try {
            lambda = Double.parseDouble(txtLambda.getText());
            mu = Double.parseDouble(txtMu.getText());
            s = Integer.parseInt(txtS.getText());
            
            Double k = chkCalcularK.isSelected() ? Double.parseDouble(txtK.getText()) : null;
            Double t = chkCalcularT.isSelected() ? Double.parseDouble(txtT.getText()) : null;

            // Validar usando la utilidad existente
            if (ValidadorParametros.validarMMs(lambda, mu, s)) {
                resultadoActual = MMsCalculadora.calcular(lambda, mu, s, k, t);
                mostrarResultados(resultadoActual);
            } else {
                mostrarAlerta("Parámetros inválidos", "Verifique que λ < s·μ y s >= 1.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Por favor ingrese números válidos.");
        }
    }

    private void mostrarResultados(ResultadoAnaliticoMMs r) {
        lblEstado.setText(r.isEstable() ? "ESTABLE" : "INESTABLE");
        lblRho.setText(String.format("%.4f", r.getRho()));
        lblA.setText(String.format("%.4f", r.getA())); // Tráfico ofrecido
        lblP0.setText(String.format("%.4f", r.getP0()));
        lblC.setText(String.format("%.4f", r.getC())); // Erlang C
        lblLq.setText(String.format("%.4f", r.getLq()));
        lblL.setText(String.format("%.4f", r.getL()));
        lblWq.setText(String.format("%.4f", r.getWq()));
        lblW.setText(String.format("%.4f", r.getW()));
        lblU.setText(String.format("%.4f", r.getU())); // Utilización

        // Resultados opcionales
        if (r.getPNMasK() != null) {
            lblTituloPnMasK.setVisible(true);
            lblPnMasK.setVisible(true);
            lblPnMasK.setText(String.format("%.4f", r.getPNMasK()));
        } else {
            lblTituloPnMasK.setVisible(false);
            lblPnMasK.setVisible(false);
        }
        
        if (r.getPWqMayorT() != null) {
            lblTituloPWqMayorT.setVisible(true);
            lblTituloPWsMayorT.setVisible(true);
            lblPWqMayorT.setVisible(true);
            lblPWsMayorT.setVisible(true);
            lblPWqMayorT.setText(String.format("%.4f", r.getPWqMayorT()));
            lblPWsMayorT.setText(String.format("%.4f", r.getPWsMayorT()));
        } else {
            lblTituloPWqMayorT.setVisible(false);
            lblTituloPWsMayorT.setVisible(false);
            lblPWqMayorT.setVisible(false);
            lblPWsMayorT.setVisible(false);
        }

        vboxResultados.setVisible(true);
    }

    @FXML
    void irSimulacion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MMsSimulacion.fxml"));
        Parent root = loader.load();

        // Pasar datos al controlador de simulación M/M/s
        MMsSimulacionController controller = loader.getController();
        controller.initData(resultadoActual, lambda, mu, s);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);

        stage.setScene(new Scene(root));
    }
    
    @FXML
    void volver(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/omarcisho/proyecto_ryms/MenuPrincipal.fxml"));
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