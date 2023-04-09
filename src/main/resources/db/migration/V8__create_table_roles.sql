CREATE TABLE `learning_courses`.`roles` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `type` VARCHAR(256) UNIQUE NOT NULL,

                                            PRIMARY KEY (`id`)
);