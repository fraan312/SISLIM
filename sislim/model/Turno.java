package sislim.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase Turno - Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase representa un turno de limpieza domiciliaria en el sistema SISLIM.
 * Contiene toda la información necesaria para gestionar un turno y mantiene
 * una relación de composición con las notificaciones.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - COMPOSICIÓN: Relación con Notificacion (un turno tiene notificaciones)
 */
public class Turno {
    
    // Atributos privados - ENCAPSULAMIENTO
    private int idTurno;
    private LocalDate fecha;
    private LocalTime hora;
    private int duracion; // en minutos
    private String tipoServicio;
    private String estado; // Pendiente, Confirmado, Cancelado
    private String observaciones;
    private int idCliente;
    private int idDisponibilidad;
    private int idAdmin;
    
    // Lista de notificaciones asociadas al turno - COMPOSICIÓN
    private List<Notificacion> notificaciones;
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     */
    public Turno() {
        this.idTurno = 0;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
        this.duracion = 120; // 2 horas por defecto
        this.tipoServicio = "";
        this.estado = "Pendiente";
        this.observaciones = "";
        this.idCliente = 0;
        this.idDisponibilidad = 0;
        this.idAdmin = 0;
        this.notificaciones = new ArrayList<>();
    }
    
    /**
     * Constructor con parámetros principales - ENCAPSULAMIENTO
     * @param idTurno Identificador único del turno
     * @param fecha Fecha del turno
     * @param hora Hora del turno
     * @param duracion Duración en minutos
     * @param tipoServicio Tipo de servicio de limpieza
     * @param idCliente ID del cliente que solicita el turno
     * @param idDisponibilidad ID de la disponibilidad utilizada
     */
    public Turno(int idTurno, LocalDate fecha, LocalTime hora, int duracion, 
                 String tipoServicio, int idCliente, int idDisponibilidad) {
        this.idTurno = idTurno;
        this.fecha = fecha;
        this.hora = hora;
        this.duracion = duracion;
        this.tipoServicio = tipoServicio;
        this.estado = "Pendiente";
        this.observaciones = "";
        this.idCliente = idCliente;
        this.idDisponibilidad = idDisponibilidad;
        this.idAdmin = 0;
        this.notificaciones = new ArrayList<>();
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    public int getIdTurno() {
        return idTurno;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public LocalTime getHora() {
        return hora;
    }
    
    public int getDuracion() {
        return duracion;
    }
    
    public String getTipoServicio() {
        return tipoServicio;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public int getIdCliente() {
        return idCliente;
    }
    
    public int getIdDisponibilidad() {
        return idDisponibilidad;
    }
    
    public int getIdAdmin() {
        return idAdmin;
    }
    
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
    
    // Métodos setters - ENCAPSULAMIENTO
    public void setIdTurno(int idTurno) {
        this.idTurno = idTurno;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
    
    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
    
    public void setIdDisponibilidad(int idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
    
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }
    
    /**
     * Método para crear un turno - FUNCIONALIDAD DEL SISTEMA
     * @return true si el turno se creó exitosamente, false en caso contrario
     */
    public boolean crear() {
        try {
            // Validaciones básicas
            if (fecha == null) {
                throw new IllegalArgumentException("La fecha no puede ser nula");
            }
            if (hora == null) {
                throw new IllegalArgumentException("La hora no puede ser nula");
            }
            if (duracion <= 0) {
                throw new IllegalArgumentException("La duración debe ser mayor a 0");
            }
            if (tipoServicio == null || tipoServicio.trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de servicio no puede estar vacío");
            }
            
            // Crear notificación inicial
            Notificacion notificacionInicial = new Notificacion();
            notificacionInicial.setMensaje("Su turno ha sido creado exitosamente");
            notificacionInicial.setTipo("Confirmacion");
            notificacionInicial.setIdTurno(this.idTurno);
            
            notificaciones.add(notificacionInicial);
            
            System.out.println("Turno creado exitosamente con ID: " + this.idTurno);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para actualizar el estado del turno - FUNCIONALIDAD DEL SISTEMA
     * @param nuevoEstado El nuevo estado del turno
     * @return true si la actualización fue exitosa, false en caso contrario
     */
    public boolean actualizarEstado(String nuevoEstado) {
        try {
            // Validación: verificar que el nuevo estado sea válido
            if (!esEstadoValido(nuevoEstado)) {
                throw new IllegalArgumentException("Estado no válido: " + nuevoEstado + 
                    ". Estados válidos: Pendiente, Confirmado, Cancelado");
            }
            
            String estadoAnterior = this.estado;
            this.estado = nuevoEstado;
            
            // Crear notificación del cambio de estado
            Notificacion notificacion = new Notificacion();
            notificacion.setMensaje("El estado de su turno cambió de '" + estadoAnterior + 
                                  "' a '" + nuevoEstado + "'");
            notificacion.setTipo("Aviso");
            notificacion.setIdTurno(this.idTurno);
            
            notificaciones.add(notificacion);
            
            System.out.println("Estado del turno " + this.idTurno + " actualizado a: " + nuevoEstado);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para enviar notificaciones - FUNCIONALIDAD DEL SISTEMA
     * @return true si las notificaciones se enviaron exitosamente
     */
    public boolean notificar() {
        try {
            if (notificaciones.isEmpty()) {
                System.out.println("No hay notificaciones para enviar");
                return false;
            }
            
            for (Notificacion notificacion : notificaciones) {
                if (notificacion.enviar()) {
                    System.out.println("Notificación enviada: " + notificacion.getMensaje());
                }
            }
            
            return true;
            
        } catch (Exception e) {
            System.out.println("Error al enviar notificaciones: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método privado para validar estados - ENCAPSULAMIENTO
     * @param estado El estado a validar
     * @return true si el estado es válido, false en caso contrario
     */
    private boolean esEstadoValido(String estado) {
        return "Pendiente".equals(estado) || 
               "Confirmado".equals(estado) || 
               "Cancelado".equals(estado);
    }
    
    /**
     * Método para mostrar información del turno - FUNCIONALIDAD DEL SISTEMA
     * @return String con la información completa del turno
     */
    public String mostrarTurno() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL TURNO ===\n");
        info.append("ID: ").append(idTurno).append("\n");
        info.append("Fecha: ").append(fecha).append("\n");
        info.append("Hora: ").append(hora).append("\n");
        info.append("Duración: ").append(duracion).append(" minutos\n");
        info.append("Tipo de Servicio: ").append(tipoServicio).append("\n");
        info.append("Estado: ").append(estado).append("\n");
        info.append("Observaciones: ").append(observaciones).append("\n");
        info.append("ID Cliente: ").append(idCliente).append("\n");
        info.append("ID Disponibilidad: ").append(idDisponibilidad).append("\n");
        info.append("ID Administrador: ").append(idAdmin).append("\n");
        info.append("Notificaciones: ").append(notificaciones.size()).append("\n");
        
        return info.toString();
    }
    
    /**
     * Método toString sobrescrito - ENCAPSULAMIENTO
     * @return String con la representación del objeto Turno
     */
    @Override
    public String toString() {
        return "Turno{" +
                "idTurno=" + idTurno +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", duracion=" + duracion +
                ", tipoServicio='" + tipoServicio + '\'' +
                ", estado='" + estado + '\'' +
                ", idCliente=" + idCliente +
                ", idDisponibilidad=" + idDisponibilidad +
                ", idAdmin=" + idAdmin +
                '}';
    }
}
