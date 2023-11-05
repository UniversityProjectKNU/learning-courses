package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.CourseOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseOwnerRepository extends JpaRepository<CourseOwner, Long> {

    CourseOwner getCourseOwnerByCourseId(Long courseId);

}
