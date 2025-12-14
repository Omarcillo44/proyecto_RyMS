package analitico;

public class ResultadoAnaliticoMM1 {
    private final double lambda;
    private final double mu;
    private final double rho;
    private final double P0;
    private final double Lq;
    private final double L;
    private final double Wq;
    private final double W;
    private final double Pw;
    private final Double P_n_mas_k;
    private final Double P_Wq_mayor_t;
    private final Double P_Ws_mayor_t;
    
    public ResultadoAnaliticoMM1(double lambda, double mu, double rho, double P0, double Lq, double L, 
                                 double Wq, double W, double Pw,
                                 Double P_n_mas_k, Double P_Wq_mayor_t, Double P_Ws_mayor_t) {
        this.lambda = lambda;
        this.mu = mu;
        this.rho = rho;
        this.P0 = P0;
        this.Lq = Lq;
        this.L = L;
        this.Wq = Wq;
        this.W = W;
        this.Pw = Pw;
        this.P_n_mas_k = P_n_mas_k;
        this.P_Wq_mayor_t = P_Wq_mayor_t;
        this.P_Ws_mayor_t = P_Ws_mayor_t;
    }
    
    // Getters
    public double getLambda() { return lambda; }
    public double getMu() { return mu; }
    public double getRho() { return rho; }
    public double getP0() { return P0; }
    public double getLq() { return Lq; }
    public double getL() { return L; }
    public double getWq() { return Wq; }
    public double getW() { return W; }
    public double getPw() { return Pw; }
    public Double getPNMasK() { return P_n_mas_k; }
    public Double getPWqMayorT() { return P_Wq_mayor_t; }
    public Double getPWsMayorT() { return P_Ws_mayor_t; }
    
    public boolean isEstable() { return rho < 1.0; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== RESULTADOS ANALÍTICOS M/M/1 ==========\n");
        sb.append(String.format("Estado del sistema: %s\n", isEstable() ? "ESTABLE" : "INESTABLE"));
        sb.append(String.format("Intensidad de tráfico (ρ): %.4f\n", rho));
        sb.append(String.format("Probabilidad sistema vacío (P₀): %.4f (%.2f%%)\n", P0, P0 * 100));
        sb.append(String.format("Número promedio en la cola (Lq): %.4f clientes\n", Lq));
        sb.append(String.format("Número promedio en el sistema (L): %.4f clientes\n", L));
        sb.append(String.format("Tiempo promedio en la cola (Wq): %.4f unidades\n", Wq));
        sb.append(String.format("Tiempo promedio en el sistema (W): %.4f unidades\n", W));
        sb.append(String.format("Probabilidad de esperar (Pw): %.4f (%.2f%%)\n", Pw, Pw * 100));
        
        if (P_n_mas_k != null) {
            sb.append(String.format("P(n > k): %.4f (%.2f%%)\n", P_n_mas_k, P_n_mas_k * 100));
        }
        if (P_Wq_mayor_t != null) {
            sb.append(String.format("P(Wq > t): %.4f (%.2f%%)\n", P_Wq_mayor_t, P_Wq_mayor_t * 100));
        }
        if (P_Ws_mayor_t != null) {
            sb.append(String.format("P(Ws > t): %.4f (%.2f%%)\n", P_Ws_mayor_t, P_Ws_mayor_t * 100));
        }
        sb.append("================================================\n");
        return sb.toString();
    }
}