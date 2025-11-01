package sislim.dao;

import sislim.model.Administrador;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase AdministradorDAO - Data Access Object para Administrador
 * Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase maneja todas las operaciones de acceso a datos relacionadas con Administradores
 * en la base de datos MySQL. Implementa las operaciones CRUD.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Métodos privados y públicos bien definidos
 * - COMPOSICIÓN: Utiliza ConexionBD y Administrador
 */
public class AdministradorDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor - ENCAPSULAMIENTO
     */
    public AdministradorDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Método para crear un administrador en la base de datos - CREATE (CRUD)
     * @param administrador El administrador a crear
     * @return El administrador creado con su ID asignado, o null si hay error
     */
    public Administrador crear(Administrador administrador) {
        String sql = "INSERT INTO Administrador (nombre, email) VALUES (?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, administrador.getNombre());
            stmt.setString(2, administrador.getEmail());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        administrador.setId(idGenerado);
                        System.out.println("Administrador creado en BD con ID: " + idGenerado);
                        return administrador;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear administrador en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer un administrador por ID - READ (CRUD)
     * @param idAdmin ID del administrador a buscar
     * @return El administrador encontrado o null si no existe
     */
    public Administrador leerPorId(int idAdmin) {
        String sql = "SELECT * FROM Administrador WHERE idAdmin = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAdmin);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearAdministrador(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer administrador por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer todos los administradores - READ (CRUD)
     * @return Lista de todos los administradores
     */
    public List<Administrador> leerTodos() {
        List<Administrador> administradores = new ArrayList<>();
        String sql = "SELECT * FROM Administrador ORDER BY nombre";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                administradores.add(mapearAdministrador(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer todos los administradores: " + e.getMessage());
            e.printStackTrace();
        }
        
        return administradores;
    }
    
    /**
     * Método para actualizar un administrador - UPDATE (CRUD)
     * @param administrador El administrador a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Administrador administrador) {
        String sql = "UPDATE Administrador SET nombre = ?, email = ? WHERE idAdmin = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, administrador.getNombre());
            stmt.setString(2, administrador.getEmail());
            stmt.setInt(3, administrador.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Administrador actualizado en BD con ID: " + administrador.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar administrador en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método para eliminar un administrador - DELETE (CRUD)
     * @param idAdmin ID del administrador a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idAdmin) {
        String sql = "DELETE FROM Administrador WHERE idAdmin = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idAdmin);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Administrador eliminado de BD con ID: " + idAdmin);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar administrador de BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método privado para mapear un ResultSet a un objeto Administrador - ENCAPSULAMIENTO
     * @param rs ResultSet con los datos del administrador
     * @return Objeto Administrador mapeado
     * @throws SQLException Si hay error al leer los datos
     */
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        Administrador administrador = new Administrador();
        administrador.setId(rs.getInt("idAdmin"));
        administrador.setNombre(rs.getString("nombre"));
        administrador.setEmail(rs.getString("email"));
        // La tabla Administrador no tiene telefono en el esquema SQL
        
        return administrador;
    }
}

