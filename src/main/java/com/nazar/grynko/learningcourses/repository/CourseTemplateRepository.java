package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.CourseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseTemplateRepository extends JpaRepository<CourseTemplate, Long> {
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE CourseTemplate courseTemplate SET courseTemplate.title = :title, courseTemplate.description = :description WHERE courseTemplate.id = :id")
    void update(Long id, String title, String description);
    
}