package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ChapterTemplateRepository extends JpaRepository<ChapterTemplate, Long> {

    @Query("SELECT chapterTemplate FROM ChapterTemplate chapterTemplate WHERE chapterTemplate.courseTemplate.id = :courseId")
    Set<ChapterTemplate> getAllChaptersInCourseTemplate(@Param("courseId") Long courseId);

    Set<ChapterTemplate> getChapterTemplatesByCourseTemplateId(@Param("courseTemplateId") Long courseTemplateId);

    Optional<ChapterTemplate> getChapterTemplateByIdAndCourseTemplateId(Long id, Long chapterTemplateId);

}
