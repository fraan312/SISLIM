-- Ver todos los clientes
SELECT * FROM Cliente;

-- Ver todos los turnos con info del cliente
SELECT T.idTurno, T.fecha, T.hora, T.estado, C.nombre AS Cliente, A.nombre AS Administrador
FROM Turno T
JOIN Cliente C ON T.idCliente = C.idCliente
LEFT JOIN Administrador A ON T.idAdmin = A.idAdmin;

-- Ver notificaciones
SELECT * FROM Notificacion;

-- Borrar un turno de prueba
DELETE FROM Notificacion WHERE idTurno = 1;
DELETE FROM Turno WHERE idTurno = 1;


-- Confirmar que se borró
SELECT * FROM Turno;
