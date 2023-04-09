CREATE TABLE `learning_courses`.`courses_templates` (
                                                        `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                                        `title` VARCHAR(256) NOT NULL,
                                                        `description` VARCHAR(256) NOT NULL,

                                                        PRIMARY KEY (`id`)
);