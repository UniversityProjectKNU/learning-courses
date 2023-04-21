package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserToCourseRepository extends JpaRepository<UserToCourse, Long> {

    List<UserToCourse> getAllByUserId(Long userId);

    List<UserToCourse> getAllByCourseId(Long courseId);
}