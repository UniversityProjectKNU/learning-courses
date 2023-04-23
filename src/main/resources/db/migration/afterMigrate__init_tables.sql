##############################
##############################
DELETE FROM `roles`;

INSERT INTO `roles` (`id`, `type`)
VALUES
    (1, 'ADMIN'),
    (2, 'INSTRUCTOR'),
    (3, 'STUDENT');
##############################
##############################
DELETE FROM `users`;

INSERT INTO `users` (`id`, `login`, `password`, `first_name`, `last_name`)
VALUES
    (1, 'admin1@gmail.com', 'password', 'Nazar', 'Grynko'),
    (2, 'instructor1@gmail.com', 'password', 'Maxim', 'Veres'),
    (3, 'instructor2@gmail.com', 'password', 'Konstantin', 'Zhereb'),
    (4, 'user1@gmail.com', 'password', 'Alex', 'Bilokrynytskyi'),
    (5, 'user2@gmail.com', 'password', 'Pavlo', 'Kilko'),
    (6, 'user3@gmail.com', 'password', 'Maria', 'Kukharchuk');
##############################
##############################
DELETE FROM `users_to_roles`;

INSERT INTO `users_to_roles` (`id`, `user_id`, `role_id`)
VALUES
    (1, 1, 1),
    (2, 2, 2),
    (3, 3, 2),
    (4, 4, 3),
    (5, 5, 3),
    (6, 6, 3);
##############################
##############################
##############################
##############################
DELETE FROM `courses_templates`;

INSERT INTO `courses_templates` (`id`, `title`, `description`)
VALUES
    (1, 'C++ super course', 'This course will help you to learn basics of such language as C++.'),
    (2, 'JavaRush course', 	'The Java course is divided into 40 levels. You can move to the next level only if you have solved most of the problems of the current level.'),
    (3, 'Docker from zero to hero', 'Work out at your own pace, with the regularity that suits you. You do not have to wait for a group to get together and adjust to a rigid schedule.');
##############################
##############################
DELETE FROM `chapters_templates`;

INSERT INTO `chapters_templates` (`id`, `course_template_id`, `number`, `title`, `description`)
VALUES
    (1, 1, 1, 'Basic C++ syntax.', 'In this chapter you will learn about...'),
    (2, 1, 2, 'C++ references.', 'In this chapter you will learn about...'),
    (3, 1, 3, 'C++ libraries.', 'In this chapter you will learn about...'),
    (4, 2, 1, 'Commands and first program.', 'In this chapter you will learn about...'),
    (5, 2, 2, 'Familiarity with types and keyboard input.', 'In this chapter you will learn about...'),
    (6, 3, 1, 'DevOps story.', 'In this chapter you will learn about...'),
    (7, 3, 2, 'Getting Started.', 'In this chapter you will learn about...'),
    (8, 3, 3, 'Dev Team Productivity.', 'In this chapter you will learn about...');
##############################
##############################
DELETE FROM `lessons_templates`;

INSERT INTO `lessons_templates` (`id`, `chapter_template_id`, `number`, `title`, `description`, `max_mark`, `success_mark`)
VALUES
    (1, 1, 1, 'Numerical Bases.', 'Since we were kids, we have all used decimals to express quantities. This nomenclature that seems so logical to us may not seem so to an inhabitant of Classical Rome.', 10, 5),
    (2, 1, 2, 'Ascii Codes.', 'It is a very well-known fact that computers can manage internally only 0s (zeros) and 1s (ones).', 10, 7),
    (3, 2, 1, 'Basic concepts.', 'This section provides definitions for the specific terminology and the concepts used when describing the C programming language.', 10, 5),
    (4, 2, 2, 'C++ keywords.', 'This is a list of reserved keywords in C. Since they are used by the language, these keywords are not available for re-definition.', 12, 9),
    (5, 3, 1, 'Headers.', 'Each element of the C++ standard library is declared or defined (as appropriate) in a header . A header is not necessarily a source file', 10, 5),
    (6, 4, 1, 'Getting to know JavaRush.', 'Hi. If you are reading these lines, we confirm that these are Java lessons. Our training course is packed with practice (1500+ practice problems) and designed for an adult audience.', 10, 5),
    (7, 4, 2, 'Commands and first program.', 'A program is a set (list) of commands. First the first command is executed, then the second, then the third, and so on. When all commands are executed, the program is terminated.', 10, 5),
    (8, 4, 3, 'Screen output.', 'The body of a method consists of commands. You could even say that a method is commands combined into a group that has been given a name (method name). Both are true.', 10, 5),
    (9, 4, 4, 'Variables.', 'Variables are special things for storing data. Any data. All data in Java is stored with variables. A variable most of all resembles a box: the most common box.', 10, 5),
    (10, 5, 1, 'The future is here.', 'The attributes of the 20th century were a vacuum cleaner, a washing machine, a television set, and a car.', 8, 4),
    (11, 5, 2, 'Java course at JavaRush.', 'A unique feature of JavaRush compared to other training projects is that only Java programming is taught here. We are constantly working to make the training more effective, engaging, and accessible.', 10, 5),
    (12, 5, 3, 'Type int - integer numbers.', 'If you want to store integers in variables, you need to use the int type. The int is an abbreviation for Integer (whole from English), which kind of hints that this type allows you to store integers.', 10, 5),
    (13, 6, 1, 'LEARN 6 DevOps Tools.', 'Learn what Docker and Kubernetes are and why you might want to use them.', 10, 5),
    (14, 6, 2, 'Your First Docker Usecase - Deploy a Spring Boot Application.', 'Create Docker images for 8 Java Spring Boot Projects.', 10, 5),
    (15, 7, 1, 'Docker Concepts - Registry, Repository, Tag, Image & Containers.', 'Use Dockerfile to Automate Building of your Docker Image.', 10, 5),
    (16, 7, 2, 'Playing with Docker Images and Containers.', 'Learn Docker Commands and Docker Architecture.', 10, 5),
    (17, 8, 1, 'Understanding Docker Architecture - Docker Client, Docker Engine.', 'Create Docker Java Images with maven plugins - Dockerfile Spotify Plugin, JIB Plugin and Fabric8 Docker Maven Plugin.', 10, 5),
    (18, 8, 2, 'Why is Docker Popular?', 'You will Containerize CCS, CES Microservices, Eureka Naming Server and Zuul API Gateway with Docker and Run them using Docker Compose.', 10, 5);
##############################
##############################
##############################
##############################
DELETE FROM `courses`;

INSERT INTO `courses` (`id`, `title`, `description`, `is_finished`)
VALUES
    (1, 'C++ super course', 'This course will help you to learn basics of such language as C++.', FALSE),
    (2, 'JavaRush course', 	'The Java course is divided into 40 levels. You can move to the next level only if you have solved most of the problems of the current level.', FALSE),
    (3, 'JavaRush course', 	'The Java course is divided into 40 levels. You can move to the next level only if you have solved most of the problems of the current level.', FALSE);
##############################
##############################
DELETE FROM `chapters`;

INSERT INTO `chapters` (`id`, `course_id`, `number`, `title`, `description`, `is_finished`)
VALUES
    (1, 1, 1, 'Basic C++ syntax.', 'In this chapter you will learn about...', FALSE),
    (2, 1, 2, 'C++ references.', 'In this chapter you will learn about...', FALSE),
    (3, 1, 3, 'C++ libraries.', 'In this chapter you will learn about...', FALSE),
    (4, 2, 1, 'Commands and first program.', 'In this chapter you will learn about...', FALSE),
    (5, 2, 2, 'Familiarity with types and keyboard input.', 'In this chapter you will learn about...', FALSE),
    (6, 3, 1, 'Commands and first program.', 'In this chapter you will learn about...', FALSE),
    (7, 3, 2, 'Familiarity with types and keyboard input.', 'In this chapter you will learn about...', FALSE);
##############################
##############################
DELETE FROM `lessons`;

INSERT INTO `lessons` (`id`, `chapter_id`, `number`, `title`, `description`, `is_finished`, `max_mark`, `success_mark`)
VALUES
    (1, 1, 1, 'Numerical Bases.', 'Since we were kids, we have all used decimals to express quantities. This nomenclature that seems so logical to us may not seem so to an inhabitant of Classical Rome.', FALSE, 10, 5),
    (2, 1, 2, 'Ascii Codes.', 'It is a very well-known fact that computers can manage internally only 0s (zeros) and 1s (ones).', FALSE, 15, 7),
    (3, 2, 1, 'Basic concepts.', 'This section provides definitions for the specific terminology and the concepts used when describing the C programming language.', FALSE, 10, 5),
    (4, 2, 2, 'C++ keywords.', 'This is a list of reserved keywords in C. Since they are used by the language, these keywords are not available for re-definition.', FALSE, 10, 5),
    (5, 3, 1, 'Headers.', 'Each element of the C++ standard library is declared or defined (as appropriate) in a header . A header is not necessarily a source file', FALSE, 15, 8),
    (6, 4, 1, 'Getting to know JavaRush.', 'Hi. If you are reading these lines, we confirm that these are Java lessons. Our training course is packed with practice (1500+ practice problems) and designed for an adult audience.', FALSE, 20, 12),
    (7, 4, 2, 'Commands and first program.', 'A program is a set (list) of commands. First the first command is executed, then the second, then the third, and so on. When all commands are executed, the program is terminated.', FALSE, 20, 12),
    (8, 4, 3, 'Screen output.', 'The body of a method consists of commands. You could even say that a method is commands combined into a group that has been given a name (method name). Both are true.', FALSE, 10, 5),
    (9, 4, 4, 'Variables.', 'Variables are special things for storing data. Any data. All data in Java is stored with variables. A variable most of all resembles a box: the most common box.', FALSE, 10, 5),
    (10, 5, 1, 'The future is here.', 'The attributes of the 20th century were a vacuum cleaner, a washing machine, a television set, and a car.', FALSE, 15, 5),
    (11, 5, 2, 'Java course at JavaRush.', 'A unique feature of JavaRush compared to other training projects is that only Java programming is taught here. We are constantly working to make the training more effective, engaging, and accessible.', FALSE, 10, 5),
    (12, 5, 3, 'Type int - integer numbers.', 'If you want to store integers in variables, you need to use the int type. The int is an abbreviation for Integer (whole from English), which kind of hints that this type allows you to store integers.', FALSE, 10, 5),
    (13, 6, 1, 'Getting to know JavaRush.', 'Hi. If you are reading these lines, we confirm that these are Java lessons. Our training course is packed with practice (1500+ practice problems) and designed for an adult audience.', FALSE, 20, 12),
    (14, 6, 2, 'Commands and first program.', 'A program is a set (list) of commands. First the first command is executed, then the second, then the third, and so on. When all commands are executed, the program is terminated.', FALSE, 20, 12),
    (15, 6, 3, 'Screen output.', 'The body of a method consists of commands. You could even say that a method is commands combined into a group that has been given a name (method name). Both are true.', FALSE, 10, 5),
    (16, 6, 4, 'Variables.', 'Variables are special things for storing data. Any data. All data in Java is stored with variables. A variable most of all resembles a box: the most common box.', FALSE, 10, 5),
    (17, 7, 1, 'The future is here.', 'The attributes of the 20th century were a vacuum cleaner, a washing machine, a television set, and a car.', FALSE, 15, 5),
    (18, 7, 2, 'Java course at JavaRush.', 'A unique feature of JavaRush compared to other training projects is that only Java programming is taught here. We are constantly working to make the training more effective, engaging, and accessible.', FALSE, 10, 5),
    (19, 7, 3, 'Type int - integer numbers.', 'If you want to store integers in variables, you need to use the int type. The int is an abbreviation for Integer (whole from English), which kind of hints that this type allows you to store integers.', FALSE, 10, 5);
##############################
##############################
##############################
##############################
# DELETE FROM `users_to_courses`;
#
# INSERT INTO `users_to_courses` (`id`, `user_id`, `course_id`, `mark`, `is_passed`)
# VALUES
#     (1, 2, 1, 0, FALSE),
#     (2, 2, 2, 0, FALSE),
#     (3, 3, 2, 0, FALSE),
#     (4, 4, 1, 0, FALSE),
#     (5, 5, 1, 0, FALSE),
#     (6, 5, 2, 0, FALSE),
#     (7, 6, 2, 0, FALSE);
##############################
##############################
# DELETE FROM `users_to_lessons`;
#
# INSERT INTO `users_to_lessons`  (`id`, `user_id`, `lesson_id`, `mark`, `is_passed`)
# VALUES
#     (1, 4, 1, 10, TRUE),
#     (2, 4, 2, 12, TRUE),
#     (3, 4, 3, 10, TRUE),
#     (4, 4, 4, 0, FALSE),
#     (5, 5, 6, 12, TRUE),
#     (6, 5, 7, 12, TRUE),
#     (7, 5, 8, 8, TRUE),
#     (8, 5, 9, 0, FALSE),
#     (9, 6, 6, 14, TRUE),
#     (10, 6, 7, 0, FALSE),
#     (11, 6, 13, 12, TRUE),
#     (12, 6, 14, 12, TRUE),
#     (13, 6, 15, 0, FALSE);
##############################
##############################