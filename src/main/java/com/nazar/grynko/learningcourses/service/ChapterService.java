package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChapterService {

    private final ChapterInternalService chapterInternalService;
    private final CourseInternalService courseInternalService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterService(ChapterInternalService chapterInternalService, CourseInternalService courseInternalService,
                          ModelMapper modelMapper) {
        this.chapterInternalService = chapterInternalService;
        this.courseInternalService = courseInternalService;
        this.modelMapper = modelMapper;
    }

    public Optional<ChapterDto> get(Long id) {
        return chapterInternalService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<ChapterDto> getAllInCourse(Long courseId) {
        return chapterInternalService.getAllInCourse(courseId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        chapterInternalService.delete(id);
    }

    public ChapterDto save(ChapterDto dto, Long courseId) {
        Chapter entity = fromDto(dto);

        Course course = courseInternalService.get(courseId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setCourse(course);

        entity = chapterInternalService.save(entity);
        return toDto(entity);
    }

    public ChapterDto update(ChapterDto dto, Long id) {
        Chapter entity = fromDto(dto).setId(id);
        entity = chapterInternalService.update(entity);
        return toDto(entity);
    }

    public ChapterDto toDto(Chapter entity) {
        return modelMapper.map(entity, ChapterDto.class);
    }

    public Chapter fromDto(ChapterDto dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    public boolean hasWithCourse(Long id, Long courseId) {
        return chapterInternalService.hasWithCourse(id, courseId);
    }
}
