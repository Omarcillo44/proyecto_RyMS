// Archivo: ValidadorParametros.java
package util;

public class ValidadorParametros {
    
    public static boolean validarMM1(double lambda, double mu) {
        if (lambda <= 0) {
            System.err.println("Error: λ debe ser mayor que 0");
            return false;
        }
        if (mu <= 0) {
            System.err.println("Error: μ debe ser mayor que 0");
            return false;
        }
        if (lambda >= mu) {
            System.err.println("Error: Sistema inestable (λ >= μ)");
            return false;
        }
        return true;
    }
    
    public static boolean validarMMs(double lambda, double mu, int s) {
        if (lambda <= 0) {
            System.err.println("Error: λ debe ser mayor que 0");
            return false;
        }
        if (mu <= 0) {
            System.err.println("Error: μ debe ser mayor que 0");
            return false;
        }
        if (s < 1) {
            System.err.println("Error: s debe ser mayor o igual a 1");
            return false;
        }
        if (lambda >= s * mu) {
            System.err.println("Error: Sistema inestable (λ >= s·μ)");
            return false;
        }
        return true;
    }
    
    public static boolean validarNumeroClientes(int N) {
        if (N < 100) {
            System.out.println("Advertencia: N < 100. Se recomienda N >= 100 para resultados significativos");
            return true; // Permitir pero advertir
        }
        if (N > 1000000) {
            System.err.println("Error: N demasiado grande (> 1,000,000)");
            return false;
        }
        return true;
    }
}
