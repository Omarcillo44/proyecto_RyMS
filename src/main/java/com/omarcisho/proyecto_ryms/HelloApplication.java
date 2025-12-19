package com.omarcisho.proyecto_ryms;

import analitico.*;
import simulacion.*;
import util.ValidadorParametros;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Carga la vista del Menú Principal
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MenuPrincipal.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sistema de Colas - UPIICSA");
        // stage.setMaximized(true); // Opcional
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
//
//        Scanner scanner = new Scanner(System.in);
//
//        System.out.println("╔════════════════════════════════════════════════╗");
//        System.out.println("║   SISTEMA DE LÍNEAS DE ESPERA M/M/1 y M/M/s   ║");
//        System.out.println("╚════════════════════════════════════════════════╝\n");
//
//        while (true) {
//            System.out.println("\n=== MENÚ PRINCIPAL (Modo Consola) ===");
//            System.out.println("1. Sistema M/M/1 (Un servidor)");
//            System.out.println("2. Sistema M/M/s (Múltiples servidores)");
//            System.out.println("0. Salir");
//            System.out.print("Seleccione una opción: ");
//
//            int opcion = scanner.nextInt();
//
//            if (opcion == 0) {
//                System.out.println("\n¡Gracias por usar el sistema!");
//                break;
//            }
//
//            try {
//                if (opcion == 1) {
//                    ejecutarSistemaMM1(scanner);
//                } else if (opcion == 2) {
//                    ejecutarSistemaMMs(scanner);
//                } else {
//                    System.out.println("❌ Opción inválida.");
//                }
//            } catch (Exception e) {
//                e.printStackTrace(); // Imprimir error completo para depurar
//                System.out.println("\n❌ ERROR: " + e.getMessage());
//                scanner.nextLine(); // Limpiar buffer en caso de error de entrada
//            }
//        }
//
//        scanner.close();
//    }
//
//    private static void ejecutarSistemaMM1(Scanner scanner) {
//        System.out.println("\n╔════════════════════════════════════════════════╗");
//        System.out.println("║           SISTEMA M/M/1 - UN SERVIDOR          ║");
//        System.out.println("╚════════════════════════════════════════════════╝");
//
//        // PASO 1: PARTE ANALÍTICA
//        System.out.println("\n--- PASO 1: SOLUCIÓN ANALÍTICA ---");
//        System.out.print("Ingrese λ (tasa de llegadas): ");
//        double lambda = scanner.nextDouble();
//
//        System.out.print("Ingrese μ (tasa de servicio): ");
//        double mu = scanner.nextDouble();
//
//        System.out.print("¿Calcular P(n>k)? (s/n): ");
//        String respK = scanner.next();
//        Double k = null;
//        if (respK.equalsIgnoreCase("s")) {
//            System.out.print("Ingrese k: ");
//            k = scanner.nextDouble();
//        }
//
//        System.out.print("¿Calcular P(W>t)? (s/n): ");
//        String respT = scanner.next();
//        Double t = null;
//        if (respT.equalsIgnoreCase("s")) {
//            System.out.print("Ingrese t: ");
//            t = scanner.nextDouble();
//        }
//
//        // Calcular solución analítica
//        ResultadoAnaliticoMM1 resultadoAnalitico = MM1Calculadora.calcular(lambda, mu, k, t);
//        System.out.println(resultadoAnalitico);
//
//        // PASO 2: TRANSICIÓN A SIMULACIÓN
//        System.out.println("\n--- PASO 2: SIMULACIÓN ---");
//        System.out.print("¿Desea simular el sistema? (s/n): ");
//        String respSim = scanner.next();
//
//        if (respSim.equalsIgnoreCase("s")) {
//            System.out.print("Número de clientes a simular (N ≥ 100): ");
//            int N = scanner.nextInt();
//
//            if (!ValidadorParametros.validarNumeroClientes(N)) {
//                System.out.println("⚠️ Número de clientes inválido. Usando N=1000");
//                N = 1000;
//            }
//
//            System.out.print("¿Usar semilla aleatoria? (s/n): ");
//            String respSemilla = scanner.next();
//            Long semilla = null;
//            if (respSemilla.equalsIgnoreCase("s")) {
//                System.out.print("Ingrese semilla: ");
//                semilla = scanner.nextLong();
//            }
//
//            System.out.println("\n🔄 Ejecutando simulación...");
//
//            // AGREGAR ESTO: Definir Warm-Up (20% de N)
//            int warmUp = (int) (N * 0.20);
//            System.out.println("ℹ️  Se descartarán los primeros " + warmUp + " clientes (Warm-up 20%) para estabilizar métricas.");
//
//            // LLAMADA ACTUALIZADA
//            ResultadoSimulacionMM1 resultadoSim = SimuladorMM1.simular(lambda, mu, N, warmUp, semilla);
//
//            // PASO 3: MOSTRAR COMPARACIÓN
//            System.out.println(resultadoSim.generarReporte(resultadoAnalitico));
//
//            // PASO 4: TABLA DETALLADA
//            System.out.print("¿Desea ver la tabla detallada cliente por cliente? (s/n): ");
//            String respTabla = scanner.next();
//            if (respTabla.equalsIgnoreCase("s")) {
//                resultadoSim.imprimirTablaDetallada();
//            }
//        }
//    }
//
//    private static void ejecutarSistemaMMs(Scanner scanner) {
//        System.out.println("\n╔════════════════════════════════════════════════╗");
//        System.out.println("║       SISTEMA M/M/s - MÚLTIPLES SERVIDORES     ║");
//        System.out.println("╚════════════════════════════════════════════════╝");
//
//        // PASO 1: PARTE ANALÍTICA
//        System.out.println("\n--- PASO 1: SOLUCIÓN ANALÍTICA ---");
//        System.out.print("Ingrese λ (tasa de llegadas): ");
//        double lambda = scanner.nextDouble();
//
//        System.out.print("Ingrese μ (tasa de servicio por servidor): ");
//        double mu = scanner.nextDouble();
//
//        System.out.print("Ingrese s (número de servidores): ");
//        int s = scanner.nextInt();
//
//        System.out.print("¿Calcular P(n>k)? (s/n): ");
//        String respK = scanner.next();
//        Double k = null;
//        if (respK.equalsIgnoreCase("s")) {
//            System.out.print("Ingrese k: ");
//            k = scanner.nextDouble();
//        }
//
//        System.out.print("¿Calcular P(W>t)? (s/n): ");
//        String respT = scanner.next();
//        Double t = null;
//        if (respT.equalsIgnoreCase("s")) {
//            System.out.print("Ingrese t: ");
//            t = scanner.nextDouble();
//        }
//
//        // Calcular solución analítica
//        ResultadoAnaliticoMMs resultadoAnalitico = MMsCalculadora.calcular(lambda, mu, s, k, t);
//        System.out.println(resultadoAnalitico);
//
//        // PASO 2: TRANSICIÓN A SIMULACIÓN
//        System.out.println("\n--- PASO 2: SIMULACIÓN ---");
//        System.out.print("¿Desea simular el sistema? (s/n): ");
//        String respSim = scanner.next();
//
//        if (respSim.equalsIgnoreCase("s")) {
//            System.out.print("Número de clientes a simular (N ≥ 100): ");
//            int N = scanner.nextInt();
//
//            if (!ValidadorParametros.validarNumeroClientes(N)) {
//                System.out.println("⚠️ Número de clientes inválido. Usando N=1000");
//                N = 1000;
//            }
//
//            System.out.print("¿Usar semilla aleatoria? (s/n): ");
//            String respSemilla = scanner.next();
//            Long semilla = null;
//            if (respSemilla.equalsIgnoreCase("s")) {
//                System.out.print("Ingrese semilla: ");
//                semilla = scanner.nextLong();
//            }
//
//            // CORRECCIÓN PARA M/M/s: Definir Warm-Up
//            // Usamos un 20% de N como periodo de calentamiento
//            int warmUp = (int) (N * 0.20);
//            System.out.println("ℹ️  Se descartarán los primeros " + warmUp + " clientes (Warm-up 20%) para estabilizar métricas.");
//
//            System.out.println("\n🔄 Ejecutando simulación...");
//            // Llamada actualizada con el parámetro warmUp
//            ResultadoSimulacionMMs resultadoSim = SimuladorMMs.simular(lambda, mu, s, N, warmUp, semilla);
//
//            // PASO 3: MOSTRAR COMPARACIÓN
//            System.out.println(resultadoSim.generarReporte(resultadoAnalitico));
//
//            // PASO 4: TABLA DETALLADA
//            System.out.print("¿Desea ver la tabla detallada cliente por cliente? (s/n): ");
//            String respTabla = scanner.next();
//            if (respTabla.equalsIgnoreCase("s")) {
//                resultadoSim.imprimirTablaDetallada();
//            }
//        }
    }
}