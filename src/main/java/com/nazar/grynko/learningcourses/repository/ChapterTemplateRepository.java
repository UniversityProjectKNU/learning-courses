package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChapterTemplateRepository extends JpaRepository<ChapterTemplate, Long> {

    @Query("SELECT lessonTemplate FROM LessonTemplate lessonTemplate WHERE lessonTemplate.chapterTemplate.id = :id")
    Set<LessonTemplate> getAllLessonsInChapterTemplate(@Param("id") Long id);

}
