package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.Role;
import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void updatePublicInformation(User user) {
        userRepository.updatePublicInformation(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth());
    }

    public void updateCredentials(User user) {
        userRepository.updateCredentials(user.getId(), user.getLogin(), user.getPassword());
    }

    public void updateRoles(User user, Set<Role> roles) {
        userRepository.updateRoles(user.getId(), roles);
    }

}
