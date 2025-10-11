package sislim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Administrador - Aplicación del concepto de HERENCIA
 * 
 * Esta clase extiende de Usuario, heredando todos sus atributos y métodos.
 * Representa a un administrador del sistema SISLIM que puede confirmar turnos
 * y gestionar la disponibilidad del servicio.
 * 
 * Conceptos de POO aplicados:
 * - HERENCIA: Extiende de la clase abstracta Usuario
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - POLIMORFISMO: Sobrescribe métodos de la clase padre
 */
public class Administrador extends Usuario {
    
    // Lista para almacenar turnos gestionados por el administrador - ENCAPSULAMIENTO
    private List<Turno> turnosGestionados;
    
    // Lista para almacenar disponibilidades gestionadas - ENCAPSULAMIENTO
    private List<Disponibilidad> disponibilidades;
    
    /**
     * Constructor por defecto - HERENCIA y ENCAPSULAMIENTO
     */
    public Administrador() {
        super(); // Llama al constructor de la clase padre
        this.turnosGestionados = new ArrayList<>();
        this.disponibilidades = new ArrayList<>();
    }
    
    /**
     * Constructor con parámetros - HERENCIA y ENCAPSULAMIENTO
     * @param id Identificador único del administrador
     * @param nombre Nombre completo del administrador
     * @param email Correo electrónico del administrador
     * @param telefono Número de teléfono del administrador
     */
    public Administrador(int id, String nombre, String email, String telefono) {
        super(id, nombre, email, telefono); // Llama al constructor de la clase padre
        this.turnosGestionados = new ArrayList<>();
        this.disponibilidades = new ArrayList<>();
    }
    
    // Métodos getters y setters - ENCAPSULAMIENTO
    public List<Turno> getTurnosGestionados() {
        return turnosGestionados;
    }
    
    public void setTurnosGestionados(List<Turno> turnosGestionados) {
        this.turnosGestionados = turnosGestionados;
    }
    
    public List<Disponibilidad> getDisponibilidades() {
        return disponibilidades;
    }
    
    public void setDisponibilidades(List<Disponibilidad> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }
    
    /**
     * Implementación del método abstracto login - POLIMORFISMO
     * @return true si el login es exitoso, false en caso contrario
     */
    @Override
    public boolean login() {
        // Lógica de login para administrador (más estricta que cliente)
        // En un sistema real, aquí se validaría contra una base de datos
        return this.getEmail() != null && 
               !this.getEmail().isEmpty() && 
               this.getEmail().contains("@sislim.com");
    }
    
    /**
     * Método para confirmar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno que el administrador desea confirmar
     * @return true si la confirmación fue exitosa, false en caso contrario
     */
    public boolean confirmarTurno(Turno turno) {
        try {
            // Validación: verificar que el turno no sea nulo
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            
            // Validación: verificar que el turno esté en estado pendiente
            if (!"Pendiente".equals(turno.getEstado())) {
                throw new IllegalStateException("Solo se pueden confirmar turnos en estado 'Pendiente'");
            }
            
            // Cambiar el estado del turno a confirmado
            turno.setEstado("Confirmado");
            turno.setIdAdmin(this.getId());
            
            // Agregar el turno a la lista de turnos gestionados
            turnosGestionados.add(turno);
            
            System.out.println("Turno confirmado exitosamente por administrador: " + this.getNombre());
            return true;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al confirmar turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para gestionar disponibilidad - FUNCIONALIDAD DEL SISTEMA
     * @param disponibilidad La disponibilidad que el administrador desea gestionar
     * @param accion La acción a realizar: "agregar", "editar", "eliminar"
     * @return true si la operación fue exitosa, false en caso contrario
     */
    public boolean gestionarDisponibilidad(Disponibilidad disponibilidad, String accion) {
        try {
            switch (accion.toLowerCase()) {
                case "agregar":
                    return agregarDisponibilidad(disponibilidad);
                case "editar":
                    return editarDisponibilidad(disponibilidad);
                case "eliminar":
                    return eliminarDisponibilidad(disponibilidad.getIdDisponibilidad());
                default:
                    throw new IllegalArgumentException("Acción no válida. Use: agregar, editar, eliminar");
            }
        } catch (Exception e) {
            System.out.println("Error al gestionar disponibilidad: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método privado para agregar disponibilidad - ENCAPSULAMIENTO
     * @param disponibilidad La disponibilidad a agregar
     * @return true si se agregó exitosamente
     */
    private boolean agregarDisponibilidad(Disponibilidad disponibilidad) {
        if (disponibilidad == null) {
            throw new IllegalArgumentException("La disponibilidad no puede ser nula");
        }
        disponibilidades.add(disponibilidad);
        System.out.println("Disponibilidad agregada exitosamente");
        return true;
    }
    
    /**
     * Método privado para editar disponibilidad - ENCAPSULAMIENTO
     * @param disponibilidad La disponibilidad editada
     * @return true si se editó exitosamente
     */
    private boolean editarDisponibilidad(Disponibilidad disponibilidad) {
        if (disponibilidad == null) {
            throw new IllegalArgumentException("La disponibilidad no puede ser nula");
        }
        
        for (int i = 0; i < disponibilidades.size(); i++) {
            if (disponibilidades.get(i).getIdDisponibilidad() == disponibilidad.getIdDisponibilidad()) {
                disponibilidades.set(i, disponibilidad);
                System.out.println("Disponibilidad editada exitosamente");
                return true;
            }
        }
        
        throw new IllegalStateException("No se encontró la disponibilidad con ID: " + disponibilidad.getIdDisponibilidad());
    }
    
    /**
     * Método privado para eliminar disponibilidad - ENCAPSULAMIENTO
     * @param idDisponibilidad ID de la disponibilidad a eliminar
     * @return true si se eliminó exitosamente
     */
    private boolean eliminarDisponibilidad(int idDisponibilidad) {
        for (int i = 0; i < disponibilidades.size(); i++) {
            if (disponibilidades.get(i).getIdDisponibilidad() == idDisponibilidad) {
                disponibilidades.remove(i);
                System.out.println("Disponibilidad eliminada exitosamente");
                return true;
            }
        }
        
        throw new IllegalStateException("No se encontró la disponibilidad con ID: " + idDisponibilidad);
    }
    
    /**
     * Método para aprobar cancelación de turno - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno cuya cancelación se desea aprobar
     * @return true si la aprobación fue exitosa, false en caso contrario
     */
    public boolean aprobarCancelacion(Turno turno) {
        try {
            // Validación: verificar que el turno no sea nulo
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            
            // Validación: verificar que el turno esté en estado cancelado
            if (!"Cancelado".equals(turno.getEstado())) {
                throw new IllegalStateException("Solo se pueden aprobar cancelaciones de turnos en estado 'Cancelado'");
            }
            
            System.out.println("Cancelación de turno aprobada por administrador: " + this.getNombre());
            return true;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al aprobar cancelación: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método toString sobrescrito - POLIMORFISMO
     * @return String con la representación del objeto Administrador
     */
    @Override
    public String toString() {
        return "Administrador{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", turnosGestionados=" + turnosGestionados.size() +
                ", disponibilidades=" + disponibilidades.size() +
                '}';
    }
}
