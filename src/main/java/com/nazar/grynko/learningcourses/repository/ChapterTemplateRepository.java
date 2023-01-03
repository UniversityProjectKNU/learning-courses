package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChapterTemplateRepository extends JpaRepository<ChapterTemplate, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE ChapterTemplate chapterTemplate SET chapterTemplate.number = :number, chapterTemplate.title = :title, chapterTemplate.description = :description, chapterTemplate.courseTemplate = :courseTemplate WHERE chapterTemplate.id = :id")
    void update(@Param("id") Long id, @Param("number") Integer number, @Param("title") String title, @Param("description") String description, @Param("courseTemplate") CourseTemplate courseTemplate);

}
