package com.nazar.grynko.learningcourses.runner;

import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import com.nazar.grynko.learningcourses.service.CourseService;
import com.nazar.grynko.learningcourses.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class OnStartUpRunner implements CommandLineRunner {

    private final UserService userService;
    private final CourseService courseService;
    private final UserToLessonRepository userToLessonRepository;

    public OnStartUpRunner(UserService userService,
                           CourseService courseService,
                           UserToLessonRepository userToLessonRepository) {
        this.userService = userService;
        this.courseService = courseService;
        this.userToLessonRepository = userToLessonRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //onStartUp();
    }

    private void onStartUp() {
        var instructor1Dto = userService.get(2L).orElseThrow(IllegalArgumentException::new);
        var courseDto = courseService.create(1L, instructor1Dto.getLogin());

        var student1Dto = userService.get(4L).orElseThrow(IllegalArgumentException::new);
        courseService.enroll(courseDto.getId(), student1Dto.getId());

        var student2Dto = userService.get(5L).orElseThrow(IllegalArgumentException::new);
        courseService.enroll(courseDto.getId(), student2Dto.getId());


        var student1Lessons = userToLessonRepository.getAllByUserLoginAndCourseId(student1Dto.getLogin(), courseDto.getId());
        setRandomMark(student1Lessons);
        userToLessonRepository.saveAll(student1Lessons);

        var student2Lessons = userToLessonRepository.getAllByUserLoginAndCourseId(student2Dto.getLogin(), courseDto.getId());
        setRandomMark(student2Lessons);
        userToLessonRepository.saveAll(student2Lessons);
    }

    private void setRandomMark(List<UserToLesson> userToLessons) {
        var random = new Random();

        for (var userToLesson : userToLessons) {
            var lesson = userToLesson.getLesson();
            int mark = random.nextInt(lesson.getMaxMark()) + 1;
            userToLesson.setMark(mark);
        }
    }

}
