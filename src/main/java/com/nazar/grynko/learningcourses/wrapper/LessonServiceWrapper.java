package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.lesson.LessonDto;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.LessonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LessonServiceWrapper {

    private final LessonService lessonService;
    private final ChapterService chapterService;
    private final ModelMapper modelMapper;

    @Autowired
    public LessonServiceWrapper(LessonService lessonService, ChapterService chapterService,
                                ModelMapper modelMapper) {
        this.lessonService = lessonService;
        this.chapterService = chapterService;
        this.modelMapper = modelMapper;
    }

    public Optional<LessonDto> get(Long id) {
        return lessonService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<LessonDto> getAllInChapter(Long chapterId) {
        return lessonService.getAllInChapter(chapterId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        lessonService.delete(id);
    }

    public LessonDto save(LessonDto dto, Long chapterId) {
        Lesson entity = fromDto(dto);

        Chapter chapter = chapterService.get(chapterId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setChapter(chapter);

        entity = lessonService.save(entity);
        return toDto(entity);
    }

    public LessonDto update(LessonDto dto, Long id) {
        Lesson entity = fromDto(dto).setId(id);
        entity = lessonService.update(entity);
        return toDto(entity);
    }

    public LessonDto toDto(Lesson entity) {
        return modelMapper.map(entity, LessonDto.class);
    }

    public Lesson fromDto(LessonDto dto) {
        return modelMapper.map(dto, Lesson.class);
    }


}
