DROP TABLE IF EXISTS `learning_courses`.`users_lessons_to_files`;
DROP TABLE IF EXISTS `learning_courses`.`users_to_lessons`;
DROP TABLE IF EXISTS `learning_courses`.`users_to_courses`;

DROP TABLE IF EXISTS `learning_courses`.`lessons_templates`;
DROP TABLE IF EXISTS `learning_courses`.`chapters_templates`;
DROP TABLE IF EXISTS `learning_courses`.`courses_templates`;

DROP TABLE IF EXISTS `learning_courses`.`lessons`;
DROP TABLE IF EXISTS `learning_courses`.`chapters`;
DROP TABLE IF EXISTS `learning_courses`.`courses`;

DROP TABLE IF EXISTS `learning_courses`.`users_to_roles`;
DROP TABLE IF EXISTS `learning_courses`.`roles`;
DROP TABLE IF EXISTS `learning_courses`.`users`;

DROP TABLE IF EXISTS `learning_courses`.`files`;



CREATE TABLE `learning_courses`.`courses_templates` (
                                                        `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                                        `title` VARCHAR(256) NOT NULL,
                                                        `description` VARCHAR(256) NOT NULL,

                                                        PRIMARY KEY (`id`)
);

CREATE TABLE `learning_courses`.`chapters_templates` (
                                                         `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                                         `course_template_id` BIGINT NOT NULL,
                                                         `title` VARCHAR(256) NOT NULL,
                                                         `description` VARCHAR(256) NOT NULL,
                                                         `number` INT NOT NULL,

                                                         PRIMARY KEY (`id`),
                                                         CONSTRAINT `fk_chapters_course_template_id`
                                                             FOREIGN KEY (`course_template_id`)
                                                                 REFERENCES `learning_courses`.`courses_templates`(`id`)
                                                                 ON DELETE CASCADE
                                                                 ON UPDATE CASCADE
);

CREATE TABLE `learning_courses`.`lessons_templates` (
                                                        `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                                        `chapter_template_id` BIGINT NOT NULL,
                                                        `title` VARCHAR(256) NOT NULL,
                                                        `description` VARCHAR(256) NOT NULL,
                                                        `number` INT NOT NULL,

                                                        PRIMARY KEY (`id`),

                                                        CONSTRAINT `fk_lessons_chapter_template_id`
                                                            FOREIGN KEY (`chapter_template_id`)
                                                                REFERENCES `chapters_templates`(`id`)
                                                                ON DELETE CASCADE
                                                                ON UPDATE CASCADE
);



CREATE TABLE `learning_courses`.`courses` (
                                              `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                              `title` VARCHAR(256) NOT NULL,
                                              `description` VARCHAR(256) NOT NULL,
                                              `is_finished` BOOLEAN NOT NULL DEFAULT FALSE,
                                              `final_feedback` VARCHAR(256),

                                              PRIMARY KEY (`id`)
);

CREATE TABLE `learning_courses`.`chapters` (
                                               `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                               `course_id` BIGINT NOT NULL,
                                               `title` VARCHAR(256) NOT NULL,
                                               `description` VARCHAR(256) NOT NULL,
                                               `number` INT NOT NULL,
                                               `is_finished` BOOLEAN NOT NULL DEFAULT FALSE,
                                               `final_feedback` VARCHAR(256),

                                               PRIMARY KEY (`id`),

                                               CONSTRAINT `fk_chapters_course_id`
                                                   FOREIGN KEY (`course_id`)
                                                       REFERENCES `courses`(`id`)
                                                       ON DELETE CASCADE
                                                       ON UPDATE CASCADE
);

CREATE TABLE `learning_courses`.`lessons` (
                                              `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                              `chapter_id` BIGINT NOT NULL,
                                              `title` VARCHAR(256) NOT NULL,
                                              `description` VARCHAR(256) NOT NULL,
                                              `number` INT NOT NULL,
                                              `is_finished` BOOLEAN NOT NULL DEFAULT FALSE,
                                              `max_mark` INT NOT NULL DEFAULT 1,
                                              `success_mark` INT NOT NULL DEFAULT 1,

                                              PRIMARY KEY (`id`),

                                              CONSTRAINT `fk_lessons_chapter_id`
                                                  FOREIGN KEY (`chapter_id`)
                                                      REFERENCES `chapters`(`id`)
                                                      ON DELETE CASCADE
                                                      ON UPDATE CASCADE
);



CREATE TABLE `learning_courses`.`users` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `login` VARCHAR(256) UNIQUE NOT NULL ,
                                            `password` VARCHAR(256) NOT NULL,
                                            `first_name` VARCHAR(256) NOT NULL,
                                            `last_name` VARCHAR(256) NOT NULL,
                                            `date_of_birth` DATE NOT NULL,

                                            PRIMARY KEY (`id`)
);

CREATE TABLE `learning_courses`.`roles` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `type` VARCHAR(256) UNIQUE NOT NULL,

                                            PRIMARY KEY (`id`)
);

CREATE TABLE `learning_courses`.`users_to_roles` (
                                                     `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                                     `user_id` BIGINT NOT NULL,
                                                     `role_id` BIGINT NOT NULL,

                                                     PRIMARY KEY (`id`),

                                                     CONSTRAINT `unique_user_to_role`
                                                         UNIQUE (`user_id`, `role_id`),

                                                     CONSTRAINT `fk_users_to_roles_user_id`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `users`(`id`)
                                                             ON DELETE CASCADE
                                                             ON UPDATE CASCADE,
                                                     CONSTRAINT `fk_users_to_roles_role_id`
                                                         FOREIGN KEY (`role_id`)
                                                             REFERENCES `roles`(`id`)
                                                             ON DELETE CASCADE
                                                             ON UPDATE CASCADE
);



CREATE TABLE `learning_courses`.`files` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `title` VARCHAR(256) NOT NULL,
                                            `size` INT NOT NULL DEFAULT 0
);

CREATE TABLE `learning_courses`.`users_to_courses` (
                                                       `id` BIGINT AUTO_INCREMENT NOT NULL,
                                                       `user_id` BIGINT NOT NULL,
                                                       `course_id` BIGINT NOT NULL,
                                                       `mark` INT NOT NULL DEFAULT 0,
                                                       `is_passed` BOOLEAN NOT NULL DEFAULT FALSE,

                                                       PRIMARY KEY (`id`),

                                                       CONSTRAINT `unique_user_to_course`
                                                           UNIQUE (`user_id`, `course_id`),

                                                       CONSTRAINT `fk_users_to_courses_user_id`
                                                           FOREIGN KEY (`user_id`)
                                                               REFERENCES `users`(`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE,
                                                       CONSTRAINT `fk_users_to_courses_course_id`
                                                           FOREIGN KEY (`course_id`)
                                                               REFERENCES `courses`(`id`)
                                                               ON DELETE CASCADE
                                                               ON UPDATE CASCADE
);

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