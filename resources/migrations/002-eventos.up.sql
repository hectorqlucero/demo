CREATE TABLE `eventos` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) DEFAULT NULL,
  `detalles` text,
  `lugar` text,
  `fecha` date DEFAULT NULL,
  `hora` time DEFAULT NULL,
  `organiza` varchar(100) DEFAULT NULL,
  `imagen` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
