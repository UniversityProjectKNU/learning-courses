package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.model.UserToLesson;
import com.nazar.grynko.learningcourses.repository.UserToLessonRepository;
import org.springframework.stereotype.Service;

@Service
public class UserToLessonInternalService {

    private final UserToLessonRepository userToLessonRepository;

    public UserToLessonInternalService(UserToLessonRepository userToLessonRepository) {
        this.userToLessonRepository = userToLessonRepository;
    }

    public UserToLesson save(UserToLesson entity) {
        return userToLessonRepository.save(entity);
    }

}
