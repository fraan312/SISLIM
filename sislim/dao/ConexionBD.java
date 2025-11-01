package sislim.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase ConexionBD - Aplicación del concepto de ENCAPSULAMIENTO y SINGLETON
 * 
 * Esta clase gestiona la conexión a la base de datos MySQL del sistema SISLIM.
 * Utiliza el patrón Singleton para garantizar una única instancia de conexión.
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 * - SINGLETON: Patrón de diseño para una única instancia
 */
public class ConexionBD {
    
    // Atributos privados para la conexión - ENCAPSULAMIENTO
    private static ConexionBD instancia; // SINGLETON: Instancia única
    private Connection conexion;
    
    // Configuración de la base de datos - ENCAPSULAMIENTO
    private static final String URL = "jdbc:mysql://localhost:3306/sislim";
    private static final String USUARIO = "root";
    private static final String PASSWORD = ""; // Ajustar según tu configuración
    
    /**
     * Constructor privado para el patrón Singleton - ENCAPSULAMIENTO
     */
    private ConexionBD() {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver MySQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método para obtener la instancia única (Singleton) - SINGLETON
     * @return La instancia única de ConexionBD
     */
    public static ConexionBD getInstancia() {
        if (instancia == null) {
            instancia = new ConexionBD();
        }
        return instancia;
    }
    
    /**
     * Método para obtener una conexión a la base de datos - ENCAPSULAMIENTO
     * @return Connection objeto de conexión a MySQL
     * @throws SQLException Si hay un error al conectar
     */
    public Connection getConexion() throws SQLException {
        try {
            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
                System.out.println("Conexión a la base de datos establecida correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            throw e;
        }
        return conexion;
    }
    
    /**
     * Método para cerrar la conexión - ENCAPSULAMIENTO
     */
    public void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Método para verificar si la conexión está activa - ENCAPSULAMIENTO
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Método para probar la conexión - FUNCIONALIDAD DEL SISTEMA
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public boolean probarConexion() {
        try {
            Connection conn = getConexion();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Prueba de conexión exitosa");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error en la prueba de conexión: " + e.getMessage());
        }
        return false;
    }
}

