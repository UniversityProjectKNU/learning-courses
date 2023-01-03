package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Optional<Lesson> get(Long id) {
        return lessonRepository.findById(id);
    }

    public List<Lesson> getAll() {
        return lessonRepository.findAll();
    }

    public void delete(Long id) {
        lessonRepository.deleteById(id);
    }

    public Lesson save(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    public void update(Lesson lesson) {
//        lessonRepository.update(lesson.getId(), lesson.getNumber(), lesson.getTitle(),
//                lesson.getDescription(), lesson.getChapter().getId());
    }
    
}
