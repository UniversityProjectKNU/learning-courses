package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterTemplateRepository extends JpaRepository<ChapterTemplate, Long> {

    List<ChapterTemplate> getChapterTemplatesByCourseTemplateId(@Param("courseTemplateId") Long courseTemplateId);

    Optional<ChapterTemplate> getChapterTemplateByIdAndCourseTemplateId(Long id, Long chapterTemplateId);

}
