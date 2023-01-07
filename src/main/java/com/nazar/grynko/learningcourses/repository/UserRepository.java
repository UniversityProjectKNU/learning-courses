package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}