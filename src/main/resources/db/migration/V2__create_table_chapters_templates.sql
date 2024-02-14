CREATE TABLE `learning_courses`.`chapters_templates`
(
    `id`                 BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `course_template_id` BIGINT                       NOT NULL,
    `title`              VARCHAR(512)                 NOT NULL,
    `description`        VARCHAR(2048)                NOT NULL,
    `number`             INT                          NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `fk_chapters_course_template_id`
        FOREIGN KEY (`course_template_id`)
            REFERENCES `learning_courses`.`courses_templates` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);