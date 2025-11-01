import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import sislim.model.*;
import sislim.service.*;
import sislim.dao.*;

/**
 * Aplicación principal de Swing para SISLIM
 * Interfaz gráfica moderna y fácil de usar
 * 
 * Conceptos de POO aplicados:
 * - ENCAPSULAMIENTO: Atributos privados con métodos públicos
 * - COMPOSICIÓN: Utiliza las clases del modelo y servicios
 * - POLIMORFISMO: Uso de interfaces y herencia
 */
public class SISLIMSwing extends JFrame {
    
    // Atributos privados - ENCAPSULAMIENTO
    private TurnoService turnoService;
    private NotificacionService notificacionService;
    private List<Cliente> clientes;
    private List<Administrador> administradores;
    private List<Disponibilidad> disponibilidades;
    
    // Componentes de la interfaz - ENCAPSULAMIENTO
    private JTextArea outputArea;
    private JComboBox<Cliente> clienteComboBox;
    private JComboBox<Administrador> adminComboBox;
    private JComboBox<String> horaComboBox;
    private JComboBox<Integer> duracionComboBox;
    private JComboBox<String> servicioComboBox;
    private JTextField fechaField;
    private JTextField observacionesField;
    
    // Labels del panel de información para actualizar después de cargar datos - ENCAPSULAMIENTO
    private JLabel infoClientesLabel;
    private JLabel infoAdminsLabel;
    private JLabel infoDisponibilidadesLabel;
    private JPanel infoPanel; // Panel de información completo para refrescar
    private JPanel listaClientesPanel; // Panel contenedor para la lista de clientes
    
    /**
     * Constructor principal - ENCAPSULAMIENTO
     */
    public SISLIMSwing() {
        // Configurar la ventana principal
        setTitle("SISLIM - Sistema de Gestión de Turnos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Inicializar listas vacías PRIMERO (para evitar NullPointerException)
        this.clientes = new java.util.ArrayList<>();
        this.administradores = new java.util.ArrayList<>();
        this.disponibilidades = new java.util.ArrayList<>();
        
        // Crear la interfaz
        crearInterfaz();
        
        // Inicializar servicios y datos DESPUÉS de crear la interfaz
        inicializarSistema();
        
        // Mostrar mensaje de bienvenida
        mostrarMensaje("=== BIENVENIDO AL SISTEMA SISLIM ===\n" +
                      "Sistema de Solicitud y Gestión de Turnos de Limpieza Domiciliaria\n" +
                      "=====================================\n");
    }
    
    /**
     * Método para inicializar el sistema - ENCAPSULAMIENTO
     * En el TP4, carga datos desde MySQL usando DAOs (Data Access Objects)
     * Todo debe venir de la base de datos, no se crean datos de prueba automáticamente
     */
    private void inicializarSistema() {
        this.turnoService = new TurnoService();
        this.notificacionService = new NotificacionService();
        
        // Verificar conexión a la base de datos
        ConexionBD conexionBD = ConexionBD.getInstancia();
        if (!conexionBD.probarConexion()) {
            mostrarMensaje("⚠️ ADVERTENCIA: No se pudo conectar a la base de datos MySQL.\n" +
                          "Asegúrese de que MySQL esté ejecutándose y la base de datos 'sislim' exista.\n" +
                          "El sistema no funcionará correctamente sin conexión a la base de datos.\n");
            
            // Las listas ya están inicializadas vacías en el constructor
            return;
        }
        
        // Cargar datos desde la base de datos usando DAOs - READ (CRUD)
        cargarDatosDesdeBD();
        
        // Si no hay datos, simplemente informar (los datos se insertan manualmente o con scripts SQL)
        if (clientes.isEmpty() && administradores.isEmpty()) {
            mostrarMensaje("ℹ️ La base de datos está vacía.\n" +
                          "Puede insertar datos manualmente usando las funcionalidades del sistema\n" +
                          "o ejecutando el script SQL de inserción de datos.\n");
        }
    }
    
    /**
     * Método para cargar datos desde la base de datos - READ (CRUD)
     * En el TP4, utiliza DAOs para leer datos de MySQL
     */
    private void cargarDatosDesdeBD() {
        try {
            ClienteDAO clienteDAO = new ClienteDAO();
            AdministradorDAO adminDAO = new AdministradorDAO();
            DisponibilidadDAO dispDAO = new DisponibilidadDAO();
            
            // Cargar desde BD - READ (CRUD)
            this.clientes = clienteDAO.leerTodos();
            this.administradores = adminDAO.leerTodos();
            this.disponibilidades = dispDAO.leerTodas();
            
            mostrarMensaje("✅ Datos cargados desde la base de datos:\n" +
                          "- Clientes: " + clientes.size() + "\n" +
                          "- Administradores: " + administradores.size() + "\n" +
                          "- Disponibilidades: " + disponibilidades.size() + "\n");
            
            // Actualizar los labels del panel de información después de cargar datos
            actualizarPanelInformacion();
            
        } catch (Exception e) {
            System.err.println("Error al cargar datos desde BD: " + e.getMessage());
            e.printStackTrace();
            
            // Las listas ya están inicializadas vacías en el constructor
            // Limpiar listas por si acaso hubo un error parcial
            this.clientes.clear();
            this.administradores.clear();
            this.disponibilidades.clear();
        }
    }
    
    
    /**
     * Método para crear la interfaz principal - ENCAPSULAMIENTO
     */
    private void crearInterfaz() {
        setLayout(new BorderLayout());
        
        // Crear el panel superior con título
        JPanel topPanel = crearPanelTitulo();
        add(topPanel, BorderLayout.NORTH);
        
        // Crear el panel central con área de salida
        JPanel centerPanel = crearPanelCentral();
        add(centerPanel, BorderLayout.CENTER);
        
        // Crear el panel derecho con botones
        JPanel rightPanel = crearPanelBotones();
        add(rightPanel, BorderLayout.EAST);
        
        // Crear el panel izquierdo con información
        JPanel leftPanel = crearPanelInformacion();
        add(leftPanel, BorderLayout.WEST);
    }
    
    /**
     * Método para crear el panel del título - ENCAPSULAMIENTO
     */
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titulo = new JLabel("SISLIM - Sistema de Gestión de Turnos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(titulo);
        return panel;
    }
    
    /**
     * Método para crear el panel central - ENCAPSULAMIENTO
     */
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Área de salida
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        outputArea.setBackground(new Color(248, 249, 250));
        outputArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Método para crear el panel de botones - ENCAPSULAMIENTO
     */
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 10, 20, 20));
        panel.setBackground(new Color(236, 240, 241));
        
        // Título del panel
        JLabel tituloPanel = new JLabel("FUNCIONALIDADES");
        tituloPanel.setFont(new Font("Arial", Font.BOLD, 16));
        tituloPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tituloPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Botón Reservar Turno
        JButton btnReservar = crearBoton("1. Reservar Turno", new Color(46, 204, 113), e -> mostrarVentanaReservar());
        panel.add(btnReservar);
        panel.add(Box.createVerticalStrut(10));
        
        // Botón Confirmar Turno
        JButton btnConfirmar = crearBoton("2. Confirmar Turno", new Color(52, 152, 219), e -> mostrarVentanaConfirmar());
        panel.add(btnConfirmar);
        panel.add(Box.createVerticalStrut(10));
        
        // Botón Cancelar Turno
        JButton btnCancelar = crearBoton("3. Cancelar Turno", new Color(230, 126, 34), e -> mostrarVentanaCancelar());
        panel.add(btnCancelar);
        panel.add(Box.createVerticalStrut(10));
        
        // Botón Listar Turnos
        JButton btnListar = crearBoton("4. Listar Turnos", new Color(155, 89, 182), e -> listarTurnos());
        panel.add(btnListar);
        panel.add(Box.createVerticalStrut(10));
        
        // Botón Ver Estadísticas
        JButton btnEstadisticas = crearBoton("5. Ver Estadísticas", new Color(96, 125, 139), e -> mostrarEstadisticas());
        panel.add(btnEstadisticas);
        panel.add(Box.createVerticalStrut(20));
        
        // Botón Limpiar Pantalla
        JButton btnLimpiar = crearBoton("Limpiar Pantalla", new Color(231, 76, 60), e -> outputArea.setText(""));
        panel.add(btnLimpiar);
        
        return panel;
    }
    
    /**
     * Método para crear botones con estilo - ENCAPSULAMIENTO
     */
    private JButton crearBoton(String texto, Color color, ActionListener listener) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(200, 35));
        boton.setMaximumSize(new Dimension(200, 35));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addActionListener(listener);
        
        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
        });
        
        return boton;
    }
    
    /**
     * Método para crear el panel de información - ENCAPSULAMIENTO
     */
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel();
        this.infoPanel = panel; // Guardar referencia para actualizar
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 10));
        panel.setBackground(new Color(236, 240, 241));
        
        // Título del panel
        JLabel tituloInfo = new JLabel("INFORMACIÓN DEL SISTEMA");
        tituloInfo.setFont(new Font("Arial", Font.BOLD, 16));
        tituloInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tituloInfo);
        panel.add(Box.createVerticalStrut(20));
        
        // Información de clientes
        infoClientesLabel = new JLabel("Clientes registrados: " + clientes.size());
        infoClientesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoClientesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoClientesLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Información de administradores
        infoAdminsLabel = new JLabel("Administradores: " + administradores.size());
        infoAdminsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoAdminsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoAdminsLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Información de disponibilidades
        infoDisponibilidadesLabel = new JLabel("Disponibilidades: " + disponibilidades.size());
        infoDisponibilidadesLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoDisponibilidadesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(infoDisponibilidadesLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Separador
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(20));
        
        // Lista de clientes - Panel contenedor que se actualizará dinámicamente
        JLabel tituloClientes = new JLabel("Clientes:");
        tituloClientes.setFont(new Font("Arial", Font.BOLD, 12));
        tituloClientes.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(tituloClientes);
        panel.add(Box.createVerticalStrut(10));
        
        listaClientesPanel = new JPanel();
        listaClientesPanel.setLayout(new BoxLayout(listaClientesPanel, BoxLayout.Y_AXIS));
        listaClientesPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        listaClientesPanel.setBackground(new Color(236, 240, 241));
        actualizarListaClientes();
        panel.add(listaClientesPanel);
        
        return panel;
    }
    
    /**
     * Método para actualizar la lista de clientes en el panel - ENCAPSULAMIENTO
     */
    private void actualizarListaClientes() {
        if (listaClientesPanel == null) {
            return;
        }
        
        // Limpiar el panel de clientes
        listaClientesPanel.removeAll();
        
        // Agregar clientes actualizados
        for (Cliente cliente : clientes) {
            JLabel clienteInfo = new JLabel("• " + cliente.getNombre());
            clienteInfo.setFont(new Font("Arial", Font.PLAIN, 10));
            clienteInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaClientesPanel.add(clienteInfo);
        }
    }
    
    /**
     * Método para actualizar el panel de información después de cargar datos - ENCAPSULAMIENTO
     */
    private void actualizarPanelInformacion() {
        if (infoClientesLabel != null) {
            infoClientesLabel.setText("Clientes registrados: " + clientes.size());
        }
        if (infoAdminsLabel != null) {
            infoAdminsLabel.setText("Administradores: " + administradores.size());
        }
        if (infoDisponibilidadesLabel != null) {
            infoDisponibilidadesLabel.setText("Disponibilidades: " + disponibilidades.size());
        }
        
        // Actualizar la lista de clientes en el panel
        actualizarListaClientes();
        
        // Refrescar el panel para mostrar los nuevos valores
        if (infoPanel != null) {
            infoPanel.revalidate();
            infoPanel.repaint();
        }
    }
    
    /**
     * Método para mostrar mensajes en el área de salida - ENCAPSULAMIENTO
     */
    private void mostrarMensaje(String mensaje) {
        outputArea.append(mensaje + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }
    
    /**
     * Método para mostrar la ventana de reservar turno - FUNCIONALIDAD DEL SISTEMA
     */
    private void mostrarVentanaReservar() {
        JDialog ventana = new JDialog(this, "Reservar Turno", true);
        ventana.setSize(400, 500);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("RESERVAR NUEVO TURNO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        
        // Cliente
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblCliente);
        clienteComboBox = new JComboBox<>(clientes.toArray(new Cliente[0]));
        clienteComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    Cliente cliente = (Cliente) value;
                    setText(cliente.getNombre() + " (" + cliente.getEmail() + ")");
                }
                return this;
            }
        });
        clienteComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(clienteComboBox);
        panel.add(Box.createVerticalStrut(10));
        
        // Fecha
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblFecha);
        fechaField = new JTextField(LocalDate.now().plusDays(1).toString());
        fechaField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(fechaField);
        panel.add(Box.createVerticalStrut(10));
        
        // Hora
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblHora);
        horaComboBox = new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "14:00", "15:00", "16:00"});
        horaComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(horaComboBox);
        panel.add(Box.createVerticalStrut(10));
        
        // Duración
        JLabel lblDuracion = new JLabel("Duración (minutos):");
        lblDuracion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDuracion);
        duracionComboBox = new JComboBox<>(new Integer[]{60, 90, 120, 180});
        duracionComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(duracionComboBox);
        panel.add(Box.createVerticalStrut(10));
        
        // Servicio
        JLabel lblServicio = new JLabel("Tipo de servicio:");
        lblServicio.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblServicio);
        servicioComboBox = new JComboBox<>(new String[]{"Limpieza básica", "Limpieza profunda", "Limpieza premium"});
        servicioComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(servicioComboBox);
        panel.add(Box.createVerticalStrut(10));
        
        // Observaciones
        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblObservaciones);
        observacionesField = new JTextField();
        observacionesField.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(observacionesField);
        panel.add(Box.createVerticalStrut(20));
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnReservar = new JButton("Reservar Turno");
        btnReservar.setBackground(new Color(46, 204, 113));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.addActionListener(e -> {
            try {
                Cliente cliente = (Cliente) clienteComboBox.getSelectedItem();
                LocalDate fecha = LocalDate.parse(fechaField.getText());
                LocalTime hora = LocalTime.parse((String) horaComboBox.getSelectedItem());
                int duracion = (Integer) duracionComboBox.getSelectedItem();
                String tipoServicio = (String) servicioComboBox.getSelectedItem();
                String observaciones = observacionesField.getText();
                
                Turno turno = turnoService.reservarTurno(cliente, fecha, hora, duracion, tipoServicio, observaciones);
                
                if (turno != null) {
                    mostrarMensaje("✅ TURNO RESERVADO EXITOSAMENTE\n" +
                                  "ID del turno: " + turno.getIdTurno() + "\n" +
                                  "Cliente: " + cliente.getNombre() + "\n" +
                                  "Fecha: " + fecha + "\n" +
                                  "Hora: " + hora + "\n" +
                                  "Servicio: " + tipoServicio + "\n" +
                                  "=====================================\n");
                    
                    // Enviar notificación
                    notificacionService.enviarConfirmacionTurno(turno);
                    
                    ventana.dispose();
                } else {
                    JOptionPane.showMessageDialog(ventana, "Error: No se pudo reservar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> ventana.dispose());
        
        buttonPanel.add(btnReservar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel);
        
        ventana.add(panel, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
    
    /**
     * Método para mostrar la ventana de confirmar turno - FUNCIONALIDAD DEL SISTEMA
     */
    private void mostrarVentanaConfirmar() {
        List<Turno> turnosPendientes = turnoService.buscarTurnosPorEstado("Pendiente");
        
        if (turnosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay turnos pendientes para confirmar.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        JDialog ventana = new JDialog(this, "Confirmar Turno", true);
        ventana.setSize(500, 400);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("CONFIRMAR TURNO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        
        // Administrador
        JLabel lblAdmin = new JLabel("Administrador:");
        lblAdmin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblAdmin);
        adminComboBox = new JComboBox<>(administradores.toArray(new Administrador[0]));
        adminComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Administrador) {
                    Administrador admin = (Administrador) value;
                    setText(admin.getNombre() + " (" + admin.getEmail() + ")");
                }
                return this;
            }
        });
        adminComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(adminComboBox);
        panel.add(Box.createVerticalStrut(20));
        
        // Lista de turnos
        JLabel lblTurnos = new JLabel("Turnos pendientes:");
        lblTurnos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTurnos);
        
        DefaultListModel<Turno> listModel = new DefaultListModel<>();
        for (Turno turno : turnosPendientes) {
            listModel.addElement(turno);
        }
        JList<Turno> turnosList = new JList<>(listModel);
        turnosList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Turno) {
                    Turno turno = (Turno) value;
                    Cliente cliente = buscarClientePorId(turno.getIdCliente());
                    String nombreCliente = cliente != null ? cliente.getNombre() : "Cliente no encontrado";
                    setText("ID: " + turno.getIdTurno() + " - " + nombreCliente + " (" + turno.getFecha() + " " + turno.getHora() + ")");
                }
                return this;
            }
        });
        turnosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(turnosList);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(20));
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnConfirmar = new JButton("Confirmar Turno Seleccionado");
        btnConfirmar.setBackground(new Color(52, 152, 219));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(e -> {
            try {
                Administrador admin = (Administrador) adminComboBox.getSelectedItem();
                Turno turnoSeleccionado = turnosList.getSelectedValue();
                
                if (admin == null) {
                    JOptionPane.showMessageDialog(ventana, "Error: Debe seleccionar un administrador.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (turnoSeleccionado == null) {
                    JOptionPane.showMessageDialog(ventana, "Error: Debe seleccionar un turno.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean confirmado = turnoService.confirmarTurno(admin, turnoSeleccionado.getIdTurno());
                
                if (confirmado) {
                    mostrarMensaje("✅ TURNO CONFIRMADO EXITOSAMENTE\n" +
                                  "ID del turno: " + turnoSeleccionado.getIdTurno() + "\n" +
                                  "Administrador: " + admin.getNombre() + "\n" +
                                  "=====================================\n");
                    
                    // Enviar notificación
                    notificacionService.enviarConfirmacionTurno(turnoSeleccionado);
                    
                    ventana.dispose();
                } else {
                    JOptionPane.showMessageDialog(ventana, "Error: No se pudo confirmar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(e -> ventana.dispose());
        
        buttonPanel.add(btnConfirmar);
        buttonPanel.add(btnCancelar);
        panel.add(buttonPanel);
        
        ventana.add(panel, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
    
    /**
     * Método para mostrar la ventana de cancelar turno - FUNCIONALIDAD DEL SISTEMA
     */
    private void mostrarVentanaCancelar() {
        JDialog ventana = new JDialog(this, "Cancelar Turno", true);
        ventana.setSize(500, 400);
        ventana.setLocationRelativeTo(this);
        ventana.setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel titulo = new JLabel("CANCELAR TURNO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        
        // Cliente
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblCliente);
        JComboBox<Cliente> clienteCombo = new JComboBox<>(clientes.toArray(new Cliente[0]));
        clienteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Cliente) {
                    Cliente cliente = (Cliente) value;
                    setText(cliente.getNombre() + " (" + cliente.getEmail() + ")");
                }
                return this;
            }
        });
        clienteCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(clienteCombo);
        panel.add(Box.createVerticalStrut(20));
        
        // Lista de turnos del cliente
        JLabel lblTurnos = new JLabel("Turnos del cliente:");
        lblTurnos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTurnos);
        
        DefaultListModel<Turno> listModel = new DefaultListModel<>();
        JList<Turno> turnosList = new JList<>(listModel);
        turnosList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Turno) {
                    Turno turno = (Turno) value;
                    setText("ID: " + turno.getIdTurno() + " - " + turno.getFecha() + " " + turno.getHora() + " (" + turno.getEstado() + ")");
                }
                return this;
            }
        });
        turnosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(turnosList);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        panel.add(scrollPane);
        
        // Actualizar lista cuando se selecciona un cliente
        clienteCombo.addActionListener(e -> {
            Cliente clienteSeleccionado = (Cliente) clienteCombo.getSelectedItem();
            if (clienteSeleccionado != null) {
                List<Turno> turnosCliente = turnoService.buscarTurnosPorCliente(clienteSeleccionado.getId());
                listModel.clear();
                for (Turno turno : turnosCliente) {
                    listModel.addElement(turno);
                }
            }
        });
        
        panel.add(Box.createVerticalStrut(20));
        
        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnCancelarTurno = new JButton("Cancelar Turno Seleccionado");
        btnCancelarTurno.setBackground(new Color(230, 126, 34));
        btnCancelarTurno.setForeground(Color.WHITE);
        btnCancelarTurno.addActionListener(e -> {
            try {
                Cliente cliente = (Cliente) clienteCombo.getSelectedItem();
                Turno turnoSeleccionado = turnosList.getSelectedValue();
                
                if (cliente == null) {
                    JOptionPane.showMessageDialog(ventana, "Error: Debe seleccionar un cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (turnoSeleccionado == null) {
                    JOptionPane.showMessageDialog(ventana, "Error: Debe seleccionar un turno.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean cancelado = turnoService.cancelarTurno(cliente, turnoSeleccionado.getIdTurno());
                
                if (cancelado) {
                    mostrarMensaje("✅ TURNO CANCELADO EXITOSAMENTE\n" +
                                  "ID del turno: " + turnoSeleccionado.getIdTurno() + "\n" +
                                  "Cliente: " + cliente.getNombre() + "\n" +
                                  "=====================================\n");
                    
                    // Enviar notificación
                    notificacionService.enviarCancelacionTurno(turnoSeleccionado);
                    
                    // Actualizar lista
                    List<Turno> turnosCliente = turnoService.buscarTurnosPorCliente(cliente.getId());
                    listModel.clear();
                    for (Turno turno : turnosCliente) {
                        listModel.addElement(turno);
                    }
                } else {
                    JOptionPane.showMessageDialog(ventana, "Error: No se pudo cancelar el turno.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(ventana, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(231, 76, 60));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.addActionListener(e -> ventana.dispose());
        
        buttonPanel.add(btnCancelarTurno);
        buttonPanel.add(btnCerrar);
        panel.add(buttonPanel);
        
        ventana.add(panel, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
    
    /**
     * Método para listar turnos - FUNCIONALIDAD DEL SISTEMA
     */
    private void listarTurnos() {
        mostrarMensaje("=== LISTADO DE TURNOS ===\n");
        List<Turno> turnos = turnoService.getTurnos();
        
        if (turnos.isEmpty()) {
            mostrarMensaje("No hay turnos registrados en el sistema.\n");
        } else {
            for (Turno turno : turnos) {
                Cliente cliente = buscarClientePorId(turno.getIdCliente());
                String nombreCliente = cliente != null ? cliente.getNombre() : "Cliente no encontrado";
                
                mostrarMensaje("ID: " + turno.getIdTurno() + 
                              " | Cliente: " + nombreCliente +
                              " | Fecha: " + turno.getFecha() +
                              " | Hora: " + turno.getHora() +
                              " | Estado: " + turno.getEstado() +
                              " | Servicio: " + turno.getTipoServicio());
            }
            mostrarMensaje("Total de turnos: " + turnos.size() + "\n");
        }
    }
    
    /**
     * Método para mostrar estadísticas - FUNCIONALIDAD DEL SISTEMA
     */
    private void mostrarEstadisticas() {
        mostrarMensaje("=== ESTADÍSTICAS DEL SISTEMA ===\n");
        mostrarMensaje(turnoService.obtenerEstadisticas());
        mostrarMensaje(notificacionService.obtenerEstadisticas());
    }
    
    /**
     * Método auxiliar para buscar cliente por ID - ENCAPSULAMIENTO
     */
    private Cliente buscarClientePorId(int id) {
        return clientes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Método principal para ejecutar la aplicación
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        // Ejecutar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new SISLIMSwing().setVisible(true);
        });
    }
}
