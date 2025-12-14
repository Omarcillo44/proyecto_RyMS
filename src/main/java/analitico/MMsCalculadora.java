package analitico;

public class MMsCalculadora {
    
    public static ResultadoAnaliticoMMs calcular(double lambda, double mu, int s, Double k, Double t) 
            throws IllegalArgumentException {
        
        // Validación de parámetros
        if (lambda <= 0) {
            throw new IllegalArgumentException("λ debe ser mayor que 0");
        }
        if (mu <= 0) {
            throw new IllegalArgumentException("μ debe ser mayor que 0");
        }
        if (s < 1) {
            throw new IllegalArgumentException("s debe ser mayor o igual a 1");
        }
        if (lambda >= s * mu) {
            throw new IllegalArgumentException("Sistema inestable: λ debe ser menor que s·μ (λ < s·μ)");
        }
        
        // Cálculos básicos
        double a = lambda / mu;
        double rho = lambda / (s * mu);
        
        // Cálculo de P0
        double suma = 0.0;
        for (int n = 0; n < s; n++) {
            suma += Math.pow(a, n) / factorial(n);
        }
        double terminoS = Math.pow(a, s) / (factorial(s) * (1 - rho));
        double P0 = 1.0 / (suma + terminoS);
        
        // Cálculo de C(s,a) - Probabilidad de espera (Erlang C)
        double C = (Math.pow(a, s) / factorial(s)) * (1.0 / (1 - rho)) * P0;
        
        // Métricas del sistema
        double Lq = C * rho / (1 - rho);
        double L = Lq + a;
        double Wq = Lq / lambda;
        double W = Wq + (1.0 / mu);
        double U = rho;
        
        // Cálculos opcionales
        Double P_n_mas_k = null;
        if (k != null && k >= 0) {
            if (k < s) {
                P_n_mas_k = 1.0;
                for (int n = 0; n <= k; n++) {
                    if (n < s) {
                        P_n_mas_k -= (Math.pow(a, n) / factorial(n)) * P0;
                    } else {
                        P_n_mas_k -= (Math.pow(a, n) / (factorial(s) * Math.pow(s, n - s))) * P0;
                    }
                }
            } else {
                P_n_mas_k = C * Math.pow(rho, k - s + 1);
            }
        }
        
        Double P_Wq_mayor_t = null;
        Double P_Ws_mayor_t = null;
        if (t != null && t >= 0) {
            P_Wq_mayor_t = C * Math.exp(-s * mu * (1 - rho) * t);
            P_Ws_mayor_t = Math.exp(-s * mu * (1 - rho) * t);
        }
        
        return new ResultadoAnaliticoMMs(lambda, mu, s, rho, a, P0, C, Lq, L, Wq, W, U,
                                         P_n_mas_k, P_Wq_mayor_t, P_Ws_mayor_t);
    }
    
    private static double factorial(int n) {
        if (n <= 1) return 1.0;
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}