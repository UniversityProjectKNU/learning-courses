package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.EnrollRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollRequestRepository extends JpaRepository<EnrollRequest, Long> {

    EnrollRequest getByCourseIdAndUserLoginAndIsActiveTrue(Long courseId, String login);

    EnrollRequest getByCourseIdAndUserIdAndIsActiveTrue(Long courseId, Long userId);

    List<EnrollRequest> getAllByCourseId(Long courseId);

    List<EnrollRequest> getAllByCourseIdAndIsActive(Long courseId, Boolean isActive);

}
