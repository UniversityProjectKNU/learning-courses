package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> getAllByChapterId(Long chapterId);

    Optional<Lesson> getLessonByIdAndChapterIdAndChapterCourseId(Long id, Long chapterId, Long courseId);

    @Query("select l from Lesson l where l.id = ?1 and l.chapter.course.id = ?2")
    Optional<Lesson> getLessonByIdAndCourseId(Long id, Long courseId);

    @Query("select l from Lesson l where l.chapter.course.id = ?1")
    List<Lesson> getAllInCourse(Long courseId);

}