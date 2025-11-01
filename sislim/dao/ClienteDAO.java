package sislim.dao;

import sislim.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase ClienteDAO - Data Access Object para Cliente
 * Aplicación del concepto de ENCAPSULAMIENTO
 * 
 * Esta clase maneja todas las operaciones de acceso a datos relacionadas con Clientes
 * en la base de datos MySQL. Implementa las operaciones CRUD.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Métodos privados y públicos bien definidos
 * - COMPOSICIÓN: Utiliza ConexionBD y Cliente
 */
public class ClienteDAO {
    
    private ConexionBD conexionBD;
    
    /**
     * Constructor - ENCAPSULAMIENTO
     */
    public ClienteDAO() {
        this.conexionBD = ConexionBD.getInstancia();
    }
    
    /**
     * Método para crear un cliente en la base de datos - CREATE (CRUD)
     * @param cliente El cliente a crear
     * @return El cliente creado con su ID asignado, o null si hay error
     */
    public Cliente crear(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre, email, telefono, direccion) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            
            if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
                stmt.setString(3, cliente.getTelefono());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            
            if (cliente.getDireccion() != null && !cliente.getDireccion().isEmpty()) {
                stmt.setString(4, cliente.getDireccion());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        cliente.setId(idGenerado);
                        System.out.println("Cliente creado en BD con ID: " + idGenerado);
                        return cliente;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al crear cliente en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer un cliente por ID - READ (CRUD)
     * @param idCliente ID del cliente a buscar
     * @return El cliente encontrado o null si no existe
     */
    public Cliente leerPorId(int idCliente) {
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer cliente por ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Método para leer todos los clientes - READ (CRUD)
     * @return Lista de todos los clientes
     */
    public List<Cliente> leerTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente ORDER BY nombre";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error al leer todos los clientes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clientes;
    }
    
    /**
     * Método para actualizar un cliente - UPDATE (CRUD)
     * @param cliente El cliente a actualizar
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre = ?, email = ?, telefono = ?, direccion = ? WHERE idCliente = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            
            if (cliente.getTelefono() != null && !cliente.getTelefono().isEmpty()) {
                stmt.setString(3, cliente.getTelefono());
            } else {
                stmt.setNull(3, Types.VARCHAR);
            }
            
            if (cliente.getDireccion() != null && !cliente.getDireccion().isEmpty()) {
                stmt.setString(4, cliente.getDireccion());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            
            stmt.setInt(5, cliente.getId());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Cliente actualizado en BD con ID: " + cliente.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente en BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método para eliminar un cliente - DELETE (CRUD)
     * @param idCliente ID del cliente a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM Cliente WHERE idCliente = ?";
        
        try (Connection conn = conexionBD.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idCliente);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                System.out.println("Cliente eliminado de BD con ID: " + idCliente);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente de BD: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Método privado para mapear un ResultSet a un objeto Cliente - ENCAPSULAMIENTO
     * @param rs ResultSet con los datos del cliente
     * @return Objeto Cliente mapeado
     * @throws SQLException Si hay error al leer los datos
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("idCliente"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setEmail(rs.getString("email"));
        
        String telefono = rs.getString("telefono");
        if (telefono != null) {
            cliente.setTelefono(telefono);
        }
        
        String direccion = rs.getString("direccion");
        if (direccion != null) {
            cliente.setDireccion(direccion);
        }
        
        return cliente;
    }
}

