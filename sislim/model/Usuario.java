package sislim.model;

/**
 * Clase abstracta Usuario - Aplicación del concepto de ABSTRACCIÓN
 * 
 * Esta clase define la estructura común para todos los usuarios del sistema SISLIM.
 * Al ser abstracta, no puede ser instanciada directamente, pero proporciona
 * la base común para Cliente y Administrador.
 * 
 * Conceptos de POO aplicados:
 * - ABSTRACCIÓN: Clase abstracta que define comportamiento común
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos de acceso
 */
public abstract class Usuario {
    
    // Atributos privados - ENCAPSULAMIENTO
    private int id;
    private String nombre;
    private String email;
    private String telefono;
    
    /**
     * Constructor por defecto - ENCAPSULAMIENTO
     */
    public Usuario() {
        this.id = 0;
        this.nombre = "";
        this.email = "";
        this.telefono = "";
    }
    
    /**
     * Constructor con parámetros - ENCAPSULAMIENTO
     * @param id Identificador único del usuario
     * @param nombre Nombre completo del usuario
     * @param email Correo electrónico del usuario
     * @param telefono Número de teléfono del usuario
     */
    public Usuario(int id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Métodos getters - ENCAPSULAMIENTO
    public int getId() {
        return id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    // Métodos setters - ENCAPSULAMIENTO
    public void setId(int id) {
        this.id = id;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    /**
     * Método abstracto para login - ABSTRACCIÓN
     * Cada tipo de usuario implementará su propia lógica de login
     * @return true si el login es exitoso, false en caso contrario
     */
    public abstract boolean login();
    
    /**
     * Método concreto que puede ser usado por todas las subclases
     * @return String con la información básica del usuario
     */
    public String getInformacionBasica() {
        return "ID: " + id + ", Nombre: " + nombre + ", Email: " + email;
    }
    
    /**
     * Método toString sobrescrito - POLIMORFISMO
     * @return String con la representación del objeto
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
