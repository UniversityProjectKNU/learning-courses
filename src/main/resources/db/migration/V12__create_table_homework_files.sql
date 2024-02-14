CREATE TABLE `learning_courses`.`homework_files`
(
    `id`                BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
    `user_to_lesson_id` BIGINT                       NOT NULL,
    `title`             VARCHAR(512)                 NOT NULL,
    `size`              BIGINT                       NOT NULL DEFAULT 0,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_homework_files_user_to_lesson_id`
        FOREIGN KEY (`user_to_lesson_id`)
            REFERENCES `users_to_lessons` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);