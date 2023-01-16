package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {

    List<Chapter> findAllByCourseId(Long courseId);

    Optional<Chapter> getChapterByIdAndCourseId(Long chapterId, Long courseId);

}