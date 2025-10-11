package sislim.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase Disponibilidad - Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase representa una franja horaria disponible para realizar servicios
 * de limpieza domiciliaria en el sistema SISLIM. Contiene información sobre
 * la fecha, horarios, zona y tipo de servicio disponible.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 */
public class Disponibilidad {
    
    // Atributos privados - ENCAPSULAMIENTO
    private int idDisponibilidad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String zona;
    private String servicio;
    private boolean disponible; // Indica si la disponibilidad está libre o ocupada
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     */
    public Disponibilidad() {
        this.idDisponibilidad = 0;
        this.fecha = LocalDate.now();
        this.horaInicio = LocalTime.of(9, 0); // 9:00 AM por defecto
        this.horaFin = LocalTime.of(17, 0);   // 5:00 PM por defecto
        this.zona = "";
        this.servicio = "";
        this.disponible = true;
    }
    
    /**
     * Constructor con parámetros principales - ENCAPSULAMIENTO
     * @param idDisponibilidad Identificador único de la disponibilidad
     * @param fecha Fecha de la disponibilidad
     * @param horaInicio Hora de inicio de la disponibilidad
     * @param horaFin Hora de fin de la disponibilidad
     * @param zona Zona geográfica de la disponibilidad
     * @param servicio Tipo de servicio disponible
     */
    public Disponibilidad(int idDisponibilidad, LocalDate fecha, LocalTime horaInicio, 
                         LocalTime horaFin, String zona, String servicio) {
        this.idDisponibilidad = idDisponibilidad;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.zona = zona;
        this.servicio = servicio;
        this.disponible = true;
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    public int getIdDisponibilidad() {
        return idDisponibilidad;
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    public LocalTime getHoraFin() {
        return horaFin;
    }
    
    public String getZona() {
        return zona;
    }
    
    public String getServicio() {
        return servicio;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    // Métodos setters - ENCAPSULAMIENTO
    public void setIdDisponibilidad(int idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
    
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    
    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }
    
    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
    
    public void setZona(String zona) {
        this.zona = zona;
    }
    
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    /**
     * Método para cargar disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @return true si la disponibilidad se cargó exitosamente, false en caso contrario
     */
    public boolean cargar() {
        try {
            // Validaciones básicas
            if (fecha == null) {
                throw new IllegalArgumentException("La fecha no puede ser nula");
            }
            if (horaInicio == null) {
                throw new IllegalArgumentException("La hora de inicio no puede ser nula");
            }
            if (horaFin == null) {
                throw new IllegalArgumentException("La hora de fin no puede ser nula");
            }
            if (horaInicio.isAfter(horaFin)) {
                throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la hora de fin");
            }
            if (zona == null || zona.trim().isEmpty()) {
                throw new IllegalArgumentException("La zona no puede estar vacía");
            }
            if (servicio == null || servicio.trim().isEmpty()) {
                throw new IllegalArgumentException("El servicio no puede estar vacío");
            }
            
            System.out.println("Disponibilidad cargada exitosamente con ID: " + this.idDisponibilidad);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al cargar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para editar disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @param nuevaFecha Nueva fecha para la disponibilidad
     * @param nuevaHoraInicio Nueva hora de inicio
     * @param nuevaHoraFin Nueva hora de fin
     * @param nuevaZona Nueva zona
     * @param nuevoServicio Nuevo tipo de servicio
     * @return true si la edición fue exitosa, false en caso contrario
     */
    public boolean editar(LocalDate nuevaFecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin, 
                         String nuevaZona, String nuevoServicio) {
        try {
            // Validaciones
            if (nuevaFecha == null) {
                throw new IllegalArgumentException("La nueva fecha no puede ser nula");
            }
            if (nuevaHoraInicio == null) {
                throw new IllegalArgumentException("La nueva hora de inicio no puede ser nula");
            }
            if (nuevaHoraFin == null) {
                throw new IllegalArgumentException("La nueva hora de fin no puede ser nula");
            }
            if (nuevaHoraInicio.isAfter(nuevaHoraFin)) {
                throw new IllegalArgumentException("La hora de inicio no puede ser posterior a la hora de fin");
            }
            if (nuevaZona == null || nuevaZona.trim().isEmpty()) {
                throw new IllegalArgumentException("La nueva zona no puede estar vacía");
            }
            if (nuevoServicio == null || nuevoServicio.trim().isEmpty()) {
                throw new IllegalArgumentException("El nuevo servicio no puede estar vacío");
            }
            
            // Actualizar los valores
            this.fecha = nuevaFecha;
            this.horaInicio = nuevaHoraInicio;
            this.horaFin = nuevaHoraFin;
            this.zona = nuevaZona;
            this.servicio = nuevoServicio;
            
            System.out.println("Disponibilidad editada exitosamente con ID: " + this.idDisponibilidad);
            return true;
            
        } catch (IllegalArgumentException e) {
            System.out.println("Error al editar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para eliminar disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @return true si la eliminación fue exitosa, false en caso contrario
     */
    public boolean eliminar() {
        try {
            // Validación: verificar que la disponibilidad no esté siendo utilizada
            if (!disponible) {
                throw new IllegalStateException("No se puede eliminar una disponibilidad que está siendo utilizada");
            }
            
            // Marcar como eliminada (en un sistema real, se eliminaría de la base de datos)
            this.disponible = false;
            
            System.out.println("Disponibilidad eliminada exitosamente con ID: " + this.idDisponibilidad);
            return true;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al eliminar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para verificar si está disponible - FUNCIONALIDAD DEL SISTEMA
     * @return true si la disponibilidad está libre, false si está ocupada
     */
    public boolean estaDisponible() {
        return disponible;
    }
    
    /**
     * Método para bloquear la disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @return true si se bloqueó exitosamente, false en caso contrario
     */
    public boolean bloquear() {
        try {
            if (!disponible) {
                throw new IllegalStateException("La disponibilidad ya está bloqueada");
            }
            
            this.disponible = false;
            System.out.println("Disponibilidad bloqueada exitosamente con ID: " + this.idDisponibilidad);
            return true;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al bloquear disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para desbloquear la disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @return true si se desbloqueó exitosamente, false en caso contrario
     */
    public boolean desbloquear() {
        try {
            if (disponible) {
                throw new IllegalStateException("La disponibilidad ya está desbloqueada");
            }
            
            this.disponible = true;
            System.out.println("Disponibilidad desbloqueada exitosamente con ID: " + this.idDisponibilidad);
            return true;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al desbloquear disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para calcular la duración en minutos - FUNCIONALIDAD DEL SISTEMA
     * @return Duración en minutos entre horaInicio y horaFin
     */
    public int calcularDuracion() {
        if (horaInicio == null || horaFin == null) {
            return 0;
        }
        
        int minutosInicio = horaInicio.getHour() * 60 + horaInicio.getMinute();
        int minutosFin = horaFin.getHour() * 60 + horaFin.getMinute();
        
        return minutosFin - minutosInicio;
    }
    
    /**
     * Método toString sobrescrito - ENCAPSULAMIENTO
     * @return String con la representación del objeto Disponibilidad
     */
    @Override
    public String toString() {
        return "Disponibilidad{" +
                "idDisponibilidad=" + idDisponibilidad +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", zona='" + zona + '\'' +
                ", servicio='" + servicio + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
