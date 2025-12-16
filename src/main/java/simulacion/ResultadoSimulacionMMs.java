package simulacion;

import util.Estadisticas;
import analitico.ResultadoAnaliticoMMs;
import modelo.Cliente;
import java.util.List;

public class ResultadoSimulacionMMs {
    private final double Wq_sim;
    private final double W_sim;
    private final double Lq_sim;
    private final double L_sim;
    private final double[] utilizaciones;
    private final int maxCola;
    private final double maxEspera;
    private final int clientesSinEspera;
    private final int N;
    private final List<Double> tiemposEspera;
    private final List<Double> tiemposEnSistema;
    private final List<Cliente> clientesCompletados;
    private final int numServidores;
    private final double tiempoTotalSimulacion; // Nuevo campo para la validación precisa
    
    public ResultadoSimulacionMMs(double Wq_sim, double W_sim, double Lq_sim, double L_sim,
                                  double[] utilizaciones, int maxCola, double maxEspera,
                                  int clientesSinEspera, int N,
                                  List<Double> tiemposEspera, List<Double> tiemposEnSistema,
                                  List<Cliente> clientesCompletados,
                                  double tiempoTotalSimulacion) { // Nuevo parámetro en constructor
        this.Wq_sim = Wq_sim;
        this.W_sim = W_sim;
        this.Lq_sim = Lq_sim;
        this.L_sim = L_sim;
        this.utilizaciones = utilizaciones;
        this.maxCola = maxCola;
        this.maxEspera = maxEspera;
        this.clientesSinEspera = clientesSinEspera;
        this.N = N;
        this.tiemposEspera = tiemposEspera;
        this.tiemposEnSistema = tiemposEnSistema;
        this.clientesCompletados = clientesCompletados;
        this.numServidores = utilizaciones.length;
        this.tiempoTotalSimulacion = tiempoTotalSimulacion;
    }
    
    // Getters
    public double getWqSim() { return Wq_sim; }
    public double getWSim() { return W_sim; }
    public double getLqSim() { return Lq_sim; }
    public double getLSim() { return L_sim; }
    public double[] getUtilizaciones() { return utilizaciones; }
    public List<Cliente> getClientesCompletados() { return clientesCompletados; }
    public double getTiempoTotalSimulacion() { return tiempoTotalSimulacion; }
    // Getters necesarios para el controlador JavaFX (que faltaban antes)
    public int getMaxCola() { return maxCola; }
    public double getMaxEspera() { return maxEspera; }
    public int getClientesSinEspera() { return clientesSinEspera; }
    
    public String[][] obtenerTablaDetallada() {
        // Cálculo de columnas dinámicas según s
        int numColumnas = 4 + (5 * numServidores) + 1;
        String[][] tabla = new String[clientesCompletados.size()][numColumnas];
        
        for (int i = 0; i < clientesCompletados.size(); i++) {
            Cliente c = clientesCompletados.get(i);
            int col = 0;
            
            // Datos fijos del cliente
            tabla[i][col++] = String.valueOf(c.getId());
            tabla[i][col++] = String.format("%.4f", c.getAleatorio1());
            tabla[i][col++] = String.format("%.4f", c.getTiempoEntreLlegada());
            tabla[i][col++] = String.format("%.4f", c.getTiempoLlegada());
            
            // Datos por servidor (Inicio, Espera, Servicio, Fin, Ocio)
            for (int s = 0; s < numServidores; s++) {
                tabla[i][col++] = (c.getServidorAsignado() == s) ? String.format("%.4f", c.getTiempoInicioServicio()) : "-";
            }
            for (int s = 0; s < numServidores; s++) {
                tabla[i][col++] = (c.getServidorAsignado() == s) ? String.format("%.4f", c.getTiempoEspera()) : "-";
            }
            for (int s = 0; s < numServidores; s++) {
                tabla[i][col++] = (c.getServidorAsignado() == s) ? String.format("%.4f", c.getTiempoServicio()) : "-";
            }
            for (int s = 0; s < numServidores; s++) {
                tabla[i][col++] = (c.getServidorAsignado() == s) ? String.format("%.4f", c.getTiempoFinServicio()) : "-";
            }
            for (int s = 0; s < numServidores; s++) {
                tabla[i][col++] = (c.getServidorAsignado() == s) ? String.format("%.4f", c.getTiempoOcio()) : "-";
            }
            
            // Dato final
            tabla[i][col] = String.format("%.4f", c.getTiempoEnSistema());
        }
        
        return tabla;
    }
    
    public void imprimirTablaDetallada() {
        // Método de utilidad para consola
        System.out.println("\n========== TABLA DETALLADA M/M/s ==========");
        System.out.print("CAMION_No\tALEATORIO\tTIEMPO_ENTRE_LLEGADA\tTIEMPO_LLEGADA\t");
        for (int s = 1; s <= numServidores; s++) System.out.print("INICIO_S" + s + "\t");
        for (int s = 1; s <= numServidores; s++) System.out.print("ESPERA_S" + s + "\t");
        for (int s = 1; s <= numServidores; s++) System.out.print("SERVICIO_S" + s + "\t");
        for (int s = 1; s <= numServidores; s++) System.out.print("FIN_S" + s + "\t");
        for (int s = 1; s <= numServidores; s++) System.out.print("OCIO_S" + s + "\t");
        System.out.println("TIEMPO_SISTEMA");
        
        String[][] tabla = obtenerTablaDetallada();
        for (String[] fila : tabla) {
            for (String celda : fila) {
                System.out.print(celda + "\t");
            }
            System.out.println();
        }
        System.out.println("==========================================\n");
    }
    
    public String generarReporte(ResultadoAnaliticoMMs analitico) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== RESULTADOS SIMULACIÓN M/M/s ==========\n");
        sb.append(String.format("Clientes útiles (post warm-up): %d\n", N));
        sb.append(String.format("Servidores: %d\n", utilizaciones.length));
        
        sb.append("\n--- MÉTRICAS PRINCIPALES ---\n");
        sb.append(String.format("%-35s %10.4f  (Teórico: %.4f, Error: %.2f%%)\n",
            "Tiempo promedio en cola (Wq):", Wq_sim, analitico.getWq(),
            Estadisticas.calcularErrorRelativo(analitico.getWq(), Wq_sim)));
        sb.append(String.format("%-35s %10.4f  (Teórico: %.4f, Error: %.2f%%)\n",
            "Tiempo promedio en sistema (W):", W_sim, analitico.getW(),
            Estadisticas.calcularErrorRelativo(analitico.getW(), W_sim)));
        sb.append(String.format("%-35s %10.4f  (Teórico: %.4f, Error: %.2f%%)\n",
            "Número promedio en cola (Lq):", Lq_sim, analitico.getLq(),
            Estadisticas.calcularErrorRelativo(analitico.getLq(), Lq_sim)));
        sb.append(String.format("%-35s %10.4f  (Teórico: %.4f, Error: %.2f%%)\n",
            "Número promedio en sistema (L):", L_sim, analitico.getL(),
            Estadisticas.calcularErrorRelativo(analitico.getL(), L_sim)));
        
        sb.append("\n--- UTILIZACIÓN POR SERVIDOR ---\n");
        double sumaUtil = 0.0;
        double minUtil = utilizaciones[0];
        double maxUtil = utilizaciones[0];
        for (int i = 0; i < utilizaciones.length; i++) {
            sb.append(String.format("Servidor %d: %.4f (%.2f%%)\n",
                i + 1, utilizaciones[i], utilizaciones[i] * 100));
            sumaUtil += utilizaciones[i];
            if (utilizaciones[i] < minUtil) minUtil = utilizaciones[i];
            if (utilizaciones[i] > maxUtil) maxUtil = utilizaciones[i];
        }
        double utilizacionPromedio = sumaUtil / utilizaciones.length;
        double balanceCarga = (maxUtil - minUtil) * 100;
        sb.append(String.format("Utilización promedio: %.4f (%.2f%%)\n",
            utilizacionPromedio, utilizacionPromedio * 100));
        sb.append(String.format("Balance de carga: %.2f%%\n", balanceCarga));
        
        sb.append("\n--- MÉTRICAS ADICIONALES ---\n");
        sb.append(String.format("Máxima longitud de cola (fase estable): %d clientes\n", maxCola));
        sb.append(String.format("Tiempo máximo de espera: %.4f unidades\n", maxEspera));
        sb.append(String.format("Clientes sin espera: %d (%.2f%%)\n",
            clientesSinEspera, (clientesSinEspera * 100.0 / N)));
        
        double[] ic95Wq = Estadisticas.calcularIC95(tiemposEspera);
        sb.append(String.format("IC 95%% para Wq: [%.4f, %.4f]\n", ic95Wq[0], ic95Wq[1]));
        
        // CORRECCIÓN FINAL: Ley de Little usando el tiempo exacto de la fase estable
        sb.append("\n--- VALIDACIÓN (LEY DE LITTLE) ---\n");
        double lambdaEfectiva = N / tiempoTotalSimulacion;
        double L_little = lambdaEfectiva * W_sim;
        
        sb.append(String.format("L (simulado): %.4f\n", L_sim));
        sb.append(String.format("λ efect·W (Ley de Little): %.4f\n", L_little));
        sb.append(String.format("Diferencia: %.4f%%\n",
            Estadisticas.calcularErrorRelativo(L_sim, L_little)));
        
        sb.append("================================================\n");
        return sb.toString();
    }
}