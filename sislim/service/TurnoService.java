package sislim.service;

import sislim.model.*;
import sislim.dao.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase TurnoService - Aplicación del concepto de ENCAPSULAMIENTO y MVC
 * 
 * Esta clase proporciona servicios para la gestión de turnos en el sistema SISLIM.
 * Maneja la lógica de negocio relacionada con turnos, incluyendo reservas,
 * confirmaciones, cancelaciones y búsquedas.
 * 
 * En el TP4, esta clase actúa como CONTROLADOR en el patrón MVC:
 * - MODEL: Turno, Cliente, Administrador (clases del modelo)
 * - VIEW: SISLIMSwing.java (interfaz gráfica)
 * - CONTROLLER: TurnoService (esta clase - lógica de negocio)
 * 
 * Utiliza DAO (Data Access Object) para persistencia en MySQL con JDBC.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - COMPOSICIÓN: Utiliza las clases del modelo y DAOs
 */
public class TurnoService {
    
    // DAO para acceso a datos - ENCAPSULAMIENTO y COMPOSICIÓN
    private TurnoDAO turnoDAO;
    private NotificacionDAO notificacionDAO;
    private DisponibilidadDAO disponibilidadDAO;
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     * Inicializa los DAOs para acceso a la base de datos
     */
    public TurnoService() {
        this.turnoDAO = new TurnoDAO();
        this.notificacionDAO = new NotificacionDAO();
        this.disponibilidadDAO = new DisponibilidadDAO();
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    /**
     * Método para obtener todos los turnos desde la base de datos - READ (CRUD)
     * @return Lista de todos los turnos
     */
    public List<Turno> getTurnos() {
        return turnoDAO.leerTodos();
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
            
            // Verificar que no haya conflicto de horarios usando DAO - FUNCIONALIDAD DEL SISTEMA
            if (turnoDAO.existeConflictoHorario(fecha, hora, 0)) {
                throw new IllegalStateException("Ya existe un turno reservado para esa fecha y hora. Por favor, seleccione otro horario.");
            }
            
            // Buscar una disponibilidad desde la BD que coincida con la fecha - READ (CRUD)
            List<Disponibilidad> disponibilidades = disponibilidadDAO.leerTodas();
            int idDisponibilidad = 0;
            
            // Buscar una disponibilidad que coincida con la fecha
            for (Disponibilidad disp : disponibilidades) {
                if (disp.getFecha().equals(fecha) && disp.isDisponible()) {
                    idDisponibilidad = disp.getIdDisponibilidad();
                    break;
                }
            }
            
            // Si no se encuentra disponibilidad, usar la primera disponible
            if (idDisponibilidad == 0 && !disponibilidades.isEmpty()) {
                for (Disponibilidad disp : disponibilidades) {
                    if (disp.isDisponible()) {
                        idDisponibilidad = disp.getIdDisponibilidad();
                        break;
                    }
                }
            }
            
            // Si aún no hay disponibilidad, lanzar excepción
            if (idDisponibilidad == 0) {
                throw new IllegalStateException("No hay disponibilidades en la base de datos. Por favor, agregue disponibilidades primero.");
            }
            
            // Crear el nuevo turno
            Turno nuevoTurno = new Turno();
            nuevoTurno.setFecha(fecha);
            nuevoTurno.setHora(hora);
            nuevoTurno.setDuracion(duracion);
            nuevoTurno.setTipoServicio(tipoServicio);
            nuevoTurno.setObservaciones(observaciones != null ? observaciones : "");
            nuevoTurno.setIdCliente(cliente.getId());
            nuevoTurno.setEstado("Pendiente");
            nuevoTurno.setIdDisponibilidad(idDisponibilidad); // Asignar disponibilidad desde BD - READ (CRUD)
            
            // Usar el método del cliente para solicitar el turno (validación)
            if (cliente.solicitarTurno(nuevoTurno)) {
                // Persistir el turno en la base de datos usando DAO - CREATE (CRUD)
                Turno turnoGuardado = turnoDAO.crear(nuevoTurno);
                
                if (turnoGuardado != null) {
                    // Crear notificación de confirmación y guardarla en BD
                    Notificacion notificacion = Notificacion.crearConfirmacion(
                        turnoGuardado.getIdTurno(), 
                        "Su turno ha sido reservado exitosamente para el " + fecha + " a las " + hora
                    );
                    notificacionDAO.crear(notificacion);
                    
                    System.out.println("Turno reservado exitosamente con ID: " + turnoGuardado.getIdTurno());
                    return turnoGuardado;
                } else {
                    throw new IllegalStateException("Error al guardar el turno en la base de datos");
                }
            }
            
            return null;
            
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
            // Buscar el turno desde la base de datos - READ (CRUD)
            Turno turno = turnoDAO.leerPorId(idTurno);
            if (turno == null) {
                throw new IllegalStateException("No se encontró el turno con ID: " + idTurno);
            }
            
            // Usar el método del administrador para confirmar el turno (validación)
            if (administrador.confirmarTurno(turno)) {
                // Actualizar el turno en la base de datos - UPDATE (CRUD)
                if (turnoDAO.actualizar(turno)) {
                    // Crear notificación de confirmación y guardarla en BD
                    Notificacion notificacion = Notificacion.crearConfirmacion(
                        turno.getIdTurno(),
                        "Su turno ha sido confirmado por el administrador"
                    );
                    notificacionDAO.crear(notificacion);
                    
                    System.out.println("Turno confirmado exitosamente por administrador: " + administrador.getNombre());
                    return true;
                }
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
            // Buscar el turno desde la base de datos - READ (CRUD)
            Turno turno = turnoDAO.leerPorId(idTurno);
            if (turno == null) {
                throw new IllegalStateException("No se encontró el turno con ID: " + idTurno);
            }
            
            // Verificar que el turno pertenece al cliente
            if (turno.getIdCliente() != cliente.getId()) {
                throw new IllegalStateException("El turno no pertenece a este cliente");
            }
            
            // Validación: verificar que el turno no esté ya cancelado
            if ("Cancelado".equals(turno.getEstado())) {
                throw new IllegalStateException("El turno ya está cancelado");
            }
            
            // Cambiar el estado del turno a cancelado (actualizar directamente el objeto de BD)
            turno.setEstado("Cancelado");
            
            // Actualizar el turno en la base de datos - UPDATE (CRUD)
            if (turnoDAO.actualizar(turno)) {
                // Crear notificación de cancelación y guardarla en BD
                Notificacion notificacion = Notificacion.crearAviso(
                    turno.getIdTurno(),
                    "Su turno ha sido cancelado exitosamente"
                );
                notificacionDAO.crear(notificacion);
                
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
        List<Turno> turnos = turnoDAO.leerTodos(); // Leer desde BD - READ (CRUD)
        
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos registrados en el sistema");
            return new ArrayList<>();
        }
        
        for (Turno turno : turnos) {
            System.out.println(turno.toString());
        }
        
        System.out.println("Total de turnos: " + turnos.size());
        return turnos;
    }
    
    /**
     * Método para buscar turno por ID - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno a buscar
     * @return El turno encontrado o null si no existe
     */
    public Turno buscarTurnoPorId(int idTurno) {
        return turnoDAO.leerPorId(idTurno); // Leer desde BD - READ (CRUD)
    }
    
    /**
     * Método para buscar turnos por cliente - FUNCIONALIDAD DEL SISTEMA
     * @param idCliente ID del cliente
     * @return Lista de turnos del cliente
     */
    public List<Turno> buscarTurnosPorCliente(int idCliente) {
        return turnoDAO.buscarPorCliente(idCliente); // Buscar en BD
    }
    
    /**
     * Método para buscar turnos por fecha - FUNCIONALIDAD DEL SISTEMA
     * @param fecha Fecha a buscar
     * @return Lista de turnos en esa fecha
     */
    public List<Turno> buscarTurnosPorFecha(LocalDate fecha) {
        return turnoDAO.buscarPorFecha(fecha); // Buscar en BD
    }
    
    /**
     * Método para buscar turnos por estado - FUNCIONALIDAD DEL SISTEMA
     * @param estado Estado a buscar
     * @return Lista de turnos con ese estado
     */
    public List<Turno> buscarTurnosPorEstado(String estado) {
        return turnoDAO.buscarPorEstado(estado); // Buscar en BD
    }
    
    /**
     * Método para obtener estadísticas del sistema - FUNCIONALIDAD DEL SISTEMA
     * @return String con estadísticas
     */
    public String obtenerEstadisticas() {
        // Leer todos los turnos desde BD para calcular estadísticas - READ (CRUD)
        List<Turno> turnos = turnoDAO.leerTodos();
        
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
        
        // Leer turnos cancelados desde BD - READ (CRUD)
        List<Turno> turnosCancelados = turnoDAO.buscarPorEstado("Cancelado");
        
        List<Turno> turnosAEliminar = turnosCancelados.stream()
                .filter(turno -> turno.getFecha().isBefore(fechaLimite))
                .collect(Collectors.toList());
        
        // Eliminar de la base de datos - DELETE (CRUD)
        int eliminados = 0;
        for (Turno turno : turnosAEliminar) {
            if (turnoDAO.eliminar(turno.getIdTurno())) {
                eliminados++;
            }
        }
        
        System.out.println("Se eliminaron " + eliminados + " turnos cancelados antiguos");
        return eliminados;
    }
}
