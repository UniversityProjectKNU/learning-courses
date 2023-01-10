package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.repository.CourseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseTemplateService courseTemplateService;
    private final ChapterService chapterService;
    private final ModelMapper modelMapper;

    public CourseService(CourseRepository courseRepository, CourseTemplateService courseTemplateService,
                         ChapterService chapterService, ModelMapper modelMapper) {
        this.courseRepository = courseRepository;
        this.courseTemplateService = courseTemplateService;
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }

    public Optional<Course> get(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        courseRepository.delete(course);
    }

    public Course create(Long courseTemplateId) {
        CourseTemplate template = courseTemplateService.get(courseTemplateId)
                .orElseThrow(IllegalArgumentException::new);
        Course entity = fromTemplate(template);

        entity = courseRepository.save(entity);

        chapterService.create(template.getId(), entity);

        return entity;
    }

    private Course fromTemplate(CourseTemplate template) {
        return modelMapper.map(template, Course.class);
    }

}
