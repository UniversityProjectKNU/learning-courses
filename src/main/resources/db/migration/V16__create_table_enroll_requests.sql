CREATE TABLE `learning_courses`.`enroll_requests`
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    user_id     BIGINT                NOT NULL,
    course_id   BIGINT                NOT NULL,
    is_active   BOOLEAN               NOT NULL DEFAULT TRUE,
    is_approved BOOLEAN               NOT NULL DEFAULT FALSE,

    PRIMARY KEY (`id`),


    CONSTRAINT `fk_enroll_requests_to_user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `users` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_enroll_requests_to_course_id`
        FOREIGN KEY (`course_id`)
            REFERENCES `courses` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);