package com.nazar.grynko.learningcourses.wrapper;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.ChapterService;
import com.nazar.grynko.learningcourses.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChapterServiceWrapper {

    private final ChapterService chapterService;
    private final CourseService courseService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChapterServiceWrapper(ChapterService chapterService, CourseService courseService,
                                 ModelMapper modelMapper) {
        this.chapterService = chapterService;
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }

    public Optional<ChapterDto> get(Long id) {
        return chapterService.get(id)
                .flatMap(val -> Optional.of(toDto(val)));
    }

    public List<ChapterDto> getAllInCourse(Long courseId) {
        return chapterService.getAllInCourse(courseId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        chapterService.delete(id);
    }

    public ChapterDto save(ChapterDto dto, Long courseId) {
        Chapter entity = fromDto(dto);

        Course course = courseService.get(courseId)
                .orElseThrow(IllegalArgumentException::new);
        entity.setCourse(course);

        entity = chapterService.save(entity);
        return toDto(entity);
    }

    public ChapterDto update(ChapterDto dto, Long id) {
        Chapter entity = fromDto(dto).setId(id);
        entity = chapterService.update(entity);
        return toDto(entity);
    }

    public ChapterDto toDto(Chapter entity) {
        return modelMapper.map(entity, ChapterDto.class);
    }

    public Chapter fromDto(ChapterDto dto) {
        return modelMapper.map(dto, Chapter.class);
    }

    public boolean hasWithCourse(Long id, Long courseId) {
        return chapterService.hasWithCourse(id, courseId);
    }
}
