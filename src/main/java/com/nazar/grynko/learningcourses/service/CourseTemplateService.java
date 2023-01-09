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
        CourseTemplate courseTemplate = courseTemplateRepository.findById(id)
                        .orElseThrow(IllegalArgumentException::new);
        courseTemplateRepository.delete(courseTemplate);
    }

    public CourseTemplate save(CourseTemplate courseTemplate) {
        return courseTemplateRepository.save(courseTemplate);
    }

    public CourseTemplate update(CourseTemplate courseTemplate) {
        CourseTemplate dbCourseTemplate = courseTemplateRepository.findById(courseTemplate.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbCourseTemplate, courseTemplate);
        return courseTemplateRepository.save(courseTemplate);
    }

    private void setNullFields(CourseTemplate source, CourseTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
    }
    
}
