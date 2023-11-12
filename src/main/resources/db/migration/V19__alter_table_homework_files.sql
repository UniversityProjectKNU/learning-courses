ALTER TABLE learning_courses.homework_files
    RENAME COLUMN title TO s3_name,
    ADD COLUMN title VARCHAR(256) NOT NULL;