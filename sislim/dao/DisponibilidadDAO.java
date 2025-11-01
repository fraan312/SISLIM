package sislim.dao;

import sislim.model.Disponibilidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DisponibilidadDAO - Data Access Object para Disponibilidad
 * Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase maneja todas las operaciones de acceso a datos relacionadas con Disponibilidades
 * en la base de datos MySQL. Implementa las operaciones CRUD.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Métodos privados y públicos bien definidos
 * - COMPOSICIÓN: Utiliza ConexionBD y Disponibilidad
 */
public class DisponibilidadDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor - ENCAPSULAMIENTO
     */
    public DisponibilidadDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Método para crear una disponibilidad en la base de datos - CREATE (CRUD)
     * @param disponibilidad La disponibilidad a crear
     * @return La disponibilidad creada con su ID asignado, o null si hay error
     */
    public Disponibilidad crear(Disponibilidad disponibilidad) {
        String sql = "INSERT INTO Disponibilidad (fecha, horaInicio, horaFin, zona, servicio) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, Date.valueOf(disponibilidad.getFecha()));
            stmt.setTime(2, Time.valueOf(disponibilidad.getHoraInicio()));
            stmt.setTime(3, Time.valueOf(disponibilidad.getHoraFin()));
            
            if (disponibilidad.getZona() != null && !disponibilidad.getZona().isEmpty()) {
                stmt.setString(4, disponibilidad.getZona());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            if (disponibilidad.getServicio() != null && !disponibilidad.getServicio().isEmpty()) {
                stmt.setString(5, disponibilidad.getServicio());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        disponibilidad.setIdDisponibilidad(idGenerado);
                        System.out.println("Disponibilidad creada en BD con ID: " + idGenerado);
                        return disponibilidad;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear disponibilidad en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer una disponibilidad por ID - READ (CRUD)
     * @param idDisponibilidad ID de la disponibilidad a buscar
     * @return La disponibilidad encontrada o null si no existe
     */
    public Disponibilidad leerPorId(int idDisponibilidad) {
        String sql = "SELECT * FROM Disponibilidad WHERE idDisponibilidad = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDisponibilidad);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearDisponibilidad(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer disponibilidad por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer todas las disponibilidades - READ (CRUD)
     * @return Lista de todas las disponibilidades
     */
    public List<Disponibilidad> leerTodas() {
        List<Disponibilidad> disponibilidades = new ArrayList<>();
        String sql = "SELECT * FROM Disponibilidad ORDER BY fecha, horaInicio";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                disponibilidades.add(mapearDisponibilidad(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer todas las disponibilidades: " + e.getMessage());
            e.printStackTrace();
        }
        
        return disponibilidades;
    }
    
    /**
     * Método para actualizar una disponibilidad - UPDATE (CRUD)
     * @param disponibilidad La disponibilidad a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Disponibilidad disponibilidad) {
        String sql = "UPDATE Disponibilidad SET fecha = ?, horaInicio = ?, horaFin = ?, zona = ?, servicio = ? " +
                     "WHERE idDisponibilidad = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(disponibilidad.getFecha()));
            stmt.setTime(2, Time.valueOf(disponibilidad.getHoraInicio()));
            stmt.setTime(3, Time.valueOf(disponibilidad.getHoraFin()));
            
            if (disponibilidad.getZona() != null && !disponibilidad.getZona().isEmpty()) {
                stmt.setString(4, disponibilidad.getZona());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            if (disponibilidad.getServicio() != null && !disponibilidad.getServicio().isEmpty()) {
                stmt.setString(5, disponibilidad.getServicio());
            } else {
                stmt.setNull(5, Types.VARCHAR);
            }
            
            stmt.setInt(6, disponibilidad.getIdDisponibilidad());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Disponibilidad actualizada en BD con ID: " + disponibilidad.getIdDisponibilidad());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar disponibilidad en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método para eliminar una disponibilidad - DELETE (CRUD)
     * @param idDisponibilidad ID de la disponibilidad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idDisponibilidad) {
        String sql = "DELETE FROM Disponibilidad WHERE idDisponibilidad = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idDisponibilidad);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Disponibilidad eliminada de BD con ID: " + idDisponibilidad);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar disponibilidad de BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método privado para mapear un ResultSet a un objeto Disponibilidad - ENCAPSULAMIENTO
     * @param rs ResultSet con los datos de la disponibilidad
     * @return Objeto Disponibilidad mapeado
     * @throws SQLException Si hay error al leer los datos
     */
    private Disponibilidad mapearDisponibilidad(ResultSet rs) throws SQLException {
        Disponibilidad disponibilidad = new Disponibilidad();
        disponibilidad.setIdDisponibilidad(rs.getInt("idDisponibilidad"));
        disponibilidad.setFecha(rs.getDate("fecha").toLocalDate());
        disponibilidad.setHoraInicio(rs.getTime("horaInicio").toLocalTime());
        disponibilidad.setHoraFin(rs.getTime("horaFin").toLocalTime());
        
        String zona = rs.getString("zona");
        if (zona != null) {
            disponibilidad.setZona(zona);
        }
        
        String servicio = rs.getString("servicio");
        if (servicio != null) {
            disponibilidad.setServicio(servicio);
        }
        
        disponibilidad.setDisponible(true); // Por defecto disponible
        
        return disponibilidad;
    }
}

