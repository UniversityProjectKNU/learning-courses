package com.nazar.grynko.learningcourses.service.internal;

import com.nazar.grynko.learningcourses.exception.domain.EntityNotFoundException;
import com.nazar.grynko.learningcourses.model.Chapter;
import com.nazar.grynko.learningcourses.model.Course;
import com.nazar.grynko.learningcourses.model.Lesson;
import com.nazar.grynko.learningcourses.repository.ChapterRepository;
import com.nazar.grynko.learningcourses.repository.LessonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    private final Long VALID_LESSON_ID = 1L;
    private final Long VALID_COURSE_ID = 1L;

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private LessonInternalService lessonInternalService;

    @Test
    @DisplayName("Given valid lesson ID. When get is called. Should return Lesson entity")
    void getLessonById_checkResult() {
        // PREPARE
        var lessonId = 1L;
        var lesson = new Lesson();
        lesson.setId(lessonId);
        lesson.setTitle("Test Lesson");

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        // ACT
        var actual = lessonInternalService.get(lessonId);

        // VERIFY
        assertNotNull(actual);
        assertEquals(lesson.getId(), actual.getId());
        assertEquals(lesson.getTitle(), actual.getTitle());

        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    @DisplayName("Given invalid lesson ID. When get is called. Should throw EntityNotFoundException")
    void getLessonById_NotFound() {
        // PREPARE
        var lessonId = 999L;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // ACT + VERIFY
        assertThrows(EntityNotFoundException.class, () -> lessonInternalService.get(lessonId));
        verify(lessonRepository, times(1)).findById(lessonId);
    }

    @Test
    @DisplayName("Given valid chapter ID. When getAllInChapter is called. Should return list of lessons")
    void getAllInChapter_checkResult() {
        // PREPARE
        var chapterId = 1L;
        var lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setTitle("Test Lesson 1");

        var lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setTitle("Test Lesson 2");

        when(lessonRepository.getAllByChapterId(chapterId)).thenReturn(List.of(lesson1, lesson2));

        // ACT
        var actual = lessonInternalService.getAllInChapter(chapterId);

        // VERIFY
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(lesson1.getId(), actual.get(0).getId());
        assertEquals(lesson1.getTitle(), actual.get(0).getTitle());
        assertEquals(lesson2.getId(), actual.get(1).getId());
        assertEquals(lesson2.getTitle(), actual.get(1).getTitle());

        verify(lessonRepository, times(1)).getAllByChapterId(chapterId);
    }

    @Test
    @DisplayName("Given invalid chapter ID. When getAllInChapter is called. Should return empty list")
    void getAllInChapter_emptyResult() {
        // PREPARE
        var chapterId = 1L;
        when(lessonRepository.getAllByChapterId(chapterId)).thenReturn(List.of());

        // ACT
        var actual = lessonInternalService.getAllInChapter(chapterId);

        // VERIFY
        assertNotNull(actual);
        assertTrue(actual.isEmpty());

        verify(lessonRepository, times(1)).getAllByChapterId(chapterId);
    }

    @Test
    @DisplayName("Given valid lesson ID. When delete is called. Should delete lesson")
    void deleteLesson_Success() {
        // PREPARE
        var lesson = new Lesson();
        lesson.setId(VALID_LESSON_ID);

        var chapter = new Chapter();
        var course = new Course();
        course.setId(VALID_COURSE_ID);
        chapter.setCourse(course);
        lesson.setChapter(chapter);

        when(lessonRepository.findById(VALID_LESSON_ID)).thenReturn(Optional.of(lesson));
        when(lessonInternalService.getNumberOfLessonsInCourse(VALID_COURSE_ID)).thenReturn(3L);
        doNothing().when(lessonRepository).delete(lesson);

        // ACT
        lessonInternalService.delete(VALID_LESSON_ID);

        // VERIFY
        verify(lessonRepository, times(1)).findById(VALID_LESSON_ID);
        verify(lessonRepository, times(1)).delete(lesson);
    }

    @Test
    @DisplayName("Given invalid lesson ID. When delete is called. Should throw EntityNotFoundException")
    void deleteLesson_NotFound() {
        // PREPARE
        var lessonId = 1L;
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        // ACT + VERIFY
        assertThrows(EntityNotFoundException.class, () -> lessonInternalService.delete(lessonId));
        verify(lessonRepository, times(1)).findById(lessonId);
        verify(lessonRepository, never()).delete(any(Lesson.class));
    }

    @Test
    @DisplayName("Given invalid number of lessons. When delete is called. Should throw IllegalStateException")
    void deleteLesson_InvalidNumberOfLessons() {
        // PREPARE
        var lesson = new Lesson();
        lesson.setId(VALID_LESSON_ID);

        var chapter = new Chapter();
        var course = new Course();
        course.setId(VALID_COURSE_ID);
        chapter.setCourse(course);
        lesson.setChapter(chapter);

        when(lessonRepository.findById(VALID_LESSON_ID)).thenReturn(Optional.of(lesson));
        when(lessonInternalService.getNumberOfLessonsInCourse(VALID_COURSE_ID)).thenReturn(1L);

        // ACT + VERIFY
        assertThrows(IllegalStateException.class, () -> lessonInternalService.delete(VALID_LESSON_ID));
        verify(lessonRepository, times(1)).findById(VALID_LESSON_ID);
        verify(lessonRepository, never()).delete(lesson);
    }

}
