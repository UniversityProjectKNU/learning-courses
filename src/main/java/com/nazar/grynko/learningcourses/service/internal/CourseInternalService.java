package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.mapper.CourseMapper;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.model.UserToCourse;
import com.nazar.grynko.learningcourses.repository.CourseRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CourseInternalService {

    private final CourseRepository courseRepository;
    private final CourseTemplateInternalService courseTemplateInternalService;
    private final ChapterInternalService chapterInternalService;
    private final LessonInternalService lessonInternalService;
    private final CourseMapper courseMapper;
    private final UserToCourseInternalService userToCourseInternalService;

    public CourseInternalService(CourseRepository courseRepository, CourseTemplateInternalService courseTemplateInternalService,
                                 ChapterInternalService chapterInternalService, LessonInternalService lessonInternalService, CourseMapper courseMapper,
                                 UserToCourseInternalService userToCourseInternalService) {
        this.courseRepository = courseRepository;
        this.courseTemplateInternalService = courseTemplateInternalService;
        this.chapterInternalService = chapterInternalService;
        this.lessonInternalService = lessonInternalService;
        this.courseMapper = courseMapper;
        this.userToCourseInternalService = userToCourseInternalService;
    }

    public Optional<Course> get(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public void delete(Long id) {
        var course = courseRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        courseRepository.delete(course);
    }

    @Transactional
    public Course create(Long courseTemplateId) {
        var template = courseTemplateInternalService.get(courseTemplateId)
                .orElseThrow(IllegalArgumentException::new);

        var entity = courseMapper.fromTemplate(template).setId(null);
        entity.setIsFinished(false);

        entity = courseRepository.save(entity);

        chapterInternalService.create(template.getId(), entity);

        return entity;
    }

    public Course save(Course entity) {
        return courseRepository.save(entity);
    }

    public Course update(Course course) {
        var dbCourse = courseRepository.findById(course.getId())
                .orElseThrow(IllegalArgumentException::new);
        setNullFields(dbCourse, course);
        return courseRepository.save(course);
    }

    @Transactional
    public UserToCourse enroll(User user, Course course) {
        var entity = new UserToCourse()
                .setUser(user)
                .setCourse(course)
                .setMark(0)
                .setIsPassed(false);

        entity = userToCourseInternalService.save(entity);
        lessonInternalService.enroll(user, course.getId());

        return entity;
    }

    private void setNullFields(Course source, Course destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getTitle() == null) destination.setTitle(source.getTitle());
        if (destination.getDescription() == null) destination.setDescription(source.getDescription());
        if (destination.getIsFinished() == null) destination.setIsFinished(source.getIsFinished());
        if (destination.getFinalFeedback() == null) destination.setFinalFeedback(source.getFinalFeedback());
    }
}
