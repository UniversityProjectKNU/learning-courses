package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.ChapterTemplate;
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
    @Query("UPDATE LessonTemplate lessonTemplate SET lessonTemplate.number = :number, lessonTemplate.title = :title, lessonTemplate.description = :description WHERE lessonTemplate.id = :id")
    void updatePublicInformation(@Param("id") Long id, @Param("number") Integer number, @Param("title") String title, @Param("description") String description);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE LessonTemplate lessonTemplate SET lessonTemplate.chapterTemplate = :chapterTemplate WHERE lessonTemplate.id = :id")
    void updateChapterTemplate(@Param("id") Long id, @Param("chapterTemplate") ChapterTemplate chapterTemplate);

}