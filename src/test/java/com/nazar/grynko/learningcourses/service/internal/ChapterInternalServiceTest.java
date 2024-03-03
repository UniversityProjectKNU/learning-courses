package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.EntityNotFoundException;
import com.nazar.grynko.learningcourses.mapper.ChapterMapper;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.repository.ChapterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChapterInternalServiceTest {

    @Mock
    private ChapterRepository chapterRepository;
    @Mock
    private ChapterTemplateInternalService chapterTemplateInternalService;
    @Mock
    private LessonInternalService lessonInternalService;
    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterInternalService sut;

    @Test
    public void shouldReturnChapterWhenItExists() {
        // PREPARE
        Long chapterId = 1L;

        // MOCKING
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(new Chapter()));

        // ACT
        Chapter actualChapter = sut.get(chapterId);

        // VERIFY
        verify(chapterRepository).findById(chapterId);

        // Check the returned value
        assertNotNull(actualChapter);
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionWhenChapterNotFound() {
        // PREPARE
        Long chapterId = 100L;

        // MOCKING
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        // ACT & VERIFY
        assertThrows(EntityNotFoundException.class, () -> sut.get(chapterId));
        verify(chapterRepository).findById(chapterId);
    }


    @Test
    public void shouldDeleteCourseWhenExists() {
        // PREPARE
        Long courseId = 1L;

        // MOCKING
        when(chapterRepository.findById(courseId)).thenReturn(Optional.of(new Chapter()));
        doNothing().when(chapterRepository).delete(any(Chapter.class));

        // ACT
        sut.delete(courseId);

        // VERIFY
        verify(chapterRepository).findById(courseId);
        verify(chapterRepository).delete(any(Chapter.class));
    }


}
