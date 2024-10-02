CREATE TABLE `mensajes` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'primary key',
  `registrar_mensaje` text,
  `correo_mensaje` text,
  `carrera_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_mensajes_carrera_id` (`carrera_id`),
  CONSTRAINT `fk_mensajes_carrera_id` FOREIGN KEY (`carrera_id`) REFERENCES `carrera` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
