CREATE TABLE `learning_courses`.`users_to_roles` (
                                                     `id` BIGINT AUTO_INCREMENT UNIQUE NOT NULL,
                                                     `user_id` BIGINT NOT NULL,
                                                     `role_id` BIGINT NOT NULL,

                                                     PRIMARY KEY (`id`),

                                                     CONSTRAINT `unique_user_to_role`
                                                         UNIQUE (`user_id`, `role_id`),

                                                     CONSTRAINT `fk_users_to_roles_user_id`
                                                         FOREIGN KEY (`user_id`)
                                                             REFERENCES `users`(`id`)
                                                             ON DELETE CASCADE
                                                             ON UPDATE CASCADE,
                                                     CONSTRAINT `fk_users_to_roles_role_id`
                                                         FOREIGN KEY (`role_id`)
                                                             REFERENCES `roles`(`id`)
                                                             ON DELETE CASCADE
                                                             ON UPDATE CASCADE
);