CREATE TABLE `learning_courses`.`courses`
(
    `id`          BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
    `title`       VARCHAR(256)                 NOT NULL,
    `description` VARCHAR(256)                 NOT NULL,
    `is_finished` BOOLEAN                      NOT NULL DEFAULT FALSE,

    PRIMARY KEY (`id`)
);