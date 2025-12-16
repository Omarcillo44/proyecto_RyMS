package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Cliente;

import java.io.IOException;
import java.util.List;

public class MMsTablaDetalladaController {

    @FXML private Label lblNumClientes, lblNumServidores, lblTituloTabla;
    @FXML private TableView<Cliente> tableDetallada;

    // initData recibe la lista de clientes y la cantidad de servidores
    public void initData(List<Cliente> clientes, int numServidores) {
        lblNumClientes.setText("Clientes simulados: " + clientes.size());
        lblNumServidores.setText("Servidores: " + numServidores);

        // Limpiar columnas previas si las hubiera
        tableDetallada.getColumns().clear();

        // --- 1. Columnas Fijas (Generales) ---
        agregarColumnaEstatica("Cliente #", "id");
        agregarColumnaEstatica("Aleatorio 1", "aleatorio1");
        agregarColumnaEstatica("Tiempo Entre Llegadas", "tiempoEntreLlegada");
        agregarColumnaEstatica("Momento Llegada", "tiempoLlegada");

        // --- 2. Columnas Dinámicas por Servidor ---
        for (int i = 0; i < numServidores; i++) {
            final int indexServidor = i; // Necesario para usar dentro de lambdas
            int numeroServidorVis = i + 1; // Para mostrar "Servidor 1" en lugar de 0

            agregarColumnaDinamica("Inicio S" + numeroServidorVis, c -> 
                (c.getServidorAsignado() == indexServidor) ? String.format("%.4f", c.getTiempoInicioServicio()) : "-");

            agregarColumnaDinamica("Espera S" + numeroServidorVis, c -> 
                (c.getServidorAsignado() == indexServidor) ? String.format("%.4f", c.getTiempoEspera()) : "-");
            
            // Nota: Asumimos que aleatorio2 solo es relevante para el servidor asignado
            agregarColumnaDinamica("Servicio S" + numeroServidorVis, c -> 
                (c.getServidorAsignado() == indexServidor) ? String.format("%.4f", c.getTiempoServicio()) : "-");
            
            agregarColumnaDinamica("Fin S" + numeroServidorVis, c -> 
                (c.getServidorAsignado() == indexServidor) ? String.format("%.4f", c.getTiempoFinServicio()) : "-");
                
            agregarColumnaDinamica("Ocio S" + numeroServidorVis, c -> 
                (c.getServidorAsignado() == indexServidor) ? String.format("%.4f", c.getTiempoOcio()) : "-");
        }

        // --- 3. Columnas Fijas Finales ---
        agregarColumnaEstatica("Tiempo Sistema", "tiempoEnSistema");

        // Cargar datos
        tableDetallada.setItems(FXCollections.observableArrayList(clientes));
    }

    // Helper para columnas simples mapeadas directamente a propiedades
    private void agregarColumnaEstatica(String titulo, String propiedad) {
        TableColumn<Cliente, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setMinWidth(100);
        tableDetallada.getColumns().add(col);
    }

    // Helper para columnas calculadas (donde decidimos si mostrar el valor o un guion)
    private void agregarColumnaDinamica(String titulo, java.util.function.Function<Cliente, String> mapper) {
        TableColumn<Cliente, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(data -> new SimpleStringProperty(mapper.apply(data.getValue())));
        col.setMinWidth(90);
        tableDetallada.getColumns().add(col);
    }
    
    @FXML
    void volver(ActionEvent event) throws IOException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MMsSimulacion.fxml"));
        Parent root = loader.load();
        
        // Aquí no podemos restaurar el estado completo fácilmente sin pasar parámetros de vuelta,
        // pero idealmente volverías a la vista anterior.
        // Por simplicidad en este paso, regresamos al menú o a la vista de simulación (recargándola vacía o manejando sesión).
        // *Recomendación:* Simplemente cierra la ventana o usa un 'back' navigation si tienes un gestor de escenas global.
        // Ojo: Si recargas MMsSimulacion.fxml aquí, perderás los datos de la simulación actual.
        // Lo mejor es guardar la escena anterior en el controlador principal o simplemente volver al menú.
        
        // Opción segura: Volver al menú o a la pantalla de parámetros M/M/s
        Parent menu = FXMLLoader.load(getClass().getResource("/com/omarcisho/proyecto_ryms/MMsAnalitico.fxml")); 
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);
        stage.setScene(new Scene(menu));
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        System.out.println("Funcionalidad de exportar pendiente...");
    }
    
    @FXML
    void imprimir(ActionEvent event) {
        System.out.println("Funcionalidad de imprimir pendiente...");
    }
}