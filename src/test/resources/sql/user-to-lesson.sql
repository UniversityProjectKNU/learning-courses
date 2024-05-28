INSERT INTO `users` (`id`, `login`, `password`, `first_name`, `last_name`, `role`)
VALUES
    (1, 'instructor1@gmail.com', 'password', 'Alex', 'Bilokrynytskyi', 'INSTRUCTOR'),
    (2, 'instructor2@gmail.com', 'password', 'Pavlo', 'Kilko', 'INSTRUCTOR'),
    (3, 'user1@gmail.com', 'password', 'Alex', 'Bilokrynytskyi', 'STUDENT'),
    (4, 'user2@gmail.com', 'password', 'Pavlo', 'Kilko', 'STUDENT'),
    (5, 'user3@gmail.com', 'password', 'Maria', 'Kukharchuk', 'STUDENT');
##############################
##############################
##############################
##############################
INSERT INTO `courses` (`id`, `title`, `description`, `is_finished`)
VALUES
    (1, 'Course title 1', 'Course description 1', FALSE),
    (2, 'Course title 2', 'Course description 2', FALSE);
##############################
##############################
INSERT INTO `chapters` (`id`, `course_id`, `title`, `description`, `number`, `is_finished`)
VALUES
    (1, 1, 'Chapter title 1', 'Chapter description 1', 1, FALSE),
    (2, 1, 'Chapter title 2', 'Chapter description 2', 2, FALSE),
    (3, 1, 'Chapter title 3', 'Chapter description 3', 3, FALSE),


    (4, 2, 'Chapter title 4', 'Chapter description 1', 1, FALSE),
    (5, 2, 'Chapter title 5', 'Chapter description 2', 2, FALSE);
##############################
##############################
INSERT INTO `lessons` (`id`, `chapter_id`, `title`, `description`, `max_mark`, `success_mark`, `number`, `is_finished`)
VALUES
    (1, 1, 'Lesson title 1', 'Lesson description 1', 10, 7, 1, FALSE),
    (2, 1, 'Lesson title 2', 'Lesson description 2', 10, 7, 2, FALSE),
    (3, 1, 'Lesson title 3', 'Lesson description 3', 10, 7, 3, FALSE),

    (4, 2, 'Lesson title 4', 'Lesson description 4', 10, 7, 1, FALSE),
    (5, 2, 'Lesson title 5', 'Lesson description 5', 10, 7, 2, FALSE),
    (6, 2, 'Lesson title 6', 'Lesson description 6', 10, 7, 3, FALSE),

    (7, 3, 'Lesson title 7', 'Lesson description 7', 10, 7, 1, FALSE),
    (8, 3, 'Lesson title 8', 'Lesson description 8', 10, 7, 2, FALSE),
    (9, 3, 'Lesson title 9', 'Lesson description 9', 10, 7, 3, FALSE),


    (10, 4, 'Lesson title 10', 'Lesson description 10', 10, 7, 1, FALSE),
    (11, 4, 'Lesson title 11', 'Lesson description 11', 10, 7, 2, FALSE),
    (12, 4, 'Lesson title 12', 'Lesson description 12', 10, 7, 3, FALSE),
    (13, 4, 'Lesson title 13', 'Lesson description 13', 15, 10, 4, FALSE),

    (14, 5, 'Lesson title 14', 'Lesson description 14', 10, 7, 1, FALSE),
    (15, 5, 'Lesson title 15', 'Lesson description 15', 10, 7, 2, FALSE),
    (16, 5, 'Lesson title 16', 'Lesson description 16', 15, 10, 3, FALSE),
    (17, 5, 'Lesson title 17', 'Lesson description 17', 15, 10, 4, FALSE);
##############################
##############################
##############################
##############################
INSERT INTO `users_to_courses` (`id`, `user_id`, `course_id`, `mark`, `is_passed`)
VALUES
    (1, 1, 1, 0, FALSE),
    (2, 2, 1, 0, FALSE),
    (3, 2, 2, 0, FALSE),
    (4, 3, 1, 0, FALSE),
    (5, 4, 1, 0, FALSE);
##############################
##############################
INSERT INTO `users_to_lessons`  (`id`, `user_id`, `lesson_id`, `mark`, `is_passed`)
VALUES
    (1, 3, 1, 10, TRUE),
    (2, 3, 2, 8, TRUE),
    (3, 3, 3, 7, TRUE),
    (4, 3, 4, 7, TRUE),
    (5, 3, 5, 0, FALSE),
    (6, 3, 6, 0, FALSE),
    (7, 3, 7, 0, FALSE),
    (8, 3, 8, 0, FALSE),
    (9, 3, 9, 0, FALSE),

    (10, 4, 1, 10, TRUE),
    (11, 4, 2, 8, TRUE),
    (12, 4, 3, 7, TRUE),
    (13, 4, 4, 7, TRUE),
    (14, 4, 5, 0, FALSE),
    (15, 4, 6, 0, FALSE),
    (16, 4, 7, 0, FALSE),
    (17, 4, 8, 0, FALSE),
    (18, 4, 9, 0, FALSE);

