package simulacion;

import util.Estadisticas;
import analitico.ResultadoAnaliticoMM1;
import modelo.Cliente;
import java.util.List;

public class ResultadoSimulacionMM1 {
    private final double Wq_sim;
    private final double W_sim;
    private final double Lq_sim;
    private final double L_sim;
    private final double utilizacion;
    private final int maxCola;
    private final double maxEspera;
    private final int clientesSinEspera;
    private final int N;
    private final List<Double> tiemposEspera;
    private final List<Double> tiemposEnSistema;
    private final List<Cliente> clientesCompletados;
    private final double tiempoTotalSimulacion; // Nuevo campo
    
    public ResultadoSimulacionMM1(double Wq_sim, double W_sim, double Lq_sim, double L_sim,
                                  double utilizacion, int maxCola, double maxEspera,
                                  int clientesSinEspera, int N,
                                  List<Double> tiemposEspera, List<Double> tiemposEnSistema,
                                  List<Cliente> clientesCompletados,
                                  double tiempoTotalSimulacion) { // Constructor actualizado
        this.Wq_sim = Wq_sim;
        this.W_sim = W_sim;
        this.Lq_sim = Lq_sim;
        this.L_sim = L_sim;
        this.utilizacion = utilizacion;
        this.maxCola = maxCola;
        this.maxEspera = maxEspera;
        this.clientesSinEspera = clientesSinEspera;
        this.N = N;
        this.tiemposEspera = tiemposEspera;
        this.tiemposEnSistema = tiemposEnSistema;
        this.clientesCompletados = clientesCompletados;
        this.tiempoTotalSimulacion = tiempoTotalSimulacion;
    }
    
    // Getters
    public double getWqSim() { return Wq_sim; }
    public double getWSim() { return W_sim; }
    public double getLqSim() { return Lq_sim; }
    public double getLSim() { return L_sim; }
    public double getUtilizacion() { return utilizacion; }
    public int getMaxCola() { return maxCola; }
    public double getMaxEspera() { return maxEspera; }
    public int getClientesSinEspera() { return clientesSinEspera; }
    public List<Cliente> getClientesCompletados() { return clientesCompletados; }
    public double getTiempoTotalSimulacion() { return tiempoTotalSimulacion; }

    public String[][] obtenerTablaDetallada() {
        String[][] tabla = new String[clientesCompletados.size()][11];
        for (int i = 0; i < clientesCompletados.size(); i++) {
            Cliente c = clientesCompletados.get(i);
            tabla[i][0] = String.valueOf(c.getId());
            tabla[i][1] = String.format("%.4f", c.getAleatorio1());
            tabla[i][2] = String.format("%.4f", c.getTiempoEntreLlegada());
            tabla[i][3] = String.format("%.4f", c.getTiempoLlegada());
            tabla[i][4] = String.format("%.4f", c.getTiempoInicioServicio());
            tabla[i][5] = String.format("%.4f", c.getTiempoEspera());
            tabla[i][6] = String.format("%.4f", c.getAleatorio2());
            tabla[i][7] = String.format("%.4f", c.getTiempoServicio());
            tabla[i][8] = String.format("%.4f", c.getTiempoFinServicio());
            tabla[i][9] = String.format("%.4f", c.getTiempoOcio());
            tabla[i][10] = String.format("%.4f", c.getTiempoEnSistema());
        }
        return tabla;
    }
    
    public void imprimirTablaDetallada() {
        System.out.println("\n========== TABLA DETALLADA M/M/1 ==========");
        System.out.println("CLIENTE#\tALEATORIO1\tTIEMPO_ENTRE_LLEGADA\tMOMENTO_LLEGADA\t" +
                          "TIEMPO_INICIO_SERVICIO\tTIEMPO_ESPERA\tALEATORIO2\t" +
                          "TIEMPO_SERVICIO\tTIEMPO_TERMINACION_SERVICIO\tTIEMPO_OCIO1\tTIEMPO_SISTEMA");
        String[][] tabla = obtenerTablaDetallada();
        for (String[] fila : tabla) {
            System.out.println(String.join("\t\t", fila));
        }
        System.out.println("==========================================\n");
    }
    
    public String generarReporte(ResultadoAnaliticoMM1 analitico) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== RESULTADOS SIMULACIÓN M/M/1 ==========\n");
        sb.append(String.format("Clientes simulados (post warm-up): %d\n", N));
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
        
        sb.append("\n--- MÉTRICAS ADICIONALES (SOLO SIMULACIÓN) ---\n");
        sb.append(String.format("Utilización del servidor: %.4f (%.2f%%)\n", utilizacion, utilizacion * 100));
        sb.append(String.format("Máxima longitud de cola (fase estable): %d clientes\n", maxCola));
        sb.append(String.format("Tiempo máximo de espera: %.4f unidades\n", maxEspera));
        sb.append(String.format("Clientes sin espera: %d (%.2f%%)\n",
            clientesSinEspera, (clientesSinEspera * 100.0 / N)));
        
        double[] ic95Wq = Estadisticas.calcularIC95(tiemposEspera);
        sb.append(String.format("IC 95%% para Wq: [%.4f, %.4f]\n", ic95Wq[0], ic95Wq[1]));
        
        sb.append("\n--- VALIDACIÓN (LEY DE LITTLE) ---\n");
        // CORRECCIÓN: Usar N / Tiempo Total Real
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