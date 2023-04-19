CREATE TABLE `learning_courses`.`users_to_courses`
(
    `id`        BIGINT AUTO_INCREMENT NOT NULL,
    `user_id`   BIGINT                NOT NULL,
    `course_id` BIGINT                NOT NULL,
    `mark`      INT                   NOT NULL DEFAULT 0,
    `is_passed` BOOLEAN               NOT NULL DEFAULT FALSE,

    PRIMARY KEY (`id`),

    CONSTRAINT `unique_user_to_course`
        UNIQUE (`user_id`, `course_id`),

    CONSTRAINT `fk_users_to_courses_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_users_to_courses_course_id`
        FOREIGN KEY (`course_id`)
            REFERENCES `courses` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);