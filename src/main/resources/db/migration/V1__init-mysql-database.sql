CREATE DATABASE  IF NOT EXISTS `mastermind` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `mastermind`;

/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `nick` varchar(20) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `password` varchar(225) DEFAULT NULL,
  `games` BIGINT (20) DEFAULT NULL,
  `total` BIGINT (20) DEFAULT NULL,
  `img` TINYBLOB  DEFAULT NULL,
  `avatar` BIGINT(20) DEFAULT NULL,
  `registration_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `games`;

CREATE TABLE `games` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `duration` int(5) DEFAULT NULL,
  `attempts` int(2) DEFAULT NULL,
  `sequence` TEXT DEFAULT NULL,
  `user` BIGINT(20) DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `points` decimal (6,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `avatars`;

CREATE TABLE `avatars` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `avatar_name` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4PRIMARY;



    