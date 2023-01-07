package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CourseTemplateRepository extends JpaRepository<CourseTemplate, Long> {

    @Query("SELECT chapterTemplate FROM ChapterTemplate chapterTemplate WHERE chapterTemplate.courseTemplate.id = :id")
    Set<ChapterTemplate> getAllChaptersInCourseTemplate(@Param("id") Long id);
    
}