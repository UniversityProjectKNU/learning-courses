CREATE TABLE `learning_courses`.`lessons_templates`
(
    `id`                  BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `chapter_template_id` BIGINT                       NOT NULL,
    `title`               VARCHAR(256)                 NOT NULL,
    `description`         VARCHAR(256)                 NOT NULL,
    `number`              INT                          NOT NULL,
    `max_mark`            INT                          NOT NULL DEFAULT 1,
    `success_mark`        INT                          NOT NULL DEFAULT 1,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_lessons_chapter_template_id`
        FOREIGN KEY (`chapter_template_id`)
            REFERENCES `chapters_templates` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);