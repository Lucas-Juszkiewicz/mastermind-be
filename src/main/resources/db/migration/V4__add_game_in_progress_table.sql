DROP TABLE IF EXISTS `game_in_progress`;

CREATE TABLE `game_in_progress` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) NOT NULL,
  `start_time` DATETIME(6) NOT NULL,
  `round` int(1),
  `sequence` TEXT NOT NULL,
  `guesses` TEXT,
  `responses` TEXT,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_game_in_progress_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB;
