package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToLessonRepository extends JpaRepository<UserToLesson, Long> {

    @Query("select utl from UserToLesson utl where utl.user.login = ?1 and utl.lesson.id = ?2")
    Optional<UserToLesson> findByUserLoginAndLessonId(String login, Long lessonId);

    @Query("select utl from UserToLesson utl where utl.user.login = ?1 and utl.lesson.chapter.course.id = ?2")
    List<UserToLesson> getAllByUserLoginAndCourseId(String login, Long courseId);
}