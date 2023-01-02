package com.nazar.grynko.learningcourses.service;

import com.nazar.grynko.learningcourses.model.User;
import com.nazar.grynko.learningcourses.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void update(User user) {
        userRepository.update(user.getId(), user.getFirstName(), user.getLastName(), user.getDateOfBirth());
    }
}
