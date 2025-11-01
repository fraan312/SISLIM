package sislim.dao;

import sislim.model.Notificacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase NotificacionDAO - Data Access Object para Notificacion
 * Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase maneja todas las operaciones de acceso a datos relacionadas con Notificaciones
 * en la base de datos MySQL. Implementa las operaciones CRUD.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Métodos privados y públicos bien definidos
 * - COMPOSICIÓN: Utiliza ConexionBD y Notificacion
 */
public class NotificacionDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor - ENCAPSULAMIENTO
     */
    public NotificacionDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Método para crear una notificación en la base de datos - CREATE (CRUD)
     * @param notificacion La notificación a crear
     * @return La notificación creada con su ID asignado, o null si hay error
     */
    public Notificacion crear(Notificacion notificacion) {
        String sql = "INSERT INTO Notificacion (mensaje, fechaEnvio, tipo, idTurno) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, notificacion.getMensaje());
            stmt.setTimestamp(2, Timestamp.valueOf(notificacion.getFechaEnvio()));
            stmt.setString(3, notificacion.getTipo());
            stmt.setInt(4, notificacion.getIdTurno());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        notificacion.setIdNotificacion(idGenerado);
                        notificacion.setEnviada(true); // Si está en BD, consideramos que fue enviada
                        System.out.println("Notificación creada en BD con ID: " + idGenerado);
                        return notificacion;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear notificación en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer una notificación por ID - READ (CRUD)
     * @param idNotificacion ID de la notificación a buscar
     * @return La notificación encontrada o null si no existe
     */
    public Notificacion leerPorId(int idNotificacion) {
        String sql = "SELECT * FROM Notificacion WHERE idNotificacion = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idNotificacion);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearNotificacion(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer notificación por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer todas las notificaciones - READ (CRUD)
     * @return Lista de todas las notificaciones
     */
    public List<Notificacion> leerTodas() {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion ORDER BY fechaEnvio DESC";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                notificaciones.add(mapearNotificacion(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer todas las notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notificaciones;
    }
    
    /**
     * Método para buscar notificaciones por turno - FUNCIONALIDAD DEL SISTEMA
     * @param idTurno ID del turno
     * @return Lista de notificaciones para ese turno
     */
    public List<Notificacion> buscarPorTurno(int idTurno) {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE idTurno = ? ORDER BY fechaEnvio DESC";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idTurno);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificaciones.add(mapearNotificacion(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar notificaciones por turno: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notificaciones;
    }
    
    /**
     * Método para buscar notificaciones por tipo - FUNCIONALIDAD DEL SISTEMA
     * @param tipo Tipo de notificación
     * @return Lista de notificaciones de ese tipo
     */
    public List<Notificacion> buscarPorTipo(String tipo) {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificacion WHERE tipo = ? ORDER BY fechaEnvio DESC";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, tipo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    notificaciones.add(mapearNotificacion(rs));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al buscar notificaciones por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return notificaciones;
    }
    
    /**
     * Método privado para mapear un ResultSet a un objeto Notificacion - ENCAPSULAMIENTO
     * @param rs ResultSet con los datos de la notificación
     * @return Objeto Notificacion mapeado
     * @throws SQLException Si hay error al leer los datos
     */
    private Notificacion mapearNotificacion(ResultSet rs) throws SQLException {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(rs.getInt("idNotificacion"));
        notificacion.setMensaje(rs.getString("mensaje"));
        
        Timestamp timestamp = rs.getTimestamp("fechaEnvio");
        if (timestamp != null) {
            notificacion.setFechaEnvio(timestamp.toLocalDateTime());
        }
        
        notificacion.setTipo(rs.getString("tipo"));
        notificacion.setIdTurno(rs.getInt("idTurno"));
        notificacion.setEnviada(true); // Si está en BD, consideramos que fue enviada
        
        return notificacion;
    }
}

