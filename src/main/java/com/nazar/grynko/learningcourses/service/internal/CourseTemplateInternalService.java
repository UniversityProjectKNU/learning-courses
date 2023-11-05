package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.CourseTemplateOwnerRepository;
import com.nazar.grynko.learningcourses.repository.CourseTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class CourseTemplateInternalService {

    private final CourseTemplateRepository courseTemplateRepository;
    private final CourseTemplateOwnerRepository courseTemplateOwnerRepository;

    @Autowired
    public CourseTemplateInternalService(CourseTemplateRepository courseTemplateRepository,
                                         CourseTemplateOwnerRepository courseTemplateOwnerRepository) {
        this.courseTemplateRepository = courseTemplateRepository;
        this.courseTemplateOwnerRepository = courseTemplateOwnerRepository;
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
        fillNullFields(dbCourseTemplate, entity);
        return courseTemplateRepository.save(entity);
    }

    private void fillNullFields(CourseTemplate source, CourseTemplate destination) {
        if (isNull(destination.getId())) destination.setId(source.getId());
        if (isNull(destination.getTitle())) destination.setTitle(source.getTitle());
        if (isNull(destination.getDescription())) destination.setDescription(source.getDescription());
    }

    public User getCourseTemplateOwner(Long courseTemplateId) {
        return courseTemplateOwnerRepository.getCourseTemplateOwnerByCourseTemplateId(courseTemplateId).getUser();
    }
}
