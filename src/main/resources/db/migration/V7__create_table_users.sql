CREATE TABLE `learning_courses`.`users` (
                                            `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                            `login` VARCHAR(256) UNIQUE NOT NULL ,
                                            `password` VARCHAR(256) NOT NULL,
                                            `first_name` VARCHAR(256) NOT NULL,
                                            `last_name` VARCHAR(256) NOT NULL,
                                            `date_of_birth` DATE NOT NULL,

                                            PRIMARY KEY (`id`)
);