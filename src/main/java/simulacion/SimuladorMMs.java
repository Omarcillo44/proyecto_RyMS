package simulacion;

import modelo.*;
import util.GeneradorExponencial;
import util.Estadisticas;
import java.util.*;

public class SimuladorMMs {

    /**
     * Simula el sistema M/M/s con periodo de calentamiento (warm-up).
     * @param lambda Tasa de llegadas
     * @param mu Tasa de servicio
     * @param s Número de servidores
     * @param N Número de clientes ESTADÍSTICOS (útiles)
     * @param warmUp Número de clientes a descartar al inicio (calentamiento)
     * @param semilla Semilla aleatoria
     */
    public static ResultadoSimulacionMMs simular(double lambda, double mu, int s, int N, int warmUp, Long semilla) {
        GeneradorExponencial generador = (semilla != null) ? 
            new GeneradorExponencial(semilla) : new GeneradorExponencial();
        
        ColaEventos colaEventos = new ColaEventos();
        Queue<Cliente> colaClientes = new LinkedList<>();
        Servidor[] servidores = new Servidor[s];
        double[] ultimoTiempoFinServicio = new double[s];
        
        for (int i = 0; i < s; i++) {
            servidores[i] = new Servidor(i);
            ultimoTiempoFinServicio[i] = 0.0;
        }
        
        List<Cliente> clientesCompletados = new ArrayList<>(); // Solo los útiles (post-warmup)
        double reloj = 0.0;
        int clientesGenerados = 0;
        int clientesTotalesAProcesar = N + warmUp;
        int contadorCompletadosTotal = 0; // Incluye los de warm-up

        // Variables para áreas
        double areaLq = 0.0;
        double areaL = 0.0;
        int longitudColaActual = 0;
        int clientesEnSistema = 0;
        int maxCola = 0; // Max cola detectada DURANTE la fase estable

        // Variables de control de Warm-Up
        double tiempoFinWarmUp = 0.0;
        double areaLqPreWarmUp = 0.0;
        double areaLPreWarmUp = 0.0;
        boolean enFaseEstable = false;

        // Generar primera llegada
        double tiempoEntreLlegada = generador.generarTiempo(lambda);
        double tiempoLlegada = tiempoEntreLlegada;
        Cliente primerCliente = new Cliente(++clientesGenerados, tiempoLlegada);
        primerCliente.setAleatorio1(generador.getUltimoAleatorio());
        primerCliente.setTiempoEntreLlegada(tiempoEntreLlegada);
        colaEventos.agregarEvento(new Evento(Evento.TipoEvento.LLEGADA, tiempoLlegada, primerCliente, -1));
        
        // --- BUCLE DE SIMULACIÓN ---
        while (contadorCompletadosTotal < clientesTotalesAProcesar && colaEventos.hayEventos()) {
            Evento evento = colaEventos.obtenerProximoEvento();
            double tiempoAnterior = reloj;
            reloj = evento.getTiempo();
            double deltaT = reloj - tiempoAnterior;
            
            // Acumular áreas siempre (luego restaremos la parte del warm-up)
            areaLq += longitudColaActual * deltaT;
            areaL += clientesEnSistema * deltaT;
            
            // DETECTAR FIN DE WARM-UP (Transición)
            if (!enFaseEstable && contadorCompletadosTotal >= warmUp) {
                enFaseEstable = true;
                tiempoFinWarmUp = reloj;
                areaLqPreWarmUp = areaLq;
                areaLPreWarmUp = areaL;
                // Reiniciamos maxCola para medir solo el pico en estado estable
                maxCola = longitudColaActual; 
            }

            if (evento.getTipo() == Evento.TipoEvento.LLEGADA) {
                Cliente cliente = evento.getCliente();
                clientesEnSistema++;
                
                // Lógica de asignación de servidor
                Servidor servidorLibre = null;
                int idServidorLibre = -1;
                for (int i = 0; i < s; i++) {
                    if (servidores[i].isLibre()) {
                        servidorLibre = servidores[i];
                        idServidorLibre = i;
                        break;
                    }
                }
                
                if (servidorLibre != null) {
                    double tiempoOcio = reloj - ultimoTiempoFinServicio[idServidorLibre];
                    cliente.setTiempoOcio(tiempoOcio);
                    cliente.setServidorAsignado(idServidorLibre);
                    
                    double tiempoServicio = generador.generarTiempo(mu);
                    cliente.setAleatorio2(generador.getUltimoAleatorio());
                    servidorLibre.asignarCliente(cliente, reloj, tiempoServicio);
                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.FIN_SERVICIO,
                        servidorLibre.getTiempoFinServicio(), cliente, idServidorLibre));
                } else {
                    cliente.setTiempoOcio(0);
                    colaClientes.add(cliente);
                    longitudColaActual++;
                    
                    // Solo actualizamos maxCola si ya pasamos el warmUp (o si decidimos medir desde el principio)
                    // Para ser estrictos con el estado estable:
                    if (enFaseEstable && longitudColaActual > maxCola) {
                        maxCola = longitudColaActual;
                    }
                }
                
                // Generar siguiente llegada si no hemos excedido el total requerido
                if (clientesGenerados < clientesTotalesAProcesar) {
                    tiempoEntreLlegada = generador.generarTiempo(lambda);
                    double proximaLlegada = reloj + tiempoEntreLlegada;
                    Cliente nuevoCliente = new Cliente(++clientesGenerados, proximaLlegada);
                    nuevoCliente.setAleatorio1(generador.getUltimoAleatorio());
                    nuevoCliente.setTiempoEntreLlegada(tiempoEntreLlegada);
                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.LLEGADA,
                        proximaLlegada, nuevoCliente, -1));
                }
                
            } else { // FIN_SERVICIO
                Cliente cliente = evento.getCliente();
                Servidor servidor = servidores[evento.getServidor()];
                
                // IMPORTANTE: Liberar servidor inmediatamente para contabilidad correcta
                servidor.liberarServidor(reloj);
                ultimoTiempoFinServicio[evento.getServidor()] = reloj;
                
                contadorCompletadosTotal++;
                clientesEnSistema--;
                
                // SOLO AGREGAR A LISTA DE RESULTADOS SI PASÓ EL WARM-UP
                if (contadorCompletadosTotal > warmUp) {
                    clientesCompletados.add(cliente);
                }

                if (!colaClientes.isEmpty()) {
                    Cliente siguienteCliente = colaClientes.poll();
                    longitudColaActual--;
                    
                    siguienteCliente.setTiempoOcio(0);
                    siguienteCliente.setServidorAsignado(evento.getServidor());
                    
                    double tiempoServicio = generador.generarTiempo(mu);
                    siguienteCliente.setAleatorio2(generador.getUltimoAleatorio());
                    servidor.asignarCliente(siguienteCliente, reloj, tiempoServicio);
                    
                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.FIN_SERVICIO,
                        servidor.getTiempoFinServicio(), siguienteCliente, evento.getServidor()));
                }
            }
        }
        
        // --- CÁLCULO DE MÉTRICAS (SOLO FASE ESTABLE) ---
        double tiempoTotalSimulacion = reloj;
        double tiempoEstable = tiempoTotalSimulacion - tiempoFinWarmUp;
        
        // Áreas efectivas (Total - PreWarmUp)
        double areaLqEfectiva = areaLq - areaLqPreWarmUp;
        double areaLEfectiva = areaL - areaLPreWarmUp;
        
        double Lq_sim = areaLqEfectiva / tiempoEstable;
        double L_sim = areaLEfectiva / tiempoEstable;
        
        List<Double> tiemposEspera = new ArrayList<>();
        List<Double> tiemposEnSistema = new ArrayList<>();
        double maxEspera = 0.0;
        int clientesSinEspera = 0;
        
        for (Cliente c : clientesCompletados) {
            double espera = c.getTiempoEspera();
            tiemposEspera.add(espera);
            tiemposEnSistema.add(c.getTiempoEnSistema());
            if (espera > maxEspera) maxEspera = espera;
            if (espera == 0) clientesSinEspera++;
        }
        
        double Wq_sim = Estadisticas.calcularMedia(tiemposEspera);
        double W_sim = Estadisticas.calcularMedia(tiemposEnSistema);
        
        // Utilización (Nota: Servidor calcula sobre tiempoTotal, para mayor precisión habría que 
        // resetear contadores en el servidor también, pero con N grande el error se diluye)
        double[] utilizaciones = new double[s];
        for (int i = 0; i < s; i++) {
            utilizaciones[i] = servidores[i].calcularUtilizacion(tiempoTotalSimulacion);
        }
        
        return new ResultadoSimulacionMMs(Wq_sim, W_sim, Lq_sim, L_sim, utilizaciones,
            maxCola, maxEspera, clientesSinEspera, N, tiemposEspera, tiemposEnSistema,
            clientesCompletados, tiempoEstable); // Usamos tiempoEstable para Ley de Little
    }
}