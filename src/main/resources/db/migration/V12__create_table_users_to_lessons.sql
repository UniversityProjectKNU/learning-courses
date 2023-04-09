CREATE TABLE `learning_courses`.`users_to_lessons` (
                                                       `id` BIGINT AUTO_INCREMENT NOT NULL,
                                                       `user_id` BIGINT NOT NULL,
                                                       `lesson_id` BIGINT NOT NULL,
                                                       `mark` INT NOT NULL DEFAULT 0,
                                                       `is_passed` BOOLEAN NOT NULL DEFAULT FALSE,

                                                       PRIMARY KEY (`id`),

                                                       CONSTRAINT `unique_user_to_lesson_id`
                                                           UNIQUE (`user_id`, `lesson_id`),

                                                       CONSTRAINT `fk_users_to_lessons_user_id`
                                                           FOREIGN KEY (`user_id`)
                                                               REFERENCES `users`(`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE,
                                                       CONSTRAINT `fk_users_to_lessons_lesson_id`
                                                           FOREIGN KEY (`lesson_id`)
                                                               REFERENCES `lessons`(`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE
);