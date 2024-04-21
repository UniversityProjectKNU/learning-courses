package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> getAllByChapterId(Long chapterId);

    @Query("select l from Lesson l where l.chapter.course.id = ?1")
    List<Lesson> getAllInCourse(Long courseId);

    @Query("select sum (l.successMark) from Lesson l where l.chapter.course.id = :courseId")
    Float getSuccessMarkForCourse(Long courseId);

    @Query("select sum (l.maxMark) from Lesson l where l.chapter.course.id = :courseId")
    Float getMaxMarkForCourse(Long courseId);

    @Query("select count(l) from Lesson l where l.chapter.course.id = :courseId")
    Long getNumberOfLessonsInCourse(Long courseId);

    @Query("select count(l) from Lesson l where l.chapter.id = :chapterId")
    Long getNumberOfLessonsInChapter(Long chapterId);
}