package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.LessonTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LessonTemplateRepository extends JpaRepository<LessonTemplate, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE LessonTemplate lessonTemplate SET lessonTemplate.number = :number, lessonTemplate.title = :title, lessonTemplate.description = :description, lessonTemplate.chapterTemplate = :chapterTemplate WHERE lessonTemplate.id = :id")
    void update(@Param("id") Long id, @Param("number") Integer number, @Param("title") String title, @Param("description") String description, @Param("chapterTemplate") Long chapterTemplate);

}