CREATE TABLE `lector` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `numero` varchar(255) DEFAULT NULL,
  `rfid` varchar(255) DEFAULT NULL,
  `fecha` date DEFAULT NULL,
  `salida` time DEFAULT NULL,
  `llegada` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
