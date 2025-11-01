# SISLIM - Sistema de Gestión de Turnos de Limpieza

## Descripción
Sistema de Solicitud y Gestión de Turnos de Limpieza Domiciliaria desarrollado en Java con interfaz gráfica Swing y persistencia en MySQL mediante JDBC como parte del TP4 del Seminario de Práctica Informática.

## Estructura del Proyecto

### Aplicación Principal
- `SISLIMSwing.java` - Aplicación con interfaz gráfica Swing moderna y funcional

### Modelo de Datos (sislim/model/)
- `Usuario.java` - Clase abstracta base para usuarios (ABSTRACCIÓN)
- `Cliente.java` - Clase concreta para clientes (HERENCIA)
- `Administrador.java` - Clase concreta para administradores (HERENCIA)
- `Turno.java` - Representa un turno de limpieza (ENCAPSULAMIENTO)
- `Disponibilidad.java` - Maneja horarios disponibles (ENCAPSULAMIENTO)
- `Notificacion.java` - Sistema de notificaciones (ENCAPSULAMIENTO)

### Servicios (sislim/service/)
- `TurnoService.java` - Lógica de negocio para turnos (ENCAPSULAMIENTO) - Controlador en MVC
- `NotificacionService.java` - Gestión de notificaciones (ENCAPSULAMIENTO) - Controlador en MVC

### Acceso a Datos (sislim/dao/)
- `ConexionBD.java` - Gestión de conexión MySQL con patrón Singleton (SINGLETON)
- `ClienteDAO.java` - Acceso a datos de clientes (DAO Pattern)
- `AdministradorDAO.java` - Acceso a datos de administradores (DAO Pattern)
- `TurnoDAO.java` - Acceso a datos de turnos (DAO Pattern)
- `DisponibilidadDAO.java` - Acceso a datos de disponibilidades (DAO Pattern)
- `NotificacionDAO.java` - Acceso a datos de notificaciones (DAO Pattern)

### Base de Datos (sislim/ddbb/)
- `creacionDb.sql` - Script de creación de la base de datos MySQL
- `incercionDatos.sql` - Datos de prueba para la base de datos
- `consultas.sql` - Consultas de ejemplo

## Conceptos de POO Implementados

### 1. ABSTRACCIÓN
- Clase `Usuario` como clase abstracta que define la estructura común

### 2. ENCAPSULAMIENTO
- Atributos privados con métodos públicos (getters/setters)
- Servicios que encapsulan la lógica de negocio
- Componentes de interfaz encapsulados en métodos privados

### 3. HERENCIA
- `Cliente` y `Administrador` heredan de `Usuario`
- Reutilización de código y polimorfismo

### 4. POLIMORFISMO
- Métodos `toString()` sobrescritos en cada clase
- Uso de la clase base `Usuario` para referenciar objetos derivados
- Renderers personalizados para mostrar información amigable

### 5. SINGLETON
- `ConexionBD` implementa el patrón Singleton para gestionar una única conexión a la base de datos

### 6. DAO PATTERN
- Clases DAO para abstraer el acceso a la base de datos MySQL
- Separación de responsabilidades entre modelo, servicio y acceso a datos

### 7. MVC PATTERN
- **Modelo**: Clases del paquete `sislim.model`
- **Vista**: Interfaz gráfica `SISLIMSwing.java`
- **Controlador**: Servicios `TurnoService` y `NotificacionService` que orquestan la lógica de negocio

## Funcionalidades Principales

1. **Reservar Turno** - Interfaz gráfica para reservar turnos con validaciones
2. **Confirmar Turno** - Los administradores confirman turnos pendientes
3. **Cancelar Turno** - Los clientes pueden cancelar sus turnos
4. **Listar Turnos** - Visualización de todos los turnos del sistema
5. **Estadísticas** - Información estadística del sistema

## Características de la Interfaz

- **Diseño moderno** con colores y tipografías profesionales
- **Renderers personalizados** para mostrar información legible
- **Validaciones en tiempo real** con mensajes de error claros
- **Ventanas modales** para cada funcionalidad
- **Área de salida** para mostrar resultados y mensajes
- **Panel de información** con estadísticas del sistema

## Compilación y Ejecución

### Requisitos
- Java 8 o superior
- Compilador Java (javac)
- MySQL Server (puerto 3306 por defecto)
- Base de datos `sislim` creada (usar script `sislim/ddbb/creacionDb.sql`)
- Driver JDBC MySQL (`mysql-connector-j-8.x.x.jar`) en la carpeta `lib/`

### Configuración de MySQL

1. **Instalar MySQL Server** (XAMPP, MySQL Installer, o similar)

2. **Crear la base de datos:**
   ```sql
   -- Ejecutar el script sislim/ddbb/creacionDb.sql en MySQL
   mysql -u root -p < sislim/ddbb/creacionDb.sql
   ```

3. **Opcional - Cargar datos de prueba:**
   ```sql
   -- Ejecutar el script sislim/ddbb/incercionDatos.sql en MySQL
   mysql -u root -p < sislim/ddbb/incercionDatos.sql
   ```

4. **Configurar conexión** (si es necesario):
   - Editar `sislim/dao/ConexionBD.java`
   - Ajustar `URL`, `USUARIO` y `PASSWORD` según tu configuración de MySQL

### Descargar Driver JDBC MySQL

1. Descargar desde: https://dev.mysql.com/downloads/connector/j/
2. Extraer el archivo `.jar` (ej: `mysql-connector-j-8.0.33.jar`)
3. Colocarlo en la carpeta `lib/` del proyecto
   ```
   proyecto/
   ├── lib/
   │   └── mysql-connector-j-8.0.33.jar
   └── ...
   ```

### Compilar

```bash
javac -cp ".;lib/mysql-connector-j-8.0.33.jar" SISLIMSwing.java sislim/model/*.java sislim/service/*.java sislim/dao/*.java
```

**Nota:** Ajustar el nombre del archivo `.jar` según la versión descargada.

### Ejecutar

```bash
java -cp ".;lib/mysql-connector-j-8.0.33.jar" SISLIMSwing
```

### Verificación
Si la compilación es exitosa y MySQL está ejecutándose, se abrirá una ventana con la interfaz gráfica del sistema SISLIM conectado a la base de datos.

## Uso del Sistema

1. Al ejecutar la aplicación, se abre una ventana con interfaz gráfica
2. Usa los botones del panel derecho para acceder a las funcionalidades
3. Cada funcionalidad abre una ventana modal con formularios específicos
4. Los resultados se muestran en el área central de la aplicación
5. El panel izquierdo muestra información del sistema en tiempo real

## Datos de Prueba

El sistema puede cargar datos desde la base de datos MySQL:
- Ejecutar el script `sislim/ddbb/incercionDatos.sql` para insertar datos de prueba
- O crear clientes, administradores y disponibilidades directamente desde la interfaz
- Los datos se persisten automáticamente en MySQL mediante JDBC

## Notas Técnicas

### TP4 - Persistencia con MySQL y JDBC
- **Persistencia real**: Todos los datos se almacenan en MySQL mediante JDBC
- **Patrón DAO**: Clases DAO para abstraer el acceso a la base de datos
- **Patrón MVC**: Separación clara entre Modelo, Vista y Controlador
- **Patrón Singleton**: Gestión única de conexión a la base de datos
- **CRUD completo**: Crear, Leer, Actualizar y Eliminar turnos directamente desde MySQL

### Tecnologías
- Desarrollado en Java 8+ con Swing
- **MySQL** como base de datos relacional
- **JDBC** para conectividad con la base de datos
- Manejo de fechas con `LocalDate` y `LocalTime`
- Interfaz responsive con `BorderLayout` y `BoxLayout`
- Renderers personalizados para mejor experiencia de usuario

## Estructura de la Base de Datos

El sistema utiliza las siguientes tablas en MySQL:
- `Cliente` - Información de clientes
- `Administrador` - Información de administradores
- `Disponibilidad` - Horarios disponibles para turnos
- `Turno` - Turnos reservados con estado (Pendiente, Confirmado, Cancelado)
- `Notificacion` - Historial de notificaciones enviadas

Ver `sislim/ddbb/creacionDb.sql` para el esquema completo.

## Solución de Problemas

### Error: "No se pudo conectar a la base de datos MySQL"
- Verificar que MySQL Server esté ejecutándose
- Confirmar que la base de datos `sislim` existe
- Revisar usuario y contraseña en `ConexionBD.java`
- Verificar que el puerto 3306 esté disponible

### Error: "Driver MySQL cargado correctamente"
- Verificar que el archivo `.jar` esté en `lib/`
- Confirmar que el nombre del archivo coincida con el usado en el comando de compilación
- Descargar la versión correcta del driver JDBC

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
- El driver JDBC no está en el classpath
- Verificar que el comando de compilación y ejecución incluya el driver con `-cp`

---

**Desarrollado por:** Francisco Damian Segovia  
**Materia:** Seminario de Práctica Informática  
**Universidad:** Siglo 21  
**TP4:** Implementación en Java con POO, Swing, MySQL y JDBC