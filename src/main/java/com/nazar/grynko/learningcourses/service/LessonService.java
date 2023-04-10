package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.LessonInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonService {

    private final LessonInternalService lessonInternalService;
    private final ChapterInternalService chapterInternalService;
    private final ModelMapper modelMapper;

    @Autowired
    public LessonService(LessonInternalService lessonInternalService, ChapterInternalService chapterInternalService,
                         ModelMapper modelMapper) {
        this.lessonInternalService = lessonInternalService;
        this.chapterInternalService = chapterInternalService;
        this.modelMapper = modelMapper;
    }

    public Optional<LessonDto> get(Long id) {
        return lessonInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<LessonDto> getAllInChapter(Long chapterId) {
        return lessonInternalService.getAllInChapter(chapterId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        lessonInternalService.delete(id);
    }

    public LessonDto save(LessonDto dto, Long chapterId) {
        Lesson entity = fromDto(dto);

        Chapter chapter = chapterInternalService.get(chapterId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setChapter(chapter);

        entity = lessonInternalService.save(entity);
        return toDto(entity);
    }

    public LessonDto update(LessonDto dto, Long id) {
        Lesson entity = fromDto(dto).setId(id);
        entity = lessonInternalService.update(entity);
        return toDto(entity);
    }

    public LessonDto toDto(Lesson entity) {
        return modelMapper.map(entity, LessonDto.class);
    }

    public Lesson fromDto(LessonDto dto) {
        return modelMapper.map(dto, Lesson.class);
    }

    public boolean hasWithChapter(Long id, Long chapterId, Long courseId) {
        return lessonInternalService.hasWithChapter(id, chapterId, courseId);
    }
}
