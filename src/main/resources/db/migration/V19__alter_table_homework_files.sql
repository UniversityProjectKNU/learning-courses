ALTER TABLE learning_courses.homework_files
    RENAME COLUMN title TO s3_name,
    ADD COLUMN title VARCHAR(512) NOT NULL;