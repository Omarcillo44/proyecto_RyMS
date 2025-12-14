package modelo;

public class Cliente {
    private final int id;
    private double tiempoLlegada;
    private double tiempoInicioServicio;
    private double tiempoFinServicio;
    
    // Nuevos atributos para tabla detallada
    private double aleatorio1;  // Para generar tiempo entre llegadas
    private double aleatorio2;  // Para generar tiempo de servicio
    private double tiempoEntreLlegada;
    private double tiempoOcio;  // Tiempo que el servidor estuvo ocioso antes de este cliente
    private int servidorAsignado; // Para M/M/s
    
    public Cliente(int id, double tiempoLlegada) {
        this.id = id;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoInicioServicio = -1;
        this.tiempoFinServicio = -1;
        this.aleatorio1 = 0;
        this.aleatorio2 = 0;
        this.tiempoEntreLlegada = 0;
        this.tiempoOcio = 0;
        this.servidorAsignado = -1;
    }
    
    // Getters y setters
    public int getId() { return id; }
    public double getTiempoLlegada() { return tiempoLlegada; }
    public double getTiempoInicioServicio() { return tiempoInicioServicio; }
    public double getTiempoFinServicio() { return tiempoFinServicio; }
    public double getAleatorio1() { return aleatorio1; }
    public double getAleatorio2() { return aleatorio2; }
    public double getTiempoEntreLlegada() { return tiempoEntreLlegada; }
    public double getTiempoOcio() { return tiempoOcio; }
    public int getServidorAsignado() { return servidorAsignado; }
    
    public void setTiempoInicioServicio(double tiempo) { this.tiempoInicioServicio = tiempo; }
    public void setTiempoFinServicio(double tiempo) { this.tiempoFinServicio = tiempo; }
    public void setAleatorio1(double aleatorio) { this.aleatorio1 = aleatorio; }
    public void setAleatorio2(double aleatorio) { this.aleatorio2 = aleatorio; }
    public void setTiempoEntreLlegada(double tiempo) { this.tiempoEntreLlegada = tiempo; }
    public void setTiempoOcio(double tiempo) { this.tiempoOcio = tiempo; }
    public void setServidorAsignado(int servidor) { this.servidorAsignado = servidor; }
    
    // Métricas calculadas
    public double getTiempoEspera() {
        return tiempoInicioServicio - tiempoLlegada;
    }
    
    public double getTiempoServicio() {
        return tiempoFinServicio - tiempoInicioServicio;
    }
    
    public double getTiempoEnSistema() {
        return tiempoFinServicio - tiempoLlegada;
    }
}
