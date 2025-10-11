package sislim.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Cliente - Aplicación del concepto de HERENCIA
 * 
 * Esta clase extiende de Usuario, heredando todos sus atributos y métodos.
 * Representa a un cliente del sistema SISLIM que puede solicitar y cancelar turnos.
 * 
 * Conceptos de POO aplicados:
 * - HERENCIA: Extiende de la clase abstracta Usuario
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - POLIMORFISMO: Sobrescribe métodos de la clase padre
 */
public class Cliente extends Usuario {
    
    // Atributo específico de Cliente - ENCAPSULAMIENTO
    private String direccion;
    
    // Lista para almacenar turnos del cliente (sin persistencia) - ENCAPSULAMIENTO
    private List<Turno> turnos;
    
    /**
     * Constructor por defecto - HERENCIA y ENCAPSULAMIENTO
     */
    public Cliente() {
        super(); // Llama al constructor de la clase padre
        this.direccion = "";
        this.turnos = new ArrayList<>();
    }
    
    /**
     * Constructor con parámetros - HERENCIA y ENCAPSULAMIENTO
     * @param id Identificador único del cliente
     * @param nombre Nombre completo del cliente
     * @param email Correo electrónico del cliente
     * @param telefono Número de teléfono del cliente
     * @param direccion Dirección del cliente
     */
    public Cliente(int id, String nombre, String email, String telefono, String direccion) {
        super(id, nombre, email, telefono); // Llama al constructor de la clase padre
        this.direccion = direccion;
        this.turnos = new ArrayList<>();
    }
    
    // Métodos getters y setters - ENCAPSULAMIENTO
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public List<Turno> getTurnos() {
        return turnos;
    }
    
    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }
    
    /**
     * Implementación del método abstracto login - POLIMORFISMO
     * @return true si el login es exitoso, false en caso contrario
     */
    @Override
    public boolean login() {
        // Lógica simple de login para cliente
        // En un sistema real, aquí se validaría contra una base de datos
        return this.getEmail() != null && !this.getEmail().isEmpty();
    }
    
    /**
     * Método para solicitar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param turno El turno que el cliente desea solicitar
     * @return true si la solicitud fue exitosa, false en caso contrario
     */
    public boolean solicitarTurno(Turno turno) {
        try {
            // Validación: verificar que el turno no sea nulo
            if (turno == null) {
                throw new IllegalArgumentException("El turno no puede ser nulo");
            }
            
            // Validación: verificar que el cliente no tenga ya un turno en la misma fecha y hora
            for (Turno t : turnos) {
                if (t.getFecha().equals(turno.getFecha()) && 
                    t.getHora().equals(turno.getHora()) &&
                    !"Cancelado".equals(t.getEstado())) {
                    throw new IllegalStateException("Ya tiene un turno reservado para esa fecha y hora");
                }
            }
            
            // Asignar el cliente al turno
            turno.setIdCliente(this.getId());
            
            // Agregar el turno a la lista del cliente
            turnos.add(turno);
            
            System.out.println("Turno solicitado exitosamente por: " + this.getNombre());
            return true;
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al solicitar turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para cancelar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno a cancelar
     * @return true si la cancelación fue exitosa, false en caso contrario
     */
    public boolean cancelarTurno(int idTurno) {
        try {
            // Buscar el turno en la lista del cliente
            Turno turnoACancelar = null;
            for (Turno turno : turnos) {
                if (turno.getIdTurno() == idTurno) {
                    turnoACancelar = turno;
                    break;
                }
            }
            
            // Validación: verificar que el turno existe
            if (turnoACancelar == null) {
                throw new IllegalStateException("No se encontró el turno con ID: " + idTurno);
            }
            
            // Validación: verificar que el turno no esté ya cancelado
            if ("Cancelado".equals(turnoACancelar.getEstado())) {
                throw new IllegalStateException("El turno ya está cancelado");
            }
            
            // Cambiar el estado del turno a cancelado
            turnoACancelar.setEstado("Cancelado");
            
            System.out.println("Turno cancelado exitosamente por: " + this.getNombre());
            return true;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al cancelar turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para consultar el historial de turnos - FUNCIONALIDAD DEL SISTEMA
     * @return Lista de todos los turnos del cliente
     */
    public List<Turno> consultarHistorial() {
        System.out.println("Historial de turnos para: " + this.getNombre());
        for (Turno turno : turnos) {
            System.out.println(turno.toString());
        }
        return new ArrayList<>(turnos); // Retorna una copia para mantener encapsulamiento
    }
    
    /**
     * Método toString sobrescrito - POLIMORFISMO
     * @return String con la representación del objeto Cliente
     */
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nombre='" + getNombre() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", telefono='" + getTelefono() + '\'' +
                ", direccion='" + direccion + '\'' +
                ", cantidadTurnos=" + turnos.size() +
                '}';
    }
}
