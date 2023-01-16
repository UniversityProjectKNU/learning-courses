package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface LessonTemplateRepository extends JpaRepository<LessonTemplate, Long> {

    Set<LessonTemplate> getLessonTemplatesByChapterTemplateId(Long chapterTemplateId);

    Optional<LessonTemplate> getLessonTemplateByIdAndChapterTemplateIdAndChapterTemplateCourseTemplateId(Long id, Long chapterTemplateId, Long courseTemplateId);

}