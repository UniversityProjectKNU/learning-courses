package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.chaptertemplate.ChapterTemplateDto;
import com.nazar.grynko.learningcourses.exception.InvalidPathException;
import com.nazar.grynko.learningcourses.model.ChapterTemplate;
import com.nazar.grynko.learningcourses.model.CourseTemplate;
import com.nazar.grynko.learningcourses.service.ChapterTemplateService;
import com.nazar.grynko.learningcourses.service.CourseTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChapterTemplateServiceWrapper {

    private final ChapterTemplateService chapterTemplateService;
    private final CourseTemplateService courseTemplateService;
    private final ModelMapper modelMapper;

    public ChapterTemplateServiceWrapper(ChapterTemplateService chapterTemplateService,
                                         CourseTemplateService courseTemplateService, ModelMapper modelMapper) {
        this.chapterTemplateService = chapterTemplateService;
        this.courseTemplateService = courseTemplateService;
        this.modelMapper = modelMapper;
    }

    public Optional<ChapterTemplateDto> get(Long id) {
        return chapterTemplateService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public void delete(Long id) {
        chapterTemplateService.delete(id);
    }

    public ChapterTemplateDto save(ChapterTemplateDto dto, Long courseTemplateId) {
        ChapterTemplate entity = fromDto(dto);

        CourseTemplate courseTemplate = courseTemplateService.get(courseTemplateId)
                .orElseThrow(InvalidPathException::new);
        entity.setCourseTemplate(courseTemplate);

        entity = chapterTemplateService.save(entity);
        return toDto(entity);
    }

    public ChapterTemplateDto update(ChapterTemplateDto dto, Long id) {
        ChapterTemplate chapterTemplate = fromDto(dto).setId(id);
        chapterTemplate = chapterTemplateService.update(chapterTemplate);
        return toDto(chapterTemplate);
    }

    public Set<ChapterTemplateDto> getAllInCourseTemplate(Long courseTemplateId) {
        Set<ChapterTemplate> chapterTemplates = chapterTemplateService.getAllInCourseTemplate(courseTemplateId);
        return chapterTemplates.stream().map(this::toDto).collect(Collectors.toSet());
    }

    private ChapterTemplateDto toDto(ChapterTemplate entity) {
        return modelMapper.map(entity, ChapterTemplateDto.class);
    }

    private ChapterTemplate fromDto(ChapterTemplateDto dto) {
        return modelMapper.map(dto, ChapterTemplate.class);
    }

}
