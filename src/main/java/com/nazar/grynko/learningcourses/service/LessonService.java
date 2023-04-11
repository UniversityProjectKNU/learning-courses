package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.mapper.LessonMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.LessonInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class LessonService {

    private final LessonInternalService lessonInternalService;
    private final ChapterInternalService chapterInternalService;
    private final LessonMapper lessonMapper;

    @Autowired
    public LessonService(LessonInternalService lessonInternalService, ChapterInternalService chapterInternalService,
                         LessonMapper lessonMapper) {
        this.lessonInternalService = lessonInternalService;
        this.chapterInternalService = chapterInternalService;
        this.lessonMapper = lessonMapper;
    }

    public Optional<LessonDto> get(Long id) {
        return lessonInternalService.get(id)
                .flatMap(val -> Optional.of(lessonMapper.toDto(val)));
    }

    public List<LessonDto> getAllInChapter(Long chapterId) {
        return lessonInternalService.getAllInChapter(chapterId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        lessonInternalService.delete(id);
    }

    public LessonDto save(LessonDto dto, Long chapterId) {
        if (isNull(dto.getIsFinished()) || !dto.getIsFinished()) {
            throw new IllegalStateException();
        }

        Lesson entity = lessonMapper.fromDto(dto);

        Chapter chapter = chapterInternalService.get(chapterId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setChapter(chapter);

        entity = lessonInternalService.save(entity);
        return lessonMapper.toDto(entity);
    }

    public LessonDto update(LessonDto dto, Long id) {
        Lesson entity = lessonMapper.fromDto(dto).setId(id);
        entity = lessonInternalService.update(entity);
        return lessonMapper.toDto(entity);
    }

    public boolean hasWithChapter(Long id, Long chapterId, Long courseId) {
        return lessonInternalService.hasWithChapter(id, chapterId, courseId);
    }
}
