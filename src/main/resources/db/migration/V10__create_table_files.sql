CREATE TABLE `learning_courses`.`files` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `title` VARCHAR(256) NOT NULL,
                                            `size` INT NOT NULL DEFAULT 0
);