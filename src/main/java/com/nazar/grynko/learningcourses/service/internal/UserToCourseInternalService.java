package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.UserToCourse;
import com.nazar.grynko.learningcourses.repository.UserToCourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserToCourseInternalService {

    private final UserToCourseRepository userToCourseRepository;

    public UserToCourseInternalService(UserToCourseRepository userToCourseRepository) {
        this.userToCourseRepository = userToCourseRepository;
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
}
