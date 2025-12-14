package controller;

import analitico.ResultadoAnaliticoMM1;
import simulacion.ResultadoSimulacionMM1;
import simulacion.SimuladorMM1;
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

import java.io.IOException;

public class MM1SimulacionController {

    @FXML private Label lblLambdaHeredado, lblMuHeredado, lblProgreso;
    @FXML private TextField txtN, txtSemilla;
    @FXML private CheckBox chkUsarSemilla;
    @FXML private ProgressBar progressBar;
    @FXML private VBox vboxComparacion, vboxMetricasAdicionales;
    @FXML private TableView<FilaComparacion> tableComparacion;
    @FXML private TableColumn<FilaComparacion, String> colMetrica, colTeorico, colSimulado, colError;
    
    // Labels adicionales
    @FXML private Label lblMaxCola, lblMaxEspera, lblSinEspera, lblIC95;

    private ResultadoAnaliticoMM1 analitico;
    private ResultadoSimulacionMM1 simulacion;
    private double lambda, mu;

    // Método para recibir datos del controlador anterior
    public void initData(ResultadoAnaliticoMM1 analitico, double lambda, double mu) {
        this.analitico = analitico;
        this.lambda = lambda;
        this.mu = mu;
        
        lblLambdaHeredado.setText("λ = " + lambda);
        lblMuHeredado.setText("μ = " + mu);
        
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

            if (N < 100) {
                mostrarAlerta("Advertencia", "N es muy bajo, los resultados no convergerán.");
            }

            // Ejecutar lógica
            simulacion = SimuladorMM1.simular(lambda, mu, N, semilla);
            
            mostrarResultados();
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Revise los valores numéricos.");
        }
    }

    private void mostrarResultados() {
        // Llenar tabla comparativa
        ObservableList<FilaComparacion> datos = FXCollections.observableArrayList(
            new FilaComparacion("Wq (Cola)", analitico.getWq(), simulacion.getWqSim()),
            new FilaComparacion("W (Sistema)", analitico.getW(), simulacion.getWSim()),
            new FilaComparacion("Lq (Cola)", analitico.getLq(), simulacion.getLqSim()),
            new FilaComparacion("L (Sistema)", analitico.getL(), simulacion.getLSim())
        );
        tableComparacion.setItems(datos);
        vboxComparacion.setVisible(true);

        // Métricas adicionales
        lblMaxCola.setText("• Máxima longitud de cola: " + simulacion.getMaxCola());
        lblMaxEspera.setText(String.format("• Tiempo máximo de espera: %.4f", simulacion.getMaxEspera()));
        lblSinEspera.setText(String.format("• Clientes sin espera: %d", simulacion.getClientesSinEspera()));
        double[] ic = Estadisticas.calcularIC95(simulacion.getClientesCompletados().stream().map(c -> c.getTiempoEspera()).toList()); // Nota: tuve que adaptar esto, idealmente simulacion retorna la lista de tiempos directa o el array
        // Nota: En tu código original simulacion.tiemposEspera es private y no tiene getter directo salvo dentro de generarReporte. 
        // Asumo que agregaste getters públicos en ResultadoSimulacionMM1 para las listas o el IC.
        
        vboxMetricasAdicionales.setVisible(true);
    }

    @FXML
    void verTablaDetallada(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MM1TablaDetallada.fxml"));
        Parent root = loader.load();

        MM1TablaDetalladaController controller = loader.getController();
        controller.initData(simulacion.getClientesCompletados()); // Pasar lista de clientes

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
    
    @FXML
    void volver(ActionEvent event) throws IOException {
         // Cargar FXML de MM1Analitico y restaurar estado si es necesario
         // O simplemente volver atrás
         // ... Similar a otros métodos volver
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.show();
    }

    // Clase auxiliar interna para la tabla
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