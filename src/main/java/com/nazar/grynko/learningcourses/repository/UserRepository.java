package com.nazar.grynko.learningcourses.repository;

import com.nazar.grynko.learningcourses.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE User user SET user.firstName = :firstName, user.lastName = :lastName, user.dateOfBirth = :dateOfBirth WHERE user.id = :id")
    void update(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("dateOfBirth") Calendar dateOfBirth);

}