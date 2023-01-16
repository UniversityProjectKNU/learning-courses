package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> getAllByChapterId(Long chapterId);

    Optional<Lesson> getLessonByIdAndChapterIdAndChapterCourseId(Long id, Long chapterId, Long courseId);
}