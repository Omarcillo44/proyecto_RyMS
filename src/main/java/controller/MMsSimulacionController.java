package controller;

import analitico.ResultadoAnaliticoMMs;
import simulacion.ResultadoSimulacionMMs;
import simulacion.SimuladorMMs;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Estadisticas;
import modelo.Cliente; // Necesario para calcular IC y pasar datos a la tabla

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MMsSimulacionController {

    @FXML private Label lblLambdaHeredado, lblMuHeredado, lblSHeredado; //
    @FXML private TextField txtN, txtSemilla;
    @FXML private CheckBox chkUsarSemilla;
    @FXML private ProgressBar progressBar; //
    @FXML private Label lblProgreso;
    
    // Contenedores de resultados
    @FXML private VBox vboxComparacion, vboxMetricasAdicionales;
    @FXML private TableView<FilaComparacion> tableComparacion;
    @FXML private TableColumn<FilaComparacion, String> colMetrica, colTeorico, colSimulado, colError;

    // Sección de Utilización (Exclusivo M/M/s)
    @FXML private VBox vboxUtilizacion, vboxUtilizacionServidores; //
    @FXML private Label lblUtilizacionPromedio, lblBalanceCarga; //

    // Métricas Adicionales
    @FXML private Label lblMaxCola, lblMaxEspera, lblSinEspera, lblIC95;

    private ResultadoAnaliticoMMs analitico;
    private ResultadoSimulacionMMs simulacion;
    private double lambda, mu;
    private int s; // Variable para número de servidores

    // Método para inicializar datos desde la vista anterior (Analítico)
    public void initData(ResultadoAnaliticoMMs analitico, double lambda, double mu, int s) {
        this.analitico = analitico;
        this.lambda = lambda;
        this.mu = mu;
        this.s = s;
        
        lblLambdaHeredado.setText("λ = " + lambda);
        lblMuHeredado.setText("μ = " + mu);
        lblSHeredado.setText("s = " + s);
        
        // Configurar columnas de la tabla comparativa
        colMetrica.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().metrica));
        colTeorico.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().teorico));
        colSimulado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().simulado));
        colError.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().error));
        
        // Listener para activar/desactivar campo semilla
        chkUsarSemilla.selectedProperty().addListener((obs, oldVal, newVal) -> txtSemilla.setDisable(!newVal));
    }

    @FXML
    void ejecutarSimulacion(ActionEvent event) {
        try {
            int N = Integer.parseInt(txtN.getText());
            Long semilla = chkUsarSemilla.isSelected() ? Long.parseLong(txtSemilla.getText()) : null;

            // SUGERENCIA: Aumentar N automáticamente si es bajo para este rho
            if (N < 5000) {
                // Opcional: Avisar al usuario o simplemente calcular con más si él pone poco
                System.out.println("Sugerencia: Para rho ~0.9, usa N > 10,000");
            }

            // AJUSTE: Definir Warm-Up (ej. 20% de N)
            int warmUp = (int) (N * 0.2);

            // Ejecutar simulación con Warm-Up
            simulacion = SimuladorMMs.simular(lambda, mu, s, N, warmUp, semilla);

            mostrarResultados();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Revise los valores numéricos.");
        }
    }

    private void mostrarResultados() {
        // 1. Llenar tabla comparativa
        ObservableList<FilaComparacion> datos = FXCollections.observableArrayList(
            new FilaComparacion("Wq (Cola)", analitico.getWq(), simulacion.getWqSim()),
            new FilaComparacion("W (Sistema)", analitico.getW(), simulacion.getWSim()),
            new FilaComparacion("Lq (Cola)", analitico.getLq(), simulacion.getLqSim()),
            new FilaComparacion("L (Sistema)", analitico.getL(), simulacion.getLSim())
        );
        tableComparacion.setItems(datos);
        vboxComparacion.setVisible(true);

        // 2. Mostrar Utilización por Servidor
        vboxUtilizacionServidores.getChildren().clear();
        double[] utilizaciones = simulacion.getUtilizaciones();
        double sumaUtil = 0;
        double minUtil = utilizaciones[0];
        double maxUtil = utilizaciones[0];

        for (int i = 0; i < utilizaciones.length; i++) {
            double u = utilizaciones[i];
            Label lblServidor = new Label(String.format("Servidor %d: %.4f (%.2f%%)", (i + 1), u, u * 100));
            vboxUtilizacionServidores.getChildren().add(lblServidor);
            
            sumaUtil += u;
            if (u < minUtil) minUtil = u;
            if (u > maxUtil) maxUtil = u;
        }

        double promedio = sumaUtil / s;
        double balance = (maxUtil - minUtil) * 100;
        
        lblUtilizacionPromedio.setText(String.format("• Utilización promedio: %.4f (%.2f%%)", promedio, promedio * 100));
        lblBalanceCarga.setText(String.format("• Balance de carga: %.2f%%", balance));
        vboxUtilizacion.setVisible(true);

        // 3. Métricas adicionales
        lblMaxCola.setText("• Máxima longitud de cola: " + simulacion.getMaxCola());
        lblMaxEspera.setText(String.format("• Tiempo máximo de espera: %.4f", simulacion.getMaxEspera()));
        lblSinEspera.setText(String.format("• Clientes sin espera: %d", simulacion.getClientesSinEspera()));
        
        // Calcular IC95% de Wq usando la lista de clientes
        List<Double> tiemposEspera = simulacion.getClientesCompletados().stream()
                .map(Cliente::getTiempoEspera)
                .collect(Collectors.toList());
        double[] ic = Estadisticas.calcularIC95(tiemposEspera);
        lblIC95.setText(String.format("• IC 95%% para Wq: [%.4f, %.4f]", ic[0], ic[1]));
        
        vboxMetricasAdicionales.setVisible(true);
    }

    @FXML
    void verTablaDetallada(ActionEvent event) throws IOException {
        // Cargar vista de tabla detallada M/M/s
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MMsTablaDetallada.fxml"));
        Parent root = loader.load();

        MMsTablaDetalladaController controller = loader.getController();
        // Nota: Deberás crear el initData en MMsTablaDetalladaController para aceptar (List<Cliente>, int numServidores)
        controller.initData(simulacion.getClientesCompletados(), s); 

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    @FXML
    void volver(ActionEvent event) throws IOException {
        // Regresar a la vista analítica M/M/s
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MMsAnalitico.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.show();
    }

    // Clase auxiliar para la tabla
    public static class FilaComparacion {
        String metrica, teorico, simulado, error;
        public FilaComparacion(String m, double t, double s) {
            this.metrica = m;
            this.teorico = String.format("%.4f", t);
            this.simulado = String.format("%.4f", s);
            this.error = String.format("%.2f%%", Estadisticas.calcularErrorRelativo(t, s));
        }
    }
}