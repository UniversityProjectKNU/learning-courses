package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.RoleType;
import com.nazar.grynko.learningcourses.model.UserToCourse;
import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.CourseOwnerRepository;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import com.nazar.grynko.learningcourses.repository.UserToCourseRepository;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserToCourseInternalService {

    private final UserToCourseRepository userToCourseRepository;
    private final UserToLessonRepository userToLessonRepository;
    private final LessonRepository lessonRepository;
    private final CourseOwnerRepository courseOwnerRepository;

    public UserToCourseInternalService(UserToCourseRepository userToCourseRepository,
                                       UserToLessonRepository userToLessonRepository,
                                       LessonRepository lessonRepository,
                                       CourseOwnerRepository courseOwnerRepository) {
        this.userToCourseRepository = userToCourseRepository;
        this.userToLessonRepository = userToLessonRepository;
        this.lessonRepository = lessonRepository;
        this.courseOwnerRepository = courseOwnerRepository;
    }

    public UserToCourse save(UserToCourse entity) {
        return userToCourseRepository.save(entity);
    }

    public List<UserToCourse> getAllByUserId(Long userId) {
        return userToCourseRepository.getAllByUserId(userId);
    }

    public List<UserToCourse> getAllByCourseId(Long courseId) {
        return userToCourseRepository.getAllByCourseId(courseId);
    }

    public Optional<UserToCourse> getByUserIdAndCourseId(Long userId, Long courseId) {
        return userToCourseRepository.getByUserIdAndCourseId(userId, courseId);
    }

    public UserToCourse update(Long userId, Long courseId, UserToCourse entity) {
        var dbEntity = userToCourseRepository.getByUserIdAndCourseId(userId, courseId)
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbEntity, entity);
        return userToCourseRepository.save(entity);
    }

    private void fillNullFields(UserToCourse source, UserToCourse destination) {
        if (destination.getId() == null) destination.setId(source.getId());
        if (destination.getCourse() == null) destination.setCourse(source.getCourse());
        if (destination.getUser() == null) destination.setUser(source.getUser());
        if (destination.getIsPassed() == null) destination.setIsPassed(source.getIsPassed());
        if (destination.getMark() == null) destination.setMark(source.getMark());
        if (destination.getFinalFeedback() == null) destination.setFinalFeedback(source.getFinalFeedback());
    }

    public void finish(Long courseId) {
        var usersToLessons = userToLessonRepository.getAllByCourseId(courseId)
                .stream()
                .filter(e -> e.getUser().getRole().equals(RoleType.STUDENT))
                .collect(Collectors.groupingBy(UserToLesson::getUser));

        var usersToCourses = userToCourseRepository.getAllByCourseId(courseId)
                .stream()
                .filter(e -> e.getUser().getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toMap(UserToCourse::getUser, Function.identity()));

        var successMark = lessonRepository.getSuccessMarkForCourse(courseId);
        for (var user : usersToLessons.keySet()) {
            var lessons = usersToLessons.get(user);

            int sum = lessons.stream()
                    .map(UserToLesson::getMark)
                    .reduce(0, Integer::sum);

            var passedAll = lessons.stream().map(UserToLesson::getIsPassed).reduce(true, (a, b) -> a && b);

            int n = lessons.size();

            var mark = sum * 1f; //(sum * 1f / n);

            var userToCourse = usersToCourses.get(user);
            userToCourse.setMark(mark)
                    .setIsPassed(passedAll && Double.compare(mark, successMark) >= 0);
        }

        userToCourseRepository.saveAll(usersToCourses.values());
    }

    @Transactional
    public void removeUserFromCourse(Long courseId, Long userId) {
        userToCourseRepository.getByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new IllegalArgumentException("There is no user with such course."));

        var courseOwner = courseOwnerRepository.getCourseOwnerByCourseId(courseId);
        if (courseOwner.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Cannot remove course owner from their course.");
        }

        userToCourseRepository.deleteByCourseIdAndUserId(courseId, userId);
        userToLessonRepository.deleteAllByUserIdAndLessonChapterCourseId(userId, courseId);
    }
}
