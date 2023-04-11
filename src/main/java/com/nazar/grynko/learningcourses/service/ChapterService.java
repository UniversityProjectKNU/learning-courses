package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.mapper.ChapterMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class ChapterService {

    private final ChapterInternalService chapterInternalService;
    private final CourseInternalService courseInternalService;
    private final ChapterMapper chapterMapper;

    @Autowired
    public ChapterService(ChapterInternalService chapterInternalService, CourseInternalService courseInternalService,
                          ChapterMapper chapterMapper) {
        this.chapterInternalService = chapterInternalService;
        this.courseInternalService = courseInternalService;
        this.chapterMapper = chapterMapper;
    }

    public Optional<ChapterDto> get(Long id) {
        return chapterInternalService.get(id)
                .flatMap(val -> Optional.of(chapterMapper.toDto(val)));
    }

    public List<ChapterDto> getAllInCourse(Long courseId) {
        return chapterInternalService.getAllInCourse(courseId)
                .stream()
                .map(chapterMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        chapterInternalService.delete(id);
    }

    public ChapterDto save(ChapterDto dto, Long courseId) {
        if (isNull(dto.getIsFinished()) || !dto.getIsFinished()) {
            throw new IllegalStateException();
        }

        Course course = courseInternalService.get(courseId)
                .orElseThrow(IllegalArgumentException::new);

        Chapter entity = chapterMapper.fromDto(dto);
        entity.setCourse(course);

        entity = chapterInternalService.save(entity);
        return chapterMapper.toDto(entity);
    }

    public ChapterDto update(ChapterDto dto, Long id) {
        Chapter entity = chapterMapper.fromDto(dto).setId(id);
        entity = chapterInternalService.update(entity);
        return chapterMapper.toDto(entity);
    }

    public boolean hasWithCourse(Long id, Long courseId) {
        return chapterInternalService.hasWithCourse(id, courseId);
    }
}
