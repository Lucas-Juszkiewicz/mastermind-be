USE `mastermind`;

/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;

ALTER TABLE `users`
ADD CONSTRAINT `unique_email` UNIQUE (`email`);