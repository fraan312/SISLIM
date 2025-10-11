-- Insertar clientes
INSERT INTO Cliente (nombre, email, telefono, direccion)
VALUES 
('Juan Pérez', 'juanperez@email.com', '111-222-333', 'Av. Siempre Viva 123'),
('María Gómez', 'maria.gomez@email.com', '444-555-666', 'Calle Falsa 456');

-- Insertar administrador
INSERT INTO Administrador (nombre, email)
VALUES ('Admin1', 'admin1@sislim.com');

-- Insertar disponibilidades
INSERT INTO Disponibilidad (fecha, horaInicio, horaFin, zona, servicio)
VALUES 
('2025-10-01', '09:00:00', '11:00:00', 'Zona Norte', 'Limpieza básica'),
('2025-10-02', '15:00:00', '17:00:00', 'Zona Sur', 'Limpieza profunda');

-- Insertar turnos (cliente 1, disponibilidad 1)
INSERT INTO Turno (fecha, hora, duracion, tipoServicio, estado, observaciones, idCliente, idDisponibilidad, idAdmin)
VALUES 
('2025-10-01', '09:00:00', 120, 'Limpieza básica', 'Pendiente', 'Turno inicial', 1, 1, 1);

-- Insertar notificación
INSERT INTO Notificacion (mensaje, fechaEnvio, tipo, idTurno)
VALUES ('Su turno fue registrado con éxito', NOW(), 'Confirmacion', 1);
