CREATE TABLE `learning_courses`.`chapters` (
                                               `id` BIGINT UNIQUE AUTO_INCREMENT NOT NULL,
                                               `course_id` BIGINT NOT NULL,
                                               `title` VARCHAR(256) NOT NULL,
                                               `description` VARCHAR(256) NOT NULL,
                                               `number` INT NOT NULL,
                                               `is_finished` BOOLEAN NOT NULL DEFAULT FALSE,
                                               `final_feedback` VARCHAR(256),

                                               PRIMARY KEY (`id`),

                                               CONSTRAINT `fk_chapters_course_id`
                                                   FOREIGN KEY (`course_id`)
                                                       REFERENCES `courses`(`id`)
                                                       ON DELETE CASCADE
                                                       ON UPDATE CASCADE
);