package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.repository.CourseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTemplateService {

    private final CourseTemplateRepository courseTemplateRepository;

    @Autowired
    public CourseTemplateService(CourseTemplateRepository courseTemplateRepository) {
        this.courseTemplateRepository = courseTemplateRepository;
    }

    public Optional<CourseTemplate> get(Long id) {
        return courseTemplateRepository.findById(id);
    }

    public List<CourseTemplate> getAll() {
        return courseTemplateRepository.findAll();
    }

    public void delete(Long id) {
        courseTemplateRepository.deleteById(id);
    }

    public CourseTemplate save(CourseTemplate courseTemplate) {
        return courseTemplateRepository.save(courseTemplate);
    }

    public void update(CourseTemplate courseTemplate) {
        courseTemplateRepository.update(courseTemplate.getId(), courseTemplate.getTitle(), courseTemplate.getDescription());
    }
    
}
