package simulacion;

import modelo.*;
import util.GeneradorExponencial;
import util.Estadisticas;
import java.util.*;

public class SimuladorMM1 {
    
    /**
     * Simula el sistema M/M/1 con periodo de calentamiento (warm-up).
     * @param lambda Tasa de llegadas
     * @param mu Tasa de servicio
     * @param N Número de clientes ESTADÍSTICOS (útiles)
     * @param warmUp Número de clientes a descartar al inicio
     * @param semilla Semilla aleatoria (opcional)
     */
    public static ResultadoSimulacionMM1 simular(double lambda, double mu, int N, int warmUp, Long semilla) {

        GeneradorExponencial generador = (semilla != null)
                ? new GeneradorExponencial(semilla)
                : new GeneradorExponencial();

        ColaEventos colaEventos = new ColaEventos();
        Queue<Cliente> colaClientes = new LinkedList<>();
        Servidor servidor = new Servidor(0);

        List<Cliente> clientesCompletados = new ArrayList<>(); // Solo post-warmup
        double reloj = 0.0;
        int clientesGenerados = 0;
        double ultimoTiempoFinServicio = 0.0;
        
        int clientesTotalesAProcesar = N + warmUp;
        int contadorCompletadosTotal = 0;

        // Variables para áreas y estadísticas
        double areaLq = 0.0;
        double areaL = 0.0;
        int longitudColaActual = 0;
        int clientesEnSistema = 0;
        int maxCola = 0;

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
        primerCliente.setTiempoOcio(0);

        colaEventos.agregarEvento(new Evento(Evento.TipoEvento.LLEGADA, tiempoLlegada, primerCliente, -1));

        // --- BUCLE DE SIMULACIÓN ---
        while (contadorCompletadosTotal < clientesTotalesAProcesar && colaEventos.hayEventos()) {

            Evento evento = colaEventos.obtenerProximoEvento();
            double tiempoAnterior = reloj;
            reloj = evento.getTiempo();
            double deltaT = reloj - tiempoAnterior;

            // Acumular áreas siempre (luego restaremos lo del warm-up)
            areaLq += longitudColaActual * deltaT;
            areaL += clientesEnSistema * deltaT;

            // DETECTAR FIN DE WARM-UP
            if (!enFaseEstable && contadorCompletadosTotal >= warmUp) {
                enFaseEstable = true;
                tiempoFinWarmUp = reloj;
                areaLqPreWarmUp = areaLq;
                areaLPreWarmUp = areaL;
                maxCola = longitudColaActual; // Reiniciar pico para medir solo en fase estable
            }

            if (evento.getTipo() == Evento.TipoEvento.LLEGADA) {
                Cliente cliente = evento.getCliente();
                clientesEnSistema++;

                if (servidor.isLibre()) {
                    double tiempoOcio = reloj - ultimoTiempoFinServicio;
                    cliente.setTiempoOcio(tiempoOcio);

                    double tiempoServicio = generador.generarTiempo(mu);
                    cliente.setAleatorio2(generador.getUltimoAleatorio());
                    servidor.asignarCliente(cliente, reloj, tiempoServicio);

                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.FIN_SERVICIO,
                                    servidor.getTiempoFinServicio(), cliente, 0));
                } else {
                    cliente.setTiempoOcio(0);
                    colaClientes.add(cliente);
                    longitudColaActual++;

                    // Registrar maxCola solo si ya estamos en fase estable (o si queremos medir todo)
                    if (enFaseEstable && longitudColaActual > maxCola) {
                        maxCola = longitudColaActual;
                    }
                }

                if (clientesGenerados < clientesTotalesAProcesar) {
                    tiempoEntreLlegada = generador.generarTiempo(lambda);
                    double proximaLlegada = reloj + tiempoEntreLlegada;
                    Cliente nuevoCliente = new Cliente(++clientesGenerados, proximaLlegada);
                    nuevoCliente.setAleatorio1(generador.getUltimoAleatorio());
                    nuevoCliente.setTiempoEntreLlegada(tiempoEntreLlegada);
                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.LLEGADA, proximaLlegada, nuevoCliente, -1));
                }

            } else { // FIN_SERVICIO
                Cliente cliente = evento.getCliente();
                clientesEnSistema--;
                ultimoTiempoFinServicio = reloj;
                
                // Liberar servidor inmediatamente para contabilidad correcta
                servidor.liberarServidor(reloj);
                
                contadorCompletadosTotal++;

                // Solo guardar si pasó el warm-up
                if (contadorCompletadosTotal > warmUp) {
                    clientesCompletados.add(cliente);
                }

                if (!colaClientes.isEmpty()) {
                    Cliente siguienteCliente = colaClientes.poll();
                    longitudColaActual--;

                    siguienteCliente.setTiempoOcio(0);
                    double tiempoServicio = generador.generarTiempo(mu);
                    siguienteCliente.setAleatorio2(generador.getUltimoAleatorio());

                    servidor.asignarCliente(siguienteCliente, reloj, tiempoServicio);

                    colaEventos.agregarEvento(new Evento(Evento.TipoEvento.FIN_SERVICIO,
                                    servidor.getTiempoFinServicio(), siguienteCliente, 0));
                }
            }
        }

        // --- CÁLCULO DE MÉTRICAS (Fase Estable) ---
        double tiempoTotalSimulacion = reloj;
        double tiempoEstable = tiempoTotalSimulacion - tiempoFinWarmUp;

        // Áreas efectivas
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
        
        // Utilización sobre tiempo total (aceptable)
        double utilizacion = servidor.calcularUtilizacion(tiempoTotalSimulacion);

        return new ResultadoSimulacionMM1(
                Wq_sim, W_sim, Lq_sim, L_sim, utilizacion,
                maxCola, maxEspera, clientesSinEspera, N,
                tiemposEspera, tiemposEnSistema, clientesCompletados,
                tiempoEstable // PASAR TIEMPO ESTABLE para Little
        );
    }
}