package org.example.parkingmanagementsystem.service;


import org.example.parkingmanagementsystem.model.User;
import org.example.parkingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public List<User> allUsers() {
        return userRepository.findAll().stream().filter(User::isActiveFlag).collect(Collectors.toList());
    }

    public Optional<User> userById(long id) {
        return userRepository.findById(id).filter(User::isActiveFlag);
    }

    public Optional<User> userByEmail(String email) {
        return userRepository.findByEmail(email).filter(User::isActiveFlag);
    }

    public List<User> pendingUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActiveFlag)
                .filter(user -> !user.isApproved())
                .collect(Collectors.toList());
    }

    public List<User> approvedUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActiveFlag)
                .filter(User::isApproved).collect(Collectors.toList());
    }

    public void registerUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        user.setActiveFlag(false);
        userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
