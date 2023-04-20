package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserToLessonRepository extends JpaRepository<UserToLesson, Long> {
}