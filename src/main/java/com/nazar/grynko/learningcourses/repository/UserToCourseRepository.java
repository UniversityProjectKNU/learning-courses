package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.UserToCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserToCourseRepository extends JpaRepository<UserToCourse, Long> {
}