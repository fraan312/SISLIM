package sislim.dao;

import sislim.model.Turno;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase TurnoDAO - Data Access Object para Turno
 * Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase maneja todas las operaciones de acceso a datos relacionadas con Turnos
 * en la base de datos MySQL. Implementa las operaciones CRUD (Create, Read, Update, Delete).
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Métodos privados y públicos bien definidos
 * - COMPOSICIÓN: Utiliza ConexionBD y Turno
 */
public class TurnoDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor - ENCAPSULAMIENTO
     */
    public TurnoDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Método para crear un turno en la base de datos - CREATE (CRUD)
     * @param turno El turno a crear
     * @return El turno creado con su ID asignado, o null si hay error
     */
    public Turno crear(Turno turno) {
        String sql = "INSERT INTO Turno (fecha, hora, duracion, tipoServicio, estado, observaciones, " +
                     "idCliente, idDisponibilidad, idAdmin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Establecer parámetros
            stmt.setDate(1, Date.valueOf(turno.getFecha()));
            stmt.setTime(2, Time.valueOf(turno.getHora()));
            stmt.setInt(3, turno.getDuracion());
            stmt.setString(4, turno.getTipoServicio());
            stmt.setString(5, turno.getEstado());
            stmt.setString(6, turno.getObservaciones());
            stmt.setInt(7, turno.getIdCliente());
            stmt.setInt(8, turno.getIdDisponibilidad());
            
            if (turno.getIdAdmin() > 0) {
                stmt.setInt(9, turno.getIdAdmin());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            
            // Ejecutar inserción
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                // Obtener el ID generado
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        turno.setIdTurno(idGenerado);
                        System.out.println("Turno creado en BD con ID: " + idGenerado);
                        return turno;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear turno en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer un turno por ID - READ (CRUD)
     * @param idTurno ID del turno a buscar
     * @return El turno encontrado o null si no existe
     */
    public Turno leerPorId(int idTurno) {
        String sql = "SELECT * FROM Turno WHERE idTurno = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTurno);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTurno(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer turno por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer todos los turnos - READ (CRUD)
     * @return Lista de todos los turnos
     */
    public List<Turno> leerTodos() {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT * FROM Turno ORDER BY fecha, hora";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                turnos.add(mapearTurno(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer todos los turnos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return turnos;
    }
    
    /**
     * Método para actualizar un turno - UPDATE (CRUD)
     * @param turno El turno a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Turno turno) {
        String sql = "UPDATE Turno SET fecha = ?, hora = ?, duracion = ?, tipoServicio = ?, " +
                     "estado = ?, observaciones = ?, idCliente = ?, idDisponibilidad = ?, " +
                     "idAdmin = ? WHERE idTurno = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(turno.getFecha()));
            stmt.setTime(2, Time.valueOf(turno.getHora()));
            stmt.setInt(3, turno.getDuracion());
            stmt.setString(4, turno.getTipoServicio());
            stmt.setString(5, turno.getEstado());
            stmt.setString(6, turno.getObservaciones());
            stmt.setInt(7, turno.getIdCliente());
            stmt.setInt(8, turno.getIdDisponibilidad());
            
            if (turno.getIdAdmin() > 0) {
                stmt.setInt(9, turno.getIdAdmin());
            } else {
                stmt.setNull(9, Types.INTEGER);
            }
            
            stmt.setInt(10, turno.getIdTurno());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Turno actualizado en BD con ID: " + turno.getIdTurno());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar turno en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método para eliminar un turno - DELETE (CRUD)
     * @param idTurno ID del turno a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idTurno) {
        String sql = "DELETE FROM Turno WHERE idTurno = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTurno);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Turno eliminado de BD con ID: " + idTurno);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar turno de BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método para buscar turnos por cliente - FUNCIONALIDAD DEL SISTEMA
     * @param idCliente ID del cliente
     * @return Lista de turnos del cliente
     */
    public List<Turno> buscarPorCliente(int idCliente) {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT * FROM Turno WHERE idCliente = ? ORDER BY fecha, hora";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turnos.add(mapearTurno(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar turnos por cliente: " + e.getMessage());
            e.printStackTrace();
        }
        
        return turnos;
    }
    
    /**
     * Método para buscar turnos por estado - FUNCIONALIDAD DEL SISTEMA
     * @param estado Estado a buscar
     * @return Lista de turnos con ese estado
     */
    public List<Turno> buscarPorEstado(String estado) {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT * FROM Turno WHERE estado = ? ORDER BY fecha, hora";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, estado);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turnos.add(mapearTurno(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar turnos por estado: " + e.getMessage());
            e.printStackTrace();
        }
        
        return turnos;
    }
    
    /**
     * Método para buscar turnos por fecha - FUNCIONALIDAD DEL SISTEMA
     * @param fecha Fecha a buscar
     * @return Lista de turnos en esa fecha
     */
    public List<Turno> buscarPorFecha(LocalDate fecha) {
        List<Turno> turnos = new ArrayList<>();
        String sql = "SELECT * FROM Turno WHERE fecha = ? ORDER BY hora";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    turnos.add(mapearTurno(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar turnos por fecha: " + e.getMessage());
            e.printStackTrace();
        }
        
        return turnos;
    }
    
    /**
     * Método para verificar si hay conflicto de horarios - FUNCIONALIDAD DEL SISTEMA
     * @param fecha Fecha del turno
     * @param hora Hora del turno
     * @param idTurnoExcluir ID del turno a excluir de la verificación (para actualizaciones)
     * @return true si hay conflicto, false si no hay conflicto
     */
    public boolean existeConflictoHorario(LocalDate fecha, LocalTime hora, int idTurnoExcluir) {
        String sql = "SELECT COUNT(*) FROM Turno WHERE fecha = ? AND hora = ? AND estado != 'Cancelado'";
        
        if (idTurnoExcluir > 0) {
            sql += " AND idTurno != ?";
        }
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fecha));
            stmt.setTime(2, Time.valueOf(hora));
            
            if (idTurnoExcluir > 0) {
                stmt.setInt(3, idTurnoExcluir);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al verificar conflicto de horario: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método privado para mapear un ResultSet a un objeto Turno - ENCAPSULAMIENTO
     * @param rs ResultSet con los datos del turno
     * @return Objeto Turno mapeado
     * @throws SQLException Si hay error al leer los datos
     */
    private Turno mapearTurno(ResultSet rs) throws SQLException {
        Turno turno = new Turno();
        turno.setIdTurno(rs.getInt("idTurno"));
        turno.setFecha(rs.getDate("fecha").toLocalDate());
        turno.setHora(rs.getTime("hora").toLocalTime());
        turno.setDuracion(rs.getInt("duracion"));
        turno.setTipoServicio(rs.getString("tipoServicio"));
        turno.setEstado(rs.getString("estado"));
        turno.setObservaciones(rs.getString("observaciones"));
        turno.setIdCliente(rs.getInt("idCliente"));
        turno.setIdDisponibilidad(rs.getInt("idDisponibilidad"));
        
        int idAdmin = rs.getInt("idAdmin");
        if (!rs.wasNull()) {
            turno.setIdAdmin(idAdmin);
        }
        
        return turno;
    }
}

