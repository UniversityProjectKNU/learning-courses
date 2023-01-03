package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
}