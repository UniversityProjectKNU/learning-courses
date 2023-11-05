package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToLessonRepository extends JpaRepository<UserToLesson, Long> {

    @Query("select utl from UserToLesson utl where utl.user.login = ?1 and utl.lesson.id = ?2")
    Optional<UserToLesson> findByUserLoginAndLessonId(String login, Long lessonId);

    @Query("select utl from UserToLesson utl where utl.user.id = ?1 and utl.lesson.id = ?2")
    Optional<UserToLesson> findByUserIdAndLessonId(Long userId, Long lessonId);

    @Query("select utl from UserToLesson utl where utl.user.login = ?1 and utl.lesson.chapter.course.id = ?2")
    List<UserToLesson> getAllByUserLoginAndCourseId(String login, Long courseId);

    @Query("select utl from UserToLesson utl where utl.lesson.chapter.course.id = :courseId")
    List<UserToLesson> getAllByCourseId(Long courseId);

    /*
    Why this doesn't work: @Query("delete from UserToLesson utl where utl.lesson.chapter.course.id = :courseId and utl.user.id = :userId")
    */
    @Modifying
    void deleteAllByUserIdAndLessonChapterCourseId(Long userId, Long courseId);

}