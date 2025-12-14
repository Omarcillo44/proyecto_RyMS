// Archivo: ColaEventos.java
package modelo;

import java.util.PriorityQueue;

public class ColaEventos {
    private final PriorityQueue<Evento> cola;
    
    public ColaEventos() {
        this.cola = new PriorityQueue<>();
    }
    
    public void agregarEvento(Evento evento) {
        cola.add(evento);
    }
    
    public Evento obtenerProximoEvento() {
        return cola.poll();
    }
    
    public boolean hayEventos() {
        return !cola.isEmpty();
    }
    
    public int size() {
        return cola.size();
    }
}