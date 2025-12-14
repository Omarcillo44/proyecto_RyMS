// Archivo: Servidor.java
package modelo;

public class Servidor {
    private final int id;
    private boolean libre;
    private double tiempoFinServicio;
    private Cliente clienteActual;
    private double tiempoOcupadoAcumulado;
    private int clientesAtendidos;
    
    public Servidor(int id) {
        this.id = id;
        this.libre = true;
        this.tiempoFinServicio = 0;
        this.clienteActual = null;
        this.tiempoOcupadoAcumulado = 0;
        this.clientesAtendidos = 0;
    }
    
    public void asignarCliente(Cliente cliente, double tiempoActual, double tiempoServicio) {
        this.libre = false;
        this.clienteActual = cliente;
        this.tiempoFinServicio = tiempoActual + tiempoServicio;
        cliente.setTiempoInicioServicio(tiempoActual);
        cliente.setTiempoFinServicio(this.tiempoFinServicio);
    }
    
    public void liberarServidor(double tiempoActual) {
        if (clienteActual != null) {
            double tiempoServicio = tiempoActual - clienteActual.getTiempoInicioServicio();
            tiempoOcupadoAcumulado += tiempoServicio;
            clientesAtendidos++;
        }
        this.libre = true;
        this.clienteActual = null;
        this.tiempoFinServicio = 0;
    }
    
    public double calcularUtilizacion(double tiempoTotal) {
        return tiempoOcupadoAcumulado / tiempoTotal;
    }
    
    // Getters
    public int getId() { return id; }
    public boolean isLibre() { return libre; }
    public double getTiempoFinServicio() { return tiempoFinServicio; }
    public Cliente getClienteActual() { return clienteActual; }
    public int getClientesAtendidos() { return clientesAtendidos; }
}
