package controller;

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

public class MM1TablaDetalladaController {

    @FXML private Label lblNumClientes;
    @FXML private TableView<Cliente> tableDetallada;
    
    // Asegúrate de que los fx:id coincidan con el FXML
    @FXML private TableColumn<Cliente, Integer> colCliente;
    @FXML private TableColumn<Cliente, Double> colAleatorio1, colTiempoEntreLlegada, colMomentoLlegada;
    @FXML private TableColumn<Cliente, Double> colTiempoInicioServicio, colTiempoEspera, colAleatorio2;
    @FXML private TableColumn<Cliente, Double> colTiempoServicio, colTiempoTerminacion, colTiempoOcio, colTiempoSistema;

    public void initData(List<Cliente> clientes) {
        lblNumClientes.setText("Clientes simulados: " + clientes.size());

        // Mapeo con los getters de la clase Cliente (ej. getAleatorio1 -> "aleatorio1")
        colCliente.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAleatorio1.setCellValueFactory(new PropertyValueFactory<>("aleatorio1"));
        colTiempoEntreLlegada.setCellValueFactory(new PropertyValueFactory<>("tiempoEntreLlegada"));
        colMomentoLlegada.setCellValueFactory(new PropertyValueFactory<>("tiempoLlegada"));
        colTiempoInicioServicio.setCellValueFactory(new PropertyValueFactory<>("tiempoInicioServicio"));
        colTiempoEspera.setCellValueFactory(new PropertyValueFactory<>("tiempoEspera"));
        colAleatorio2.setCellValueFactory(new PropertyValueFactory<>("aleatorio2"));
        colTiempoServicio.setCellValueFactory(new PropertyValueFactory<>("tiempoServicio"));
        colTiempoTerminacion.setCellValueFactory(new PropertyValueFactory<>("tiempoFinServicio"));
        colTiempoOcio.setCellValueFactory(new PropertyValueFactory<>("tiempoOcio"));
        colTiempoSistema.setCellValueFactory(new PropertyValueFactory<>("tiempoEnSistema"));

        tableDetallada.setItems(FXCollections.observableArrayList(clientes));
    }

    @FXML
    void volver(ActionEvent event) throws IOException {
        // Regresamos a la vista Analítica para reiniciar el flujo correctamente
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omarcisho/proyecto_ryms/MM1Analitico.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMaximized(true);
        stage.setScene(new Scene(root));
    }

    @FXML
    void exportarCSV(ActionEvent event) {
        // Implementar lógica de exportación si se requiere
    }
    
    @FXML
    void imprimir(ActionEvent event) {
        // Implementar lógica de impresión
    }
}