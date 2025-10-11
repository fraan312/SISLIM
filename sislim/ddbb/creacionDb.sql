-- Crear la base de datos
CREATE DATABASE sislim;
USE sislim;

-- Tabla Cliente
CREATE TABLE Cliente (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    direccion VARCHAR(200)
);

-- Tabla Administrador
CREATE TABLE Administrador (
    idAdmin INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

-- Tabla Disponibilidad
CREATE TABLE Disponibilidad (
    idDisponibilidad INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    horaInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    zona VARCHAR(50),
    servicio VARCHAR(50)
);

-- Tabla Turno
CREATE TABLE Turno (
    idTurno INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    duracion INT NOT NULL,
    tipoServicio VARCHAR(50),
    estado ENUM('Pendiente','Confirmado','Cancelado') DEFAULT 'Pendiente',
    observaciones VARCHAR(255),
    idCliente INT NOT NULL,
    idDisponibilidad INT NOT NULL,
    idAdmin INT,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
    FOREIGN KEY (idDisponibilidad) REFERENCES Disponibilidad(idDisponibilidad),
    FOREIGN KEY (idAdmin) REFERENCES Administrador(idAdmin)
);

-- Tabla Notificacion
CREATE TABLE Notificacion (
    idNotificacion INT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(255) NOT NULL,
    fechaEnvio DATETIME NOT NULL,
    tipo ENUM('Recordatorio','Confirmacion','Aviso'),
    idTurno INT NOT NULL,
    FOREIGN KEY (idTurno) REFERENCES Turno(idTurno)
);
