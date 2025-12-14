package analitico;

public class ResultadoAnaliticoMMs {
    private final double lambda;
    private final double mu;
    private final int s;
    private final double rho;
    private final double a;
    private final double P0;
    private final double C;
    private final double Lq;
    private final double L;
    private final double Wq;
    private final double W;
    private final double U;
    private final Double P_n_mas_k;
    private final Double P_Wq_mayor_t;
    private final Double P_Ws_mayor_t;
    
    public ResultadoAnaliticoMMs(double lambda, double mu, int s, double rho, double a, double P0, double C,
                                 double Lq, double L, double Wq, double W, double U,
                                 Double P_n_mas_k, Double P_Wq_mayor_t, Double P_Ws_mayor_t) {
        this.lambda = lambda;
        this.mu = mu;
        this.s = s;
        this.rho = rho;
        this.a = a;
        this.P0 = P0;
        this.C = C;
        this.Lq = Lq;
        this.L = L;
        this.Wq = Wq;
        this.W = W;
        this.U = U;
        this.P_n_mas_k = P_n_mas_k;
        this.P_Wq_mayor_t = P_Wq_mayor_t;
        this.P_Ws_mayor_t = P_Ws_mayor_t;
    }
    
    // Getters
    public double getLambda() { return lambda; }
    public double getMu() { return mu; }
    public int getS() { return s; }
    public double getRho() { return rho; }
    public double getA() { return a; }
    public double getP0() { return P0; }
    public double getC() { return C; }
    public double getLq() { return Lq; }
    public double getL() { return L; }
    public double getWq() { return Wq; }
    public double getW() { return W; }
    public double getU() { return U; }
    public Double getPNMasK() { return P_n_mas_k; }
    public Double getPWqMayorT() { return P_Wq_mayor_t; }
    public Double getPWsMayorT() { return P_Ws_mayor_t; }
    
    public boolean isEstable() { return rho < 1.0; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== RESULTADOS ANALÍTICOS M/M/s ==========\n");
        sb.append(String.format("Estado del sistema: %s\n", isEstable() ? "ESTABLE" : "INESTABLE"));
        sb.append(String.format("Intensidad de tráfico por servidor (ρ): %.4f\n", rho));
        sb.append(String.format("Tráfico ofrecido (a): %.4f\n", a));
        sb.append(String.format("Probabilidad sistema vacío (P₀): %.4f (%.2f%%)\n", P0, P0 * 100));
        sb.append(String.format("Probabilidad de espera - Erlang C (C): %.4f (%.2f%%)\n", C, C * 100));
        sb.append(String.format("Número promedio en la cola (Lq): %.4f clientes\n", Lq));
        sb.append(String.format("Número promedio en el sistema (L): %.4f clientes\n", L));
        sb.append(String.format("Tiempo promedio en la cola (Wq): %.4f unidades\n", Wq));
        sb.append(String.format("Tiempo promedio en el sistema (W): %.4f unidades\n", W));
        sb.append(String.format("Utilización del sistema (U): %.4f (%.2f%%)\n", U, U * 100));
        
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