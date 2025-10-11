package sislim.model;

import java.time.LocalDateTime;

/**
 * Clase Notificacion - Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase representa una notificación en el sistema SISLIM. Las notificaciones
 * se envían a los usuarios para informar sobre cambios en el estado de sus turnos,
 * confirmaciones, recordatorios y otros avisos importantes.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 */
public class Notificacion {
    
    // Atributos privados - ENCAPSULAMIENTO
    private int idNotificacion;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String tipo; // Recordatorio, Confirmacion, Aviso
    private int idTurno;
    private boolean enviada; // Indica si la notificación ya fue enviada
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     */
    public Notificacion() {
        this.idNotificacion = 0;
        this.mensaje = "";
        this.fechaEnvio = LocalDateTime.now();
        this.tipo = "";
        this.idTurno = 0;
        this.enviada = false;
    }
    
    /**
     * Constructor con parámetros principales - ENCAPSULAMIENTO
     * @param idNotificacion Identificador único de la notificación
     * @param mensaje Mensaje de la notificación
     * @param tipo Tipo de notificación (Recordatorio, Confirmacion, Aviso)
     * @param idTurno ID del turno asociado a la notificación
     */
    public Notificacion(int idNotificacion, String mensaje, String tipo, int idTurno) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
        this.tipo = tipo;
        this.idTurno = idTurno;
        this.enviada = false;
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    public int getIdNotificacion() {
        return idNotificacion;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public int getIdTurno() {
        return idTurno;
    }
    
    public boolean isEnviada() {
        return enviada;
    }
    
    // Métodos setters - ENCAPSULAMIENTO
    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }
    
    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }
    
    /**
     * Método para enviar la notificación - FUNCIONALIDAD DEL SISTEMA
     * @return true si la notificación se envió exitosamente, false en caso contrario
     */
    public boolean enviar() {
        try {
            // Validaciones básicas
            if (mensaje == null || mensaje.trim().isEmpty()) {
                throw new IllegalArgumentException("El mensaje no puede estar vacío");
            }
            if (tipo == null || tipo.trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de notificación no puede estar vacío");
            }
            if (!esTipoValido(tipo)) {
                throw new IllegalArgumentException("Tipo de notificación no válido: " + tipo + 
                    ". Tipos válidos: Recordatorio, Confirmacion, Aviso");
            }
            if (idTurno <= 0) {
                throw new IllegalArgumentException("El ID del turno debe ser mayor a 0");
            }
            
            // Verificar si ya fue enviada
            if (enviada) {
                System.out.println("La notificación ya fue enviada anteriormente");
                return true;
            }
            
            // Simular el envío de la notificación
            // En un sistema real, aquí se enviaría por email, SMS, push notification, etc.
            System.out.println("=== NOTIFICACIÓN ENVIADA ===");
            System.out.println("Tipo: " + tipo);
            System.out.println("Fecha: " + fechaEnvio);
            System.out.println("Turno ID: " + idTurno);
            System.out.println("Mensaje: " + mensaje);
            System.out.println("=============================");
            
            // Marcar como enviada
            this.enviada = true;
            
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al enviar notificación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método privado para validar tipos de notificación - ENCAPSULAMIENTO
     * @param tipo El tipo a validar
     * @return true si el tipo es válido, false en caso contrario
     */
    private boolean esTipoValido(String tipo) {
        return "Recordatorio".equals(tipo) || 
               "Confirmacion".equals(tipo) || 
               "Aviso".equals(tipo);
    }
    
    /**
     * Método para crear una notificación de recordatorio - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno
     * @param mensaje Mensaje personalizado
     * @return Nueva notificación de recordatorio
     */
    public static Notificacion crearRecordatorio(int idTurno, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdTurno(idTurno);
        notificacion.setTipo("Recordatorio");
        notificacion.setMensaje(mensaje);
        return notificacion;
    }
    
    /**
     * Método para crear una notificación de confirmación - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno
     * @param mensaje Mensaje personalizado
     * @return Nueva notificación de confirmación
     */
    public static Notificacion crearConfirmacion(int idTurno, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdTurno(idTurno);
        notificacion.setTipo("Confirmacion");
        notificacion.setMensaje(mensaje);
        return notificacion;
    }
    
    /**
     * Método para crear una notificación de aviso - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno
     * @param mensaje Mensaje personalizado
     * @return Nueva notificación de aviso
     */
    public static Notificacion crearAviso(int idTurno, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdTurno(idTurno);
        notificacion.setTipo("Aviso");
        notificacion.setMensaje(mensaje);
        return notificacion;
    }
    
    /**
     * Método para obtener el estado de la notificación - FUNCIONALIDAD DEL SISTEMA
     * @return String con el estado de la notificación
     */
    public String obtenerEstado() {
        if (enviada) {
            return "Enviada";
        } else {
            return "Pendiente";
        }
    }
    
    /**
     * Método para verificar si es urgente - FUNCIONALIDAD DEL SISTEMA
     * @return true si es una notificación urgente, false en caso contrario
     */
    public boolean esUrgente() {
        return "Aviso".equals(tipo);
    }
    
    /**
     * Método para obtener el tiempo transcurrido desde el envío - FUNCIONALIDAD DEL SISTEMA
     * @return String con el tiempo transcurrido
     */
    public String obtenerTiempoTranscurrido() {
        if (!enviada) {
            return "No enviada";
        }
        
        LocalDateTime ahora = LocalDateTime.now();
        long minutosTranscurridos = java.time.Duration.between(fechaEnvio, ahora).toMinutes();
        
        if (minutosTranscurridos < 60) {
            return minutosTranscurridos + " minutos";
        } else if (minutosTranscurridos < 1440) { // 24 horas
            return (minutosTranscurridos / 60) + " horas";
        } else {
            return (minutosTranscurridos / 1440) + " días";
        }
    }
    
    /**
     * Método toString sobrescrito - ENCAPSULAMIENTO
     * @return String con la representación del objeto Notificacion
     */
    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", mensaje='" + mensaje + '\'' +
                ", fechaEnvio=" + fechaEnvio +
                ", tipo='" + tipo + '\'' +
                ", idTurno=" + idTurno +
                ", enviada=" + enviada +
                '}';
    }
}
