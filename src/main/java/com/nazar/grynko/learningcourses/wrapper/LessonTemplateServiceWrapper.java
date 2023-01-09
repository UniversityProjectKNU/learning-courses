package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.lessontemplate.LessonTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.LessonTemplate;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.LessonTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LessonTemplateServiceWrapper {

    private final LessonTemplateService lessonTemplateService;
    private final ChapterTemplateService chapterTemplateService;
    private final ModelMapper modelMapper;

    public LessonTemplateServiceWrapper(LessonTemplateService lessonTemplateService,
                                        ChapterTemplateService chapterTemplateService, ModelMapper modelMapper) {
        this.lessonTemplateService = lessonTemplateService;
        this.chapterTemplateService = chapterTemplateService;
        this.modelMapper = modelMapper;
    }

    public Optional<LessonTemplateDto> get(Long id) {
        return lessonTemplateService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public LessonTemplateDto save(LessonTemplateDto dto, Long chapterTemplateId) {
        LessonTemplate entity = fromDto(dto);

        ChapterTemplate chapterTemplate = chapterTemplateService.get(chapterTemplateId)
                .orElseThrow(InvalidPathException::new);
        entity.setChapterTemplate(chapterTemplate);

        entity = lessonTemplateService.save(entity);
        return toDto(entity);
    }

    public void delete(Long id) {
        lessonTemplateService.delete(id);
    }

    public LessonTemplateDto update(LessonTemplateDto dto, Long id) {
        LessonTemplate entity = fromDto(dto).setId(id);
        entity = lessonTemplateService.update(entity);
        return toDto(entity);
    }

    public Set<LessonTemplateDto> getAllInChapterTemplate(Long chapterTemplateId) {
        return lessonTemplateService.getAllInChapterTemplate(chapterTemplateId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    private LessonTemplate fromDto(LessonTemplateDto dto) {
        return modelMapper.map(dto, LessonTemplate.class);
    }

    private LessonTemplateDto toDto(LessonTemplate entity) {
        return modelMapper.map(entity, LessonTemplateDto.class);
    }

}
