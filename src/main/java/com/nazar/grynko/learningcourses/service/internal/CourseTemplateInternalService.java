package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.repository.CourseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTemplateInternalService {

    private final CourseTemplateRepository courseTemplateRepository;

    @Autowired
    public CourseTemplateInternalService(CourseTemplateRepository courseTemplateRepository) {
        this.courseTemplateRepository = courseTemplateRepository;
    }

    public Optional<CourseTemplate> get(Long id) {
        return courseTemplateRepository.findById(id);
    }

    public List<CourseTemplate> getAll() {
        return courseTemplateRepository.findAll();
    }

    public void delete(Long id) {
        CourseTemplate entity = courseTemplateRepository.findById(id)
                        .orElseThrow(IllegalArgumentException::new);
        courseTemplateRepository.delete(entity);
    }

    public CourseTemplate save(CourseTemplate entity) {
        return courseTemplateRepository.save(entity);
    }

    public CourseTemplate update(CourseTemplate entity) {
        CourseTemplate dbCourseTemplate = courseTemplateRepository.findById(entity.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbCourseTemplate, entity);
        return courseTemplateRepository.save(entity);
    }

    private void setNullFields(CourseTemplate source, CourseTemplate destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getTitle() == null) destination.setTitle(source.getTitle());
        if(destination.getDescription() == null) destination.setDescription(source.getDescription());
    }
    
}
