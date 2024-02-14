package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserToCourseRepository extends JpaRepository<UserToCourse, Long> {

    List<UserToCourse> getAllByUserId(Long userId);

    List<UserToCourse> getAllByCourseId(Long courseId);

    Optional<UserToCourse> getByUserIdAndCourseId(Long userId, Long courseId);

    @Modifying
    void deleteByCourseIdAndUserId(Long courseId, Long userId);

    Optional<UserToCourse> getByUserLoginAndCourseId(String login, Long courseId);

    @Query(value = "select count(utc) from UserToCourse utc where utc.user.id = :userId")
    long getNumberOfUserToCourse(Long userId);
}