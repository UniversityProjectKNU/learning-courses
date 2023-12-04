package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.EnrollRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollRequestRepository extends JpaRepository<EnrollRequest, Long> {

    EnrollRequest getByCourseIdAndUserLoginAndIsActiveTrue(Long courseId, String login);

    @Query("select e from EnrollRequest e where e.id = (select max(e2.id) from EnrollRequest e2 where e2.course.id = :courseId and e2.user.id = :userId)")
    EnrollRequest getLastByCourseIdAndUserId(Long courseId, Long userId);

    List<EnrollRequest> getAllByCourseId(Long courseId);

    List<EnrollRequest> getAllByCourseIdAndIsActive(Long courseId, Boolean isActive);

}
