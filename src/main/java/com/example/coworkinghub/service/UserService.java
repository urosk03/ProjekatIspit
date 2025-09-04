package com.example.coworkinghub.service;

import com.example.coworkinghub.model.User;
import com.example.coworkinghub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(User u) {
        u.setPassword(encoder.encode(u.getPassword()));
        return repo.save(u);
    }
}
