package sislim.service;

import sislim.model.*;
import sislim.dao.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase NotificacionService - Aplicación del concepto de ENCAPSULAMIENTO y MVC
 * 
 * Esta clase proporciona servicios para la gestión de notificaciones en el sistema SISLIM.
 * Maneja el envío, almacenamiento y gestión de notificaciones para turnos.
 * 
 * En el TP4, esta clase actúa como CONTROLADOR en el patrón MVC:
 * - MODEL: Notificacion (clase del modelo)
 * - VIEW: SISLIMSwing.java (interfaz gráfica)
 * - CONTROLLER: NotificacionService (esta clase - lógica de negocio)
 * 
 * Utiliza DAO (Data Access Object) para persistencia en MySQL con JDBC.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - COMPOSICIÓN: Utiliza las clases del modelo y DAOs
 */
public class NotificacionService {
    
    // DAO para acceso a datos - ENCAPSULAMIENTO y COMPOSICIÓN
    private NotificacionDAO notificacionDAO;
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     * Inicializa el DAO para acceso a la base de datos
     */
    public NotificacionService() {
        this.notificacionDAO = new NotificacionDAO();
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    /**
     * Método para obtener todas las notificaciones desde la base de datos - READ (CRUD)
     * @return Lista de todas las notificaciones
     */
    public List<Notificacion> getNotificaciones() {
        return notificacionDAO.leerTodas();
    }
    
    /**
     * Método para enviar una notificación - FUNCIONALIDAD DEL SISTEMA
     * @param notificacion La notificación a enviar
     * @return true si se envió exitosamente, false en caso contrario
     */
    public boolean enviarNotificacion(Notificacion notificacion) {
        try {
            // Validaciones básicas
            if (notificacion == null) {
                throw new IllegalArgumentException("La notificación no puede ser nula");
            }
            
            // Enviar la notificación (lógica de negocio)
            if (notificacion.enviar()) {
                // Persistir la notificación en la base de datos usando DAO - CREATE (CRUD)
                Notificacion notificacionGuardada = notificacionDAO.crear(notificacion);
                
                if (notificacionGuardada != null) {
                    System.out.println("Notificación enviada exitosamente con ID: " + notificacionGuardada.getIdNotificacion());
                    return true;
                }
            }
            
            return false;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar notificación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para crear y enviar notificación de confirmación de turno - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno confirmado
     * @return true si se envió exitosamente, false en caso contrario
     */
    public boolean enviarConfirmacionTurno(Turno turno) {
        try {
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            
            String mensaje = String.format(
                "Su turno para el %s a las %s ha sido confirmado. " +
                "Tipo de servicio: %s. Duración: %d minutos.",
                turno.getFecha(), turno.getHora(), turno.getTipoServicio(), turno.getDuracion()
            );
            
            Notificacion notificacion = Notificacion.crearConfirmacion(turno.getIdTurno(), mensaje);
            return enviarNotificacion(notificacion);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar confirmación de turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para crear y enviar notificación de cancelación de turno - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno cancelado
     * @return true si se envió exitosamente, false en caso contrario
     */
    public boolean enviarCancelacionTurno(Turno turno) {
        try {
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            
            String mensaje = String.format(
                "Su turno para el %s a las %s ha sido cancelado. " +
                "Si necesita reagendar, por favor contacte al administrador.",
                turno.getFecha(), turno.getHora()
            );
            
            Notificacion notificacion = Notificacion.crearAviso(turno.getIdTurno(), mensaje);
            return enviarNotificacion(notificacion);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar cancelación de turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para crear y enviar notificación de recordatorio - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno para el cual enviar recordatorio
     * @param horasAntes Horas antes del turno para enviar el recordatorio
     * @return true si se envió exitosamente, false en caso contrario
     */
    public boolean enviarRecordatorioTurno(Turno turno, int horasAntes) {
        try {
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            if (horasAntes <= 0) {
                throw new IllegalArgumentException("Las horas antes deben ser mayor a 0");
            }
            
            String mensaje = String.format(
                "Recordatorio: Su turno de limpieza está programado para el %s a las %s. " +
                "Tipo de servicio: %s. Por favor confirme su asistencia.",
                turno.getFecha(), turno.getHora(), turno.getTipoServicio()
            );
            
            Notificacion notificacion = Notificacion.crearRecordatorio(turno.getIdTurno(), mensaje);
            return enviarNotificacion(notificacion);
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar recordatorio de turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para enviar notificaciones masivas - FUNCIONALIDAD DEL SISTEMA
     * @param turnos Lista de turnos para los cuales enviar notificaciones
     * @param tipoNotificacion Tipo de notificación a enviar
     * @param mensaje Mensaje personalizado
     * @return Número de notificaciones enviadas exitosamente
     */
    public int enviarNotificacionesMasivas(List<Turno> turnos, String tipoNotificacion, String mensaje) {
        int notificacionesEnviadas = 0;
        
        try {
            if (turnos == null || turnos.isEmpty()) {
                throw new IllegalArgumentException("La lista de turnos no puede estar vacía");
            }
            if (mensaje == null || mensaje.trim().isEmpty()) {
                throw new IllegalArgumentException("El mensaje no puede estar vacío");
            }
            
            for (Turno turno : turnos) {
                Notificacion notificacion;
                
                switch (tipoNotificacion.toLowerCase()) {
                    case "confirmacion":
                        notificacion = Notificacion.crearConfirmacion(turno.getIdTurno(), mensaje);
                        break;
                    case "aviso":
                        notificacion = Notificacion.crearAviso(turno.getIdTurno(), mensaje);
                        break;
                    case "recordatorio":
                        notificacion = Notificacion.crearRecordatorio(turno.getIdTurno(), mensaje);
                        break;
                    default:
                        throw new IllegalArgumentException("Tipo de notificación no válido: " + tipoNotificacion);
                }
                
                if (enviarNotificacion(notificacion)) {
                    notificacionesEnviadas++;
                }
            }
            
            System.out.println("Se enviaron " + notificacionesEnviadas + " notificaciones de " + turnos.size() + " turnos");
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar notificaciones masivas: " + e.getMessage());
        }
        
        return notificacionesEnviadas;
    }
    
    /**
     * Método para buscar notificaciones por turno - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno
     * @return Lista de notificaciones para ese turno
     */
    public List<Notificacion> buscarNotificacionesPorTurno(int idTurno) {
        return notificacionDAO.buscarPorTurno(idTurno); // Buscar en BD
    }
    
    /**
     * Método para buscar notificaciones por tipo - FUNCIONALIDAD DEL SISTEMA
     * @param tipo Tipo de notificación
     * @return Lista de notificaciones de ese tipo
     */
    public List<Notificacion> buscarNotificacionesPorTipo(String tipo) {
        return notificacionDAO.buscarPorTipo(tipo); // Buscar en BD
    }
    
    /**
     * Método para buscar notificaciones no enviadas - FUNCIONALIDAD DEL SISTEMA
     * @return Lista de notificaciones pendientes de envío
     */
    public List<Notificacion> buscarNotificacionesPendientes() {
        // En BD, todas las notificaciones se consideran enviadas al estar guardadas
        // Este método podría ser útil para notificaciones en memoria antes de persistir
        return new ArrayList<>();
    }
    
    /**
     * Método para obtener estadísticas de notificaciones - FUNCIONALIDAD DEL SISTEMA
     * @return String con estadísticas
     */
    public String obtenerEstadisticas() {
        // Leer todas las notificaciones desde BD para calcular estadísticas - READ (CRUD)
        List<Notificacion> notificaciones = notificacionDAO.leerTodas();
        
        int totalNotificaciones = notificaciones.size();
        int notificacionesEnviadas = notificaciones.size(); // En BD todas están enviadas
        int notificacionesPendientes = 0;
        
        int confirmaciones = (int) notificaciones.stream().filter(n -> "Confirmacion".equals(n.getTipo())).count();
        int avisos = (int) notificaciones.stream().filter(n -> "Aviso".equals(n.getTipo())).count();
        int recordatorios = (int) notificaciones.stream().filter(n -> "Recordatorio".equals(n.getTipo())).count();
        
        return String.format(
            "=== ESTADÍSTICAS DE NOTIFICACIONES ===\n" +
            "Total de notificaciones: %d\n" +
            "Notificaciones enviadas: %d\n" +
            "Notificaciones pendientes: %d\n" +
            "Confirmaciones: %d\n" +
            "Avisos: %d\n" +
            "Recordatorios: %d\n" +
            "=====================================",
            totalNotificaciones, notificacionesEnviadas, notificacionesPendientes,
            confirmaciones, avisos, recordatorios
        );
    }
    
    /**
     * Método para limpiar notificaciones antiguas - FUNCIONALIDAD DEL SISTEMA
     * @param diasAntiguedad Días de antigüedad para considerar una notificación como antigua
     * @return Número de notificaciones eliminadas
     */
    public int limpiarNotificacionesAntiguas(int diasAntiguedad) {
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasAntiguedad);
        
        // Leer todas las notificaciones desde BD - READ (CRUD)
        List<Notificacion> notificaciones = notificacionDAO.leerTodas();
        
        List<Notificacion> notificacionesAEliminar = notificaciones.stream()
                .filter(notificacion -> notificacion.getFechaEnvio().isBefore(fechaLimite))
                .collect(Collectors.toList());
        
        // Nota: El NotificacionDAO no tiene método eliminar implementado
        // En un sistema completo se implementaría DELETE aquí
        System.out.println("Se encontraron " + notificacionesAEliminar.size() + " notificaciones antiguas para eliminar");
        return notificacionesAEliminar.size();
    }
    
    /**
     * Método para reenviar notificaciones fallidas - FUNCIONALIDAD DEL SISTEMA
     * @return Número de notificaciones reenviadas exitosamente
     */
    public int reenviarNotificacionesFallidas() {
        List<Notificacion> notificacionesPendientes = buscarNotificacionesPendientes();
        int reenviadas = 0;
        
        for (Notificacion notificacion : notificacionesPendientes) {
            if (enviarNotificacion(notificacion)) {
                reenviadas++;
            }
        }
        
        System.out.println("Se reenviaron " + reenviadas + " notificaciones fallidas");
        return reenviadas;
    }
}
