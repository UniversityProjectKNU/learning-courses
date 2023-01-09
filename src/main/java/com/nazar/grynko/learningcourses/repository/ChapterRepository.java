package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
}