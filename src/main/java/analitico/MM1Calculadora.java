package analitico;

public class MM1Calculadora {
    
    public static ResultadoAnaliticoMM1 calcular(double lambda, double mu, Double k, Double t) 
            throws IllegalArgumentException {
        
        // Validación de parámetros
        if (lambda <= 0) {
            throw new IllegalArgumentException("λ debe ser mayor que 0");
        }
        if (mu <= 0) {
            throw new IllegalArgumentException("μ debe ser mayor que 0");
        }
        if (lambda >= mu) {
            throw new IllegalArgumentException("Sistema inestable: λ debe ser menor que μ (λ < μ)");
        }
        
        // Cálculos básicos
        double rho = lambda / mu;
        double P0 = 1 - rho;
        double Lq = (rho * rho) / (1 - rho);
        double L = rho / (1 - rho);
        double Wq = Lq / lambda;
        double W = Wq + (1.0 / mu);
        double Pw = rho;
        
        // Cálculos opcionales
        Double P_n_mas_k = null;
        if (k != null && k >= 0) {
            P_n_mas_k = Math.pow(rho, k + 1);
        }
        
        Double P_Wq_mayor_t = null;
        Double P_Ws_mayor_t = null;
        if (t != null && t >= 0) {
            P_Wq_mayor_t = rho * Math.exp(-mu * (1 - rho) * t);
            P_Ws_mayor_t = Math.exp(-mu * (1 - rho) * t);
        }
        
        return new ResultadoAnaliticoMM1(lambda, mu, rho, P0, Lq, L, Wq, W, Pw, 
                                         P_n_mas_k, P_Wq_mayor_t, P_Ws_mayor_t);
    }
}