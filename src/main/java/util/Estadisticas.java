// Archivo: Estadisticas.java
package util;

import java.util.List;

public class Estadisticas {
    
    public static double calcularMedia(List<Double> valores) {
        if (valores.isEmpty()) return 0.0;
        double suma = 0.0;
        for (double valor : valores) {
            suma += valor;
        }
        return suma / valores.size();
    }
    
    public static double calcularMax(List<Double> valores) {
        if (valores.isEmpty()) return 0.0;
        double max = valores.get(0);
        for (double valor : valores) {
            if (valor > max) max = valor;
        }
        return max;
    }
    
    public static double calcularMin(List<Double> valores) {
        if (valores.isEmpty()) return 0.0;
        double min = valores.get(0);
        for (double valor : valores) {
            if (valor < min) min = valor;
        }
        return min;
    }
    
    public static double calcularDesviacionEstandar(List<Double> valores) {
        if (valores.size() < 2) return 0.0;
        double media = calcularMedia(valores);
        double sumaCuadrados = 0.0;
        for (double valor : valores) {
            sumaCuadrados += Math.pow(valor - media, 2);
        }
        return Math.sqrt(sumaCuadrados / (valores.size() - 1));
    }
    
    public static double[] calcularIC95(List<Double> valores) {
        if (valores.size() < 2) return new double[]{0.0, 0.0};
        double media = calcularMedia(valores);
        double desv = calcularDesviacionEstandar(valores);
        double errorEstandar = desv / Math.sqrt(valores.size());
        double margen = 1.96 * errorEstandar; // Z para 95% confianza
        return new double[]{media - margen, media + margen};
    }
    
    public static double calcularErrorRelativo(double valorTeorico, double valorSimulado) {
        if (valorTeorico == 0) return 0.0;
        return Math.abs((valorSimulado - valorTeorico) / valorTeorico) * 100;
    }
}