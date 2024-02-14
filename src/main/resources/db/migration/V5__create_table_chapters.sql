CREATE TABLE `learning_courses`.`chapters`
(
    `id`          BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `course_id`   BIGINT                       NOT NULL,
    `title`       VARCHAR(512)                 NOT NULL,
    `description` VARCHAR(2048)                NOT NULL,
    `number`      INT                          NOT NULL,
    `is_finished` BOOLEAN                      NOT NULL DEFAULT FALSE,

    PRIMARY KEY (`id`),

    CONSTRAINT `fk_chapters_course_id`
        FOREIGN KEY (`course_id`)
            REFERENCES `courses` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);