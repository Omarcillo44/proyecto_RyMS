// Archivo: GeneradorExponencial.java
package util;

import java.util.Random;

public class GeneradorExponencial {
    private final Random random;
    private double ultimoAleatorio; // Para guardar el último aleatorio generado
    
    public GeneradorExponencial() {
        this.random = new Random();
        this.ultimoAleatorio = 0;
    }
    
    public GeneradorExponencial(long semilla) {
        this.random = new Random(semilla);
        this.ultimoAleatorio = 0;
    }
    
    /**
     * Genera un tiempo aleatorio con distribución exponencial
     * @param tasa Tasa del proceso (λ o μ)
     * @return Tiempo generado
     */
    public double generarTiempo(double tasa) {
        ultimoAleatorio = random.nextDouble();
        return -Math.log(1 - ultimoAleatorio) / tasa;
    }
    
    /**
     * Retorna el último número aleatorio generado
     */
    public double getUltimoAleatorio() {
        return ultimoAleatorio;
    }
}
