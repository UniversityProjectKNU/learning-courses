package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.HomeworkFile;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeworkFileRepository extends JpaRepository<HomeworkFile, Long> {

    @Query("select h from HomeworkFile h where h.userToLesson = ?1")
    Optional<HomeworkFile> getByUserToLesson(UserToLesson userToLesson);

}