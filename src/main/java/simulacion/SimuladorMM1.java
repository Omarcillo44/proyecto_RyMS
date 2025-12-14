package simulacion;

import modelo.*;
import util.GeneradorExponencial;
import util.Estadisticas;
import java.util.*;

public class SimuladorMM1 {
    
    public static ResultadoSimulacionMM1 simular(double lambda, double mu, int N, Long semilla) {

        GeneradorExponencial generador = (semilla != null)
                ? new GeneradorExponencial(semilla)
                : new GeneradorExponencial();

        ColaEventos colaEventos = new ColaEventos();
        Queue<Cliente> colaClientes = new LinkedList<>();
        Servidor servidor = new Servidor(0);

        List<Cliente> clientesCompletados = new ArrayList<>();
        double reloj = 0.0;
        int clientesGenerados = 0;
        double ultimoTiempoFinServicio = 0.0;

        // Estadísticas durante la simulación
        double areaLq = 0.0;
        double areaL = 0.0;
        int longitudColaActual = 0;
        int clientesEnSistema = 0;
        int maxCola = 0;

        // Generar primera llegada
        double tiempoEntreLlegada = generador.generarTiempo(lambda);
        double tiempoLlegada = tiempoEntreLlegada;

        Cliente primerCliente = new Cliente(++clientesGenerados, tiempoLlegada);
        primerCliente.setAleatorio1(generador.getUltimoAleatorio());
        primerCliente.setTiempoEntreLlegada(tiempoEntreLlegada);
        primerCliente.setTiempoOcio(0);

        colaEventos.agregarEvento(
                new Evento(Evento.TipoEvento.LLEGADA, tiempoLlegada, primerCliente, -1)
        );

        // Simulación de eventos
        while (clientesCompletados.size() < N && colaEventos.hayEventos()) {

            Evento evento = colaEventos.obtenerProximoEvento();
            double tiempoAnterior = reloj;
            reloj = evento.getTiempo();
            double deltaT = reloj - tiempoAnterior;

            areaLq += longitudColaActual * deltaT;
            areaL += clientesEnSistema * deltaT;

            if (evento.getTipo() == Evento.TipoEvento.LLEGADA) {

                Cliente cliente = evento.getCliente();
                clientesEnSistema++;

                if (servidor.isLibre()) {
                    double tiempoOcio = reloj - ultimoTiempoFinServicio;
                    cliente.setTiempoOcio(tiempoOcio);

                    double tiempoServicio = generador.generarTiempo(mu);
                    cliente.setAleatorio2(generador.getUltimoAleatorio());

                    servidor.asignarCliente(cliente, reloj, tiempoServicio);

                    colaEventos.agregarEvento(
                            new Evento(Evento.TipoEvento.FIN_SERVICIO,
                                    servidor.getTiempoFinServicio(), cliente, 0)
                    );
                } else {
                    cliente.setTiempoOcio(0);
                    colaClientes.add(cliente);
                    longitudColaActual++;

                    if (longitudColaActual > maxCola)
                        maxCola = longitudColaActual;
                }

                if (clientesGenerados < N) {
                    tiempoEntreLlegada = generador.generarTiempo(lambda);
                    double proximaLlegada = reloj + tiempoEntreLlegada;

                    Cliente nuevoCliente = new Cliente(++clientesGenerados, proximaLlegada);
                    nuevoCliente.setAleatorio1(generador.getUltimoAleatorio());
                    nuevoCliente.setTiempoEntreLlegada(tiempoEntreLlegada);

                    colaEventos.agregarEvento(
                            new Evento(Evento.TipoEvento.LLEGADA, proximaLlegada, nuevoCliente, -1)
                    );
                }

            } else { // FIN_SERVICIO

                Cliente cliente = evento.getCliente();
                clientesCompletados.add(cliente);
                clientesEnSistema--;
                ultimoTiempoFinServicio = reloj;
                
                // CORRECCIÓN CLAVE: Registrar estadísticas del servidor INMEDIATAMENTE
                servidor.liberarServidor(reloj);

                if (!colaClientes.isEmpty()) {
                    Cliente siguienteCliente = colaClientes.poll();
                    longitudColaActual--;

                    siguienteCliente.setTiempoOcio(0);

                    double tiempoServicio = generador.generarTiempo(mu);
                    siguienteCliente.setAleatorio2(generador.getUltimoAleatorio());

                    servidor.asignarCliente(siguienteCliente, reloj, tiempoServicio);

                    colaEventos.agregarEvento(
                            new Evento(Evento.TipoEvento.FIN_SERVICIO,
                                    servidor.getTiempoFinServicio(), siguienteCliente, 0)
                    );
                }
                // Si la cola está vacía, el servidor ya quedó libre arriba
            }
        }

        double tiempoTotal = reloj; // Tiempo final de la simulación
        double Lq_sim = areaLq / tiempoTotal;
        double L_sim = areaL / tiempoTotal;

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
        double utilizacion = servidor.calcularUtilizacion(tiempoTotal);

        return new ResultadoSimulacionMM1(
                Wq_sim, W_sim, Lq_sim, L_sim, utilizacion,
                maxCola, maxEspera, clientesSinEspera, N,
                tiemposEspera, tiemposEnSistema, clientesCompletados,
                tiempoTotal // CORRECCIÓN: Pasamos el tiempo total
        );
    }
}