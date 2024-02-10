package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.dto.chapter.ChapterDto;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoSave;
import com.nazar.grynko.learningcourses.dto.chapter.ChapterDtoUpdate;
import com.nazar.grynko.learningcourses.mapper.ChapterMapper;
import com.nazar.grynko.learningcourses.service.internal.ChapterInternalService;
import com.nazar.grynko.learningcourses.service.internal.CourseInternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChapterService {

    private final ChapterInternalService chapterInternalService;
    private final CourseInternalService courseInternalService;
    private final ChapterMapper chapterMapper;

    @Autowired
    public ChapterService(ChapterInternalService chapterInternalService,
                          CourseInternalService courseInternalService,
                          ChapterMapper chapterMapper) {
        this.chapterInternalService = chapterInternalService;
        this.courseInternalService = courseInternalService;
        this.chapterMapper = chapterMapper;
    }

    public ChapterDto get(Long chapterId) {
        var chapter = chapterInternalService.get(chapterId);
        return chapterMapper.toDto(chapter);
    }

    public List<ChapterDto> getAllInCourse(Long courseId) {
        courseInternalService.throwIfMissingCourse(courseId);
        return chapterInternalService.getAllInCourse(courseId)
                .stream()
                .map(chapterMapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        chapterInternalService.delete(id);
    }

    public ChapterDto save(ChapterDtoSave dto, Long courseId) {
        var course = courseInternalService.get(courseId);

        var entity = chapterMapper.fromDtoSave(dto)
                .setCourse(course)
                .setIsFinished(false);

        entity = chapterInternalService.save(entity);
        return chapterMapper.toDto(entity);
    }

    public ChapterDto update(ChapterDtoUpdate dto, Long id) {
        var entity = chapterMapper.fromDtoUpdate(dto).setId(id);
        entity = chapterInternalService.update(entity);
        return chapterMapper.toDto(entity);
    }

}
