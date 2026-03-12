package com.calibrador.infra;

import com.calibrador.service.AlertaService;
import com.calibrador.service.AlertaServiceImpl;
import com.calibrador.util.AppException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Tarea programada que verifica vencimientos automáticamente.
 *
 * Corre en dos momentos:
 *   1. Al arrancar la app (inmediatamente)
 *   2. Cada 24 horas después del arranque
 *
 * Así el usuario siempre ve las alertas actualizadas sin hacer nada.
 *
 * Uso en Main.java:
 *
 *   AlertaScheduler scheduler = new AlertaScheduler();
 *   scheduler.iniciar();
 *   // ... al cerrar la app:
 *   scheduler.detener();
 */
public class AlertaScheduler {

    private final AlertaService alertaService;
    private ScheduledExecutorService executor;

    private static final int INTERVALO_HORAS = 24;

    public AlertaScheduler() {
        this.alertaService = new AlertaServiceImpl();
    }

    // Constructor para testing
    public AlertaScheduler(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    /**
     * Inicia el scheduler.
     * Corre verificarVencimientos() ahora y luego cada 24h.
     */
    public void iniciar() {
        executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread hilo = new Thread(runnable, "alerta-scheduler");
            hilo.setDaemon(true); // No bloquea el cierre de la app
            return hilo;
        });

        executor.scheduleAtFixedRate(
                this::ejecutarVerificacion,
                0,                  // Delay inicial: 0 → corre inmediatamente al arrancar
                INTERVALO_HORAS,    // Intervalo: cada 24 horas
                TimeUnit.HOURS
        );

        System.out.println("✓ AlertaScheduler iniciado — verifica cada " + INTERVALO_HORAS + "h");
    }

    /**
     * Detiene el scheduler limpiamente.
     * Llamar esto cuando la app se cierra para no dejar hilos huérfanos.
     */
    public void detener() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
            System.out.println("✓ AlertaScheduler detenido");
        }
    }

    /**
     * La tarea que se ejecuta en cada ciclo.
     * Separada del scheduler para poder testearla de forma independiente.
     */
    private void ejecutarVerificacion() {
        String hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("[" + hora + "] Scheduler: verificando vencimientos...");

        try {
            int nuevas = alertaService.verificarVencimientos();

            if (nuevas > 0) {
                System.out.println("[" + hora + "] Scheduler: " + nuevas + " alerta(s) nueva(s) generada(s)");
            } else {
                System.out.println("[" + hora + "] Scheduler: sin alertas nuevas");
            }

            // Aprovechar el ciclo para limpiar alertas antiguas
            alertaService.limpiarAtendidas();

        } catch (AppException e) {
            // El scheduler nunca debe caerse aunque falle una verificación
            System.err.println("[" + hora + "] Scheduler ERROR: " + e.getMessage());
        }
    }

    public boolean estaActivo() {
        return executor != null && !executor.isShutdown();
    }
}