package sislim.service;

import sislim.model.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase TurnoService - Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase proporciona servicios para la gestión de turnos en el sistema SISLIM.
 * Maneja la lógica de negocio relacionada con turnos, incluyendo reservas,
 * confirmaciones, cancelaciones y búsquedas.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - COMPOSICIÓN: Utiliza las clases del modelo (Turno, Cliente, Administrador, etc.)
 */
public class TurnoService {
    
    // Lista para almacenar todos los turnos del sistema (sin persistencia) - ENCAPSULAMIENTO
    private List<Turno> turnos;
    private int contadorId; // Contador para generar IDs únicos
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     */
    public TurnoService() {
        this.turnos = new ArrayList<>();
        this.contadorId = 1;
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    public List<Turno> getTurnos() {
        return new ArrayList<>(turnos); // Retorna una copia para mantener encapsulamiento
    }
    
    public int getContadorId() {
        return contadorId;
    }
    
    // Métodos setters - ENCAPSULAMIENTO
    public void setTurnos(List<Turno> turnos) {
        this.turnos = new ArrayList<>(turnos);
    }
    
    public void setContadorId(int contadorId) {
        this.contadorId = contadorId;
    }
    
    /**
     * Método para reservar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param cliente El cliente que solicita el turno
     * @param fecha Fecha del turno
     * @param hora Hora del turno
     * @param duracion Duración en minutos
     * @param tipoServicio Tipo de servicio
     * @param observaciones Observaciones adicionales
     * @return El turno creado o null si hubo error
     */
    public Turno reservarTurno(Cliente cliente, LocalDate fecha, LocalTime hora, 
                              int duracion, String tipoServicio, String observaciones) {
        try {
            // Validaciones básicas
            if (cliente == null) {
                throw new IllegalArgumentException("El cliente no puede ser nulo");
            }
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
            
            // Verificar que no haya conflicto de horarios (cualquier cliente)
            for (Turno turnoExistente : turnos) {
                if (turnoExistente.getFecha().equals(fecha) &&
                    turnoExistente.getHora().equals(hora) &&
                    !"Cancelado".equals(turnoExistente.getEstado())) {
                    throw new IllegalStateException("Ya existe un turno reservado para esa fecha y hora. Por favor, seleccione otro horario.");
                }
            }
            
            // Crear el nuevo turno
            Turno nuevoTurno = new Turno();
            nuevoTurno.setIdTurno(contadorId++);
            nuevoTurno.setFecha(fecha);
            nuevoTurno.setHora(hora);
            nuevoTurno.setDuracion(duracion);
            nuevoTurno.setTipoServicio(tipoServicio);
            nuevoTurno.setObservaciones(observaciones);
            nuevoTurno.setIdCliente(cliente.getId());
            nuevoTurno.setEstado("Pendiente");
            
            // Crear notificación de confirmación
            Notificacion notificacion = Notificacion.crearConfirmacion(
                nuevoTurno.getIdTurno(), 
                "Su turno ha sido reservado exitosamente para el " + fecha + " a las " + hora
            );
            nuevoTurno.getNotificaciones().add(notificacion);
            
            // Agregar el turno a la lista
            turnos.add(nuevoTurno);
            
            // Usar el método del cliente para solicitar el turno
            if (cliente.solicitarTurno(nuevoTurno)) {
                System.out.println("Turno reservado exitosamente con ID: " + nuevoTurno.getIdTurno());
                return nuevoTurno;
            } else {
                // Si falla, remover el turno de la lista
                turnos.remove(nuevoTurno);
                return null;
            }
            
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error al reservar turno: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Método para confirmar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param administrador El administrador que confirma el turno
     * @param idTurno ID del turno a confirmar
     * @return true si la confirmación fue exitosa, false en caso contrario
     */
    public boolean confirmarTurno(Administrador administrador, int idTurno) {
        try {
            // Buscar el turno
            Turno turno = buscarTurnoPorId(idTurno);
            if (turno == null) {
                throw new IllegalStateException("No se encontró el turno con ID: " + idTurno);
            }
            
            // Usar el método del administrador para confirmar el turno
            if (administrador.confirmarTurno(turno)) {
                // Crear notificación de confirmación
                Notificacion notificacion = Notificacion.crearConfirmacion(
                    turno.getIdTurno(),
                    "Su turno ha sido confirmado por el administrador"
                );
                turno.getNotificaciones().add(notificacion);
                
                System.out.println("Turno confirmado exitosamente por administrador: " + administrador.getNombre());
                return true;
            }
            
            return false;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al confirmar turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para cancelar un turno - FUNCIONALIDAD DEL SISTEMA
     * @param cliente El cliente que cancela el turno
     * @param idTurno ID del turno a cancelar
     * @return true si la cancelación fue exitosa, false en caso contrario
     */
    public boolean cancelarTurno(Cliente cliente, int idTurno) {
        try {
            // Buscar el turno
            Turno turno = buscarTurnoPorId(idTurno);
            if (turno == null) {
                throw new IllegalStateException("No se encontró el turno con ID: " + idTurno);
            }
            
            // Verificar que el turno pertenece al cliente
            if (turno.getIdCliente() != cliente.getId()) {
                throw new IllegalStateException("El turno no pertenece a este cliente");
            }
            
            // Usar el método del cliente para cancelar el turno
            if (cliente.cancelarTurno(idTurno)) {
                // Crear notificación de cancelación
                Notificacion notificacion = Notificacion.crearAviso(
                    turno.getIdTurno(),
                    "Su turno ha sido cancelado exitosamente"
                );
                turno.getNotificaciones().add(notificacion);
                
                System.out.println("Turno cancelado exitosamente por cliente: " + cliente.getNombre());
                return true;
            }
            
            return false;
            
        } catch (IllegalStateException e) {
            System.out.println("Error al cancelar turno: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Método para listar todos los turnos - FUNCIONALIDAD DEL SISTEMA
     * @return Lista de todos los turnos
     */
    public List<Turno> listarTurnos() {
        System.out.println("=== LISTADO DE TURNOS ===");
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos registrados en el sistema");
            return new ArrayList<>();
        }
        
        for (Turno turno : turnos) {
            System.out.println(turno.toString());
        }
        
        System.out.println("Total de turnos: " + turnos.size());
        return new ArrayList<>(turnos);
    }
    
    /**
     * Método para buscar turno por ID - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno a buscar
     * @return El turno encontrado o null si no existe
     */
    public Turno buscarTurnoPorId(int idTurno) {
        for (Turno turno : turnos) {
            if (turno.getIdTurno() == idTurno) {
                return turno;
            }
        }
        return null;
    }
    
    /**
     * Método para buscar turnos por cliente - FUNCIONALIDAD DEL SISTEMA
     * @param idCliente ID del cliente
     * @return Lista de turnos del cliente
     */
    public List<Turno> buscarTurnosPorCliente(int idCliente) {
        return turnos.stream()
                .filter(turno -> turno.getIdCliente() == idCliente)
                .collect(Collectors.toList());
    }
    
    /**
     * Método para buscar turnos por fecha - FUNCIONALIDAD DEL SISTEMA
     * @param fecha Fecha a buscar
     * @return Lista de turnos en esa fecha
     */
    public List<Turno> buscarTurnosPorFecha(LocalDate fecha) {
        return turnos.stream()
                .filter(turno -> turno.getFecha().equals(fecha))
                .collect(Collectors.toList());
    }
    
    /**
     * Método para buscar turnos por estado - FUNCIONALIDAD DEL SISTEMA
     * @param estado Estado a buscar
     * @return Lista de turnos con ese estado
     */
    public List<Turno> buscarTurnosPorEstado(String estado) {
        return turnos.stream()
                .filter(turno -> turno.getEstado().equals(estado))
                .collect(Collectors.toList());
    }
    
    /**
     * Método para obtener estadísticas del sistema - FUNCIONALIDAD DEL SISTEMA
     * @return String con estadísticas
     */
    public String obtenerEstadisticas() {
        int totalTurnos = turnos.size();
        int turnosPendientes = (int) turnos.stream().filter(t -> "Pendiente".equals(t.getEstado())).count();
        int turnosConfirmados = (int) turnos.stream().filter(t -> "Confirmado".equals(t.getEstado())).count();
        int turnosCancelados = (int) turnos.stream().filter(t -> "Cancelado".equals(t.getEstado())).count();
        
        return String.format(
            "=== ESTADÍSTICAS DEL SISTEMA ===\n" +
            "Total de turnos: %d\n" +
            "Turnos pendientes: %d\n" +
            "Turnos confirmados: %d\n" +
            "Turnos cancelados: %d\n" +
            "================================",
            totalTurnos, turnosPendientes, turnosConfirmados, turnosCancelados
        );
    }
    
    /**
     * Método para limpiar turnos cancelados antiguos - FUNCIONALIDAD DEL SISTEMA
     * @param diasAntiguedad Días de antigüedad para considerar un turno como antiguo
     * @return Número de turnos eliminados
     */
    public int limpiarTurnosAntiguos(int diasAntiguedad) {
        LocalDate fechaLimite = LocalDate.now().minusDays(diasAntiguedad);
        
        List<Turno> turnosAEliminar = turnos.stream()
                .filter(turno -> "Cancelado".equals(turno.getEstado()) && 
                                turno.getFecha().isBefore(fechaLimite))
                .collect(Collectors.toList());
        
        turnos.removeAll(turnosAEliminar);
        
        System.out.println("Se eliminaron " + turnosAEliminar.size() + " turnos cancelados antiguos");
        return turnosAEliminar.size();
    }
}
