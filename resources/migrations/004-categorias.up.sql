CREATE TABLE `categorias` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(200) DEFAULT NULL,
  `carrera_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_categorias_carrera_id` (`carrera_id`),
  CONSTRAINT `fk_categorias_carrera_id` FOREIGN KEY (`carrera_id`) REFERENCES `carrera` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
