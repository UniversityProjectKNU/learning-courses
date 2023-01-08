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
        User dbUser = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        userRepository.delete(dbUser);
    }

    public User update(User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(IllegalArgumentException::new);
        fillNullFields(dbUser, user);
        return userRepository.save(user);
    }

    public void fillNullFields(User source, User destination) {
        if(destination.getId() == null) destination.setId(source.getId());
        if(destination.getLogin() == null) destination.setLogin(source.getLogin());
        if(destination.getPassword() == null) destination.setPassword(source.getPassword());
        if(destination.getFirstName() == null) destination.setFirstName(source.getFirstName());
        if(destination.getLastName() == null) destination.setLastName(source.getLastName());
        if(destination.getDateOfBirth() == null) destination.setDateOfBirth(source.getDateOfBirth());
        if(destination.getRoles() == null || destination.getRoles().size() == 0) destination.setRoles(source.getRoles());
    }

}
