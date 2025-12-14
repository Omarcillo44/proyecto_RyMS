// Archivo: ResultadoSimulacionMM1.java
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
    
    public ResultadoSimulacionMM1(double Wq_sim, double W_sim, double Lq_sim, double L_sim,
                                  double utilizacion, int maxCola, double maxEspera,
                                  int clientesSinEspera, int N,
                                  List<Double> tiemposEspera, List<Double> tiemposEnSistema,
                                  List<Cliente> clientesCompletados) {
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
    
    /**
     * Genera tabla detallada para JavaFX (por ahora en consola)
     * Formato: CLIENTE# | ALEATORIO1 | TIEMPO_ENTRE_LLEGADA | MOMENTO_LLEGADA | 
     *          TIEMPO_INICIO_SERVICIO | TIEMPO_ESPERA | ALEATORIO2 | TIEMPO_SERVICIO | 
     *          TIEMPO_TERMINACION_SERVICIO | TIEMPO_OCIO1 | TIEMPO_SISTEMA
     */
    public String[][] obtenerTablaDetallada() {
        String[][] tabla = new String[clientesCompletados.size()][11];
        
        for (int i = 0; i < clientesCompletados.size(); i++) {
            Cliente c = clientesCompletados.get(i);
            
            tabla[i][0] = String.valueOf(c.getId()); // Cliente#
            tabla[i][1] = String.format("%.4f", c.getAleatorio1()); // Aleatorio 1
            tabla[i][2] = String.format("%.4f", c.getTiempoEntreLlegada()); // Tiempo entre llegada
            tabla[i][3] = String.format("%.4f", c.getTiempoLlegada()); // Momento de llegada
            tabla[i][4] = String.format("%.4f", c.getTiempoInicioServicio()); // Tiempo inicio servicio
            tabla[i][5] = String.format("%.4f", c.getTiempoEspera()); // Tiempo espera
            tabla[i][6] = String.format("%.4f", c.getAleatorio2()); // Aleatorio 2
            tabla[i][7] = String.format("%.4f", c.getTiempoServicio()); // Tiempo servicio
            tabla[i][8] = String.format("%.4f", c.getTiempoFinServicio()); // Tiempo terminación servicio
            tabla[i][9] = String.format("%.4f", c.getTiempoOcio()); // Tiempo ocio
            tabla[i][10] = String.format("%.4f", c.getTiempoEnSistema()); // Tiempo en sistema
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
        sb.append(String.format("Clientes simulados: %d\n", N));
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
        sb.append(String.format("Máxima longitud de cola: %d clientes\n", maxCola));
        sb.append(String.format("Tiempo máximo de espera: %.4f unidades\n", maxEspera));
        sb.append(String.format("Clientes sin espera: %d (%.2f%%)\n",
            clientesSinEspera, (clientesSinEspera * 100.0 / N)));
        
        double[] ic95Wq = Estadisticas.calcularIC95(tiemposEspera);
        sb.append(String.format("IC 95%% para Wq: [%.4f, %.4f]\n", ic95Wq[0], ic95Wq[1]));
        
        sb.append("\n--- VALIDACIÓN (LEY DE LITTLE) ---\n");
        double lambdaEfectiva = N / tiemposEnSistema.get(tiemposEnSistema.size()-1);
        double L_little = lambdaEfectiva * W_sim;
        sb.append(String.format("L (simulado): %.4f\n", L_sim));
        sb.append(String.format("λ·W (Ley de Little): %.4f\n", L_little));
        sb.append(String.format("Diferencia: %.4f%%\n",
            Estadisticas.calcularErrorRelativo(L_sim, L_little)));
        
        sb.append("================================================\n");
        return sb.toString();
    }
}