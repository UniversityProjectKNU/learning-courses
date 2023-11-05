CREATE TABLE `learning_courses`.`course_owners`
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    user_id   BIGINT                NOT NULL,
    course_id BIGINT                NOT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_course_owners_to_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_course_owners_to_course_template_id`
        FOREIGN KEY (`course_id`)
            REFERENCES `learning_courses`.`courses` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

