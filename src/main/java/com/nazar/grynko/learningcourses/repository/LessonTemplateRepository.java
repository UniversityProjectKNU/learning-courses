package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonTemplateRepository extends JpaRepository<LessonTemplate, Long> {
}