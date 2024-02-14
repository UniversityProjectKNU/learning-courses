CREATE TABLE `learning_courses`.`courses_templates` (
                                                        `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                                        `title` VARCHAR(512) NOT NULL,
                                                        `description` VARCHAR(2048) NOT NULL,

                                                        PRIMARY KEY (`id`)
);