package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role getByType(RoleType type);

}