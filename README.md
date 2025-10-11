# SISLIM - Sistema de Gestión de Turnos de Limpieza

## Descripción
Sistema de Solicitud y Gestión de Turnos de Limpieza Domiciliaria desarrollado en Java con interfaz gráfica Swing como parte del TP3 del Seminario de Práctica Informática.

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
- `TurnoService.java` - Lógica de negocio para turnos (ENCAPSULAMIENTO)
- `NotificacionService.java` - Gestión de notificaciones (ENCAPSULAMIENTO)

### Base de Datos (base_de_datos_sislim/)
- `creacionDb.sql` - Script de creación de la base de datos
- `insercionDatos.sql` - Datos de prueba
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

### Compilar
```bash
javac -cp "." SISLIMSwing.java sislim/model/*.java sislim/service/*.java
```

### Ejecutar
```bash
java SISLIMSwing
```

### Verificación
Si la compilación es exitosa, se abrirá una ventana con la interfaz gráfica del sistema SISLIM.

## Uso del Sistema

1. Al ejecutar la aplicación, se abre una ventana con interfaz gráfica
2. Usa los botones del panel derecho para acceder a las funcionalidades
3. Cada funcionalidad abre una ventana modal con formularios específicos
4. Los resultados se muestran en el área central de la aplicación
5. El panel izquierdo muestra información del sistema en tiempo real

## Datos de Prueba

El sistema incluye datos de prueba predefinidos:
- **3 clientes**: Juan Pérez, María Gómez, Carlos López
- **2 administradores**: Admin1, Admin2
- **Disponibilidades** de horarios para diferentes zonas
- **Sistema de notificaciones** funcional

## Notas Técnicas

- Desarrollado en Java 8+ con Swing
- Utiliza `ArrayList` para almacenamiento en memoria
- Manejo de fechas con `LocalDate` y `LocalTime`
- Interfaz responsive con `BorderLayout` y `BoxLayout`
- Renderers personalizados para mejor experiencia de usuario
- Estructura preparada para integración con base de datos (TP4)

---

**Desarrollado por:** Francisco Damian Segovia  
**Materia:** Seminario de Práctica Informática  
**Universidad:** Siglo 21  
**TP3:** Implementación en Java con POO y Swing