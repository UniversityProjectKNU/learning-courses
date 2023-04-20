package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.UserToCourse;
import com.nazar.grynko.learningcourses.repository.UserToCourseRepository;
import org.springframework.stereotype.Service;

@Service
public class UserToCourseInternalService {

    private final UserToCourseRepository userToCourseRepository;

    public UserToCourseInternalService(UserToCourseRepository userToCourseRepository) {
        this.userToCourseRepository = userToCourseRepository;
    }

    public UserToCourse save(UserToCourse entity) {
        return userToCourseRepository.save(entity);
    }
}
