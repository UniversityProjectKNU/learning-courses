CREATE TABLE `learning_courses`.`lessons`
(
    `id`           BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `chapter_id`   BIGINT                       NOT NULL,
    `title`        VARCHAR(256)                 NOT NULL,
    `description`  VARCHAR(256)                 NOT NULL,
    `number`       INT                          NOT NULL,
    `is_finished`  BOOLEAN                      NOT NULL DEFAULT FALSE,
    `max_mark`     INT                          NOT NULL DEFAULT 1,
    `success_mark` INT                          NOT NULL DEFAULT 1,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_lessons_chapter_id`
        FOREIGN KEY (`chapter_id`)
            REFERENCES `chapters` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);