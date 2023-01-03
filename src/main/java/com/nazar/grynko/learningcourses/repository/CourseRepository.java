package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}