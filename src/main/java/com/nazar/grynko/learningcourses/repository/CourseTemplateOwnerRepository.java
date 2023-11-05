package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.CourseTemplateOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTemplateOwnerRepository extends JpaRepository<CourseTemplateOwner, Long> {

    CourseTemplateOwner getCourseTemplateOwnerByCourseTemplateId(Long courseTemplateId);

}
