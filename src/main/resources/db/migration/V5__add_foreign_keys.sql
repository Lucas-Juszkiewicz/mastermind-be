USE `mastermind`;

-- Add foreign key constraint to games table
ALTER TABLE `games`
ADD CONSTRAINT `fk_games_user_id`
FOREIGN KEY (`user`) REFERENCES `users` (`id`);

-- Add foreign key constraint to users table for avatars
ALTER TABLE `users`
ADD CONSTRAINT `fk_users_avatar_id`
FOREIGN KEY (`avatar`) REFERENCES `avatars` (`id`);