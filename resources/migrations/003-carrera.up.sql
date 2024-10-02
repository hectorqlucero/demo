CREATE TABLE `carrera` (
  `id` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(200) DEFAULT NULL,
  `activa` char(1) DEFAULT NULL COMMENT 'S=si,N=No',
  `p1` text,
  `p2` text,
  `p3` text,
  `p4` text,
  `d1` text,
  `d2` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
