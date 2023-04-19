CREATE TABLE `learning_courses`.`courses`
(
    `id`             BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `title`          VARCHAR(256)                 NOT NULL,
    `description`    VARCHAR(256)                 NOT NULL,
    `is_finished`    BOOLEAN                      NOT NULL DEFAULT FALSE,
    `final_feedback` VARCHAR(256),

    PRIMARY KEY (`id`)
);