// Archivo: Evento.java
package modelo;

public class Evento implements Comparable<Evento> {
    public enum TipoEvento {
        LLEGADA,
        FIN_SERVICIO
    }
    
    private final TipoEvento tipo;
    private final double tiempo;
    private final Cliente cliente;
    private final int servidor; // -1 para llegadas, >=0 para fin de servicio
    
    public Evento(TipoEvento tipo, double tiempo, Cliente cliente, int servidor) {
        this.tipo = tipo;
        this.tiempo = tiempo;
        this.cliente = cliente;
        this.servidor = servidor;
    }
    
    // Getters
    public TipoEvento getTipo() { return tipo; }
    public double getTiempo() { return tiempo; }
    public Cliente getCliente() { return cliente; }
    public int getServidor() { return servidor; }
    
    @Override
    public int compareTo(Evento otro) {
        // Ordenar por tiempo ascendente
        return Double.compare(this.tiempo, otro.tiempo);
    }
}