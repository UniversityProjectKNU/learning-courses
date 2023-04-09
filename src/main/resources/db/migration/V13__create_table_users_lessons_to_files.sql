CREATE TABLE `learning_courses`.`users_lessons_to_files` (
                                                             `id` BIGINT AUTO_INCREMENT NOT NULL,
                                                             `user_to_lesson_id` BIGINT NOT NULL,
                                                             `file_id` BIGINT NOT NULL,

                                                             PRIMARY KEY (`id`),

                                                             CONSTRAINT `unique_user_to_lesson_file`
                                                                 UNIQUE (`user_to_lesson_id`, `file_id`),

                                                             CONSTRAINT `fk_users_to_files_user_to_lesson_id`
                                                                 FOREIGN KEY (`user_to_lesson_id`)
                                                                     REFERENCES `users_to_lessons`(`id`)
                                                                     ON DELETE CASCADE
                                                                     ON UPDATE CASCADE,
                                                             CONSTRAINT `fk_users_to_files_file_id`
                                                                 FOREIGN KEY (`file_id`)
                                                                     REFERENCES `files`(`id`)
                                                                     ON DELETE CASCADE
                                                                     ON UPDATE CASCADE
);