package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User user SET user.firstName = :firstName, user.lastName = :lastName, user.dateOfBirth = :dateOfBirth WHERE user.id = :id")
    void updatePublicInformation(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("dateOfBirth") Calendar dateOfBirth);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User user SET user.login = :login, user.password = :password WHERE user.id = :id")
    void updateCredentials(@Param("id") Long id, @Param("login") String login, @Param("password") String password);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User user SET user.roles = :roles WHERE user.id = :id")
    void updateRoles(@Param("id") Long id, @Param("roles") Set<Role> roles);

}