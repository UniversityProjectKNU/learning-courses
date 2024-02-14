package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.CourseTemplateOwnerRepository;
import com.nazar.grynko.learningcourses.repository.CourseTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CourseTemplateInternalService {

    private final CourseTemplateRepository courseTemplateRepository;
    private final CourseTemplateOwnerRepository courseTemplateOwnerRepository;

    private static final String COURSE_TEMPLATE_MISSING_PATTERN = "Course template %d doesn't exist";

    public CourseTemplateInternalService(CourseTemplateRepository courseTemplateRepository,
                                         CourseTemplateOwnerRepository courseTemplateOwnerRepository) {
        this.courseTemplateRepository = courseTemplateRepository;
        this.courseTemplateOwnerRepository = courseTemplateOwnerRepository;
    }

    public CourseTemplate get(Long courseTemplateId) {
        return courseTemplateRepository.findById(courseTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(COURSE_TEMPLATE_MISSING_PATTERN, courseTemplateId)));
    }

    public List<CourseTemplate> getAll() {
        return courseTemplateRepository.findAll();
    }

    public void delete(Long courseTemplateId) {
        CourseTemplate entity = get(courseTemplateId);
        courseTemplateRepository.delete(entity);
    }

    public CourseTemplate save(CourseTemplate entity) {
        return courseTemplateRepository.save(entity);
    }

    public CourseTemplate update(CourseTemplate entity) {
        CourseTemplate dbCourseTemplate = get(entity.getId());
        fillNullFields(dbCourseTemplate, entity);

        return courseTemplateRepository.save(entity);
    }

    public User getCourseTemplateOwner(Long courseTemplateId) {
        throwIfMissingCourseTemplate(courseTemplateId);
        return courseTemplateOwnerRepository.getCourseTemplateOwnerByCourseTemplateId(courseTemplateId).getUser();
    }

    private void fillNullFields(CourseTemplate source, CourseTemplate destination) {
        if (isNull(destination.getId())) destination.setId(source.getId());
        if (isNull(destination.getTitle())) destination.setTitle(source.getTitle());
        if (isNull(destination.getDescription())) destination.setDescription(source.getDescription());
    }

    public void throwIfMissingCourseTemplate(Long courseTemplateId) {
        courseTemplateRepository.findById(courseTemplateId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(COURSE_TEMPLATE_MISSING_PATTERN, courseTemplateId)));
    }
}
